package com.maimai.billingcalculationengine.service;

import com.maimai.billingcalculationengine.common.properties.CurrencyProperties;
import com.maimai.billingcalculationengine.model.entity.Asset;
import com.maimai.billingcalculationengine.model.entity.BillingTier;
import com.maimai.billingcalculationengine.model.entity.Client;
import com.maimai.billingcalculationengine.model.entity.Portfolio;
import com.maimai.billingcalculationengine.repository.AssetRepository;
import com.maimai.billingcalculationengine.repository.BillingTierRepository;
import com.maimai.billingcalculationengine.repository.ClientRepository;
import com.maimai.billingcalculationengine.repository.PortfolioRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

/**
 * Service responsible for investment management fee calculations.
 * Provides methods for calculating portfolio AUM, fee calculations, and currency conversions.
 */
@Slf4j
@Service
public class CalculationService {
    @Resource
    private AssetRepository assetRepository;

    @Resource
    private ClientRepository clientRepository;

    @Resource
    private BillingTierRepository billingTierRepository;

    @Resource
    private CurrencyProperties currencyProperties;

    @Resource
    private PortfolioRepository portfolioRepository;


    /**
     * Calculates the total Assets Under Management (AUM) for a given portfolio.
     * <p>
     * This method:
     * 1. Retrieves all assets associated with the portfolio
     * 2. Converts each asset value to the portfolio's currency
     * 3. Sums up all converted asset values to get the total portfolio balance
     *
     * @param portfolio The portfolio entity containing ID and currency information
     * @return The total AUM value of the portfolio in the portfolio's currency
     */
    public BigDecimal calculatePortfolioAum(Portfolio portfolio) {
        BigDecimal portfolioBalance = BigDecimal.ZERO;
        String portfolioCurrency = portfolio.getPortfolioCurrency();
        log.info("Calculating portfolio balance for portfolioId: {}", portfolio.getPortfolioId());

        // calculate all assets value to portfolio balance
        List<Asset> assetsByPortfolioId = assetRepository.findAllByPortfolioId(portfolio.getPortfolioId());
        log.info("Found {} assets for portfolioId: {}", assetsByPortfolioId.size(), portfolio.getPortfolioId());

        for (Asset asset : assetsByPortfolioId) {
            log.info("Processing asset: id={}, currency={}, value={}",
                    asset.getAssetId(), asset.getCurrency(), asset.getAssetValue());

            // TODO: use strategic pattern + simple factory
            BigDecimal assetValue = asset.getAssetValue();
            log.debug("Original asset value: {} {}", assetValue, asset.getCurrency());

            BigDecimal convertedAssetValue = convertFromCadToTargetCurrency(assetValue, portfolioCurrency);
            log.debug("Converted asset value: {} {} (conversion from {} to {})",
                    convertedAssetValue, portfolioCurrency, asset.getCurrency(), portfolioCurrency);

            portfolioBalance = portfolioBalance.add(convertedAssetValue);
        }

        log.info("Final calculated portfolio balance: {}", portfolioBalance);
        return portfolioBalance;
    }

    /**
     * Calculates the management fee for a portfolio based on its balance and the client's billing tier.
     * <p>
     * This method:
     * 1. Retrieves the client associated with the portfolio
     * 2. Gets the client's billing tier ID
     * 3. Finds the applicable tier based on the balance and tier ID
     * 4. Applies the fee percentage to the portfolio balance
     *
     * @param balance The portfolio balance to calculate fees on
     * @param portfolio The portfolio entity containing client relationship information
     * @return The calculated fee amount in CAD, rounded to 2 decimal places
     * @throws RuntimeException if client or fee percentage cannot be found
     */
    public BigDecimal calculatePortfolioFee(BigDecimal balance, Portfolio portfolio) {
        String clientId = portfolio.getClientId();
        log.info("Calculating portfolio fee for clientId: {}, portfolioId: {}, balance: {}",
                clientId, portfolio.getPortfolioId(), balance);

        Optional<Client> client = clientRepository.findByClientId(clientId);
        if (client.isEmpty()) {
            log.error("Client not found for clientId: {}", clientId);
            throw new RuntimeException("Client not found");
        }

        String billingTierId = client.get().getBillingTierId();
        log.info("Retrieved billingTierId: {} for clientId: {}", billingTierId, clientId);

        // get fee percentage
        Optional<BillingTier> applicableTier = billingTierRepository.findByTierIdAndBalance(billingTierId, balance);
        Optional<BigDecimal> feePercentage = applicableTier.map(BillingTier::getFeePercentage);

        if (feePercentage.isEmpty()) {
            log.error("Fee percentage not found for billingTierId: {}, balance: {}", billingTierId, balance);
            throw new RuntimeException("Fee percentage not found");
        }

        // convert percentage to decimal (e.g., 1.25% to 0.0125)
        BigDecimal feeRate = feePercentage.get().divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP);
        log.debug("Fee percentage: {}%, converted to decimal rate: {}", feePercentage.get(), feeRate);
        BigDecimal portfolioFee = balance.multiply(feeRate).setScale(2, RoundingMode.HALF_UP);
        log.info("Calculated portfolio fee in CAD: {} (fee rate: {}) for clientId: {}, portfolioId: {}",
                portfolioFee, feeRate, clientId, portfolio.getPortfolioId());

        return portfolioFee;
    }

    /**
     * Converts an amount from CAD to the target currency.
     * <p>
     * This method:
     * 1. Checks if conversion is needed (if target is already CAD, no conversion)
     * 2. Uses the configured CAD to USD exchange rate to convert the amount
     * 3. Rounds the result to 2 decimal places
     *
     * @param amount The amount in CAD to convert
     * @param currency The target currency to convert to
     * @return The converted amount in the target currency, rounded to 2 decimal places
     */
    public BigDecimal convertFromCadToTargetCurrency (BigDecimal amount, String currency) {
        log.debug("Converting {} CAD to {}", amount, currency);
        BigDecimal convertedValue;
        if (!currency.equals("CAD")) {
            BigDecimal cadToUsdRatio= BigDecimal.valueOf(currencyProperties.getToUSD());
            log.debug("Using conversion rate: 1 CAD = {} {}", cadToUsdRatio, currency);
            convertedValue = amount.multiply(cadToUsdRatio).setScale(2, RoundingMode.HALF_UP);
        } else {
            convertedValue = amount;
            log.debug("No conversion needed, amount already in CAD");
        }

        return convertedValue;
    }

    /**
     * Calculates the total Assets Under Management (AUM) for a given client.
     * <p>
     * This method:
     * 1. Retrieves all portfolios associated with the client
     * 2. Calculates AUM for each portfolio
     * 3. Sums up all portfolio AUMs to get the total client AUM
     *
     * @param clientId The client ID for which to calculate the total AUM
     * @return The total AUM value in CAD
     */
    public BigDecimal calculateClientTotalAum(String clientId) {
        log.info("Calculating total AUM for client ID: {}", clientId);
        BigDecimal totalAum = BigDecimal.ZERO;

        List<Portfolio> portfoliosByClientId = portfolioRepository.findAllByClientId(clientId);
        log.info("Found {} portfolios for client {}", portfoliosByClientId.size(), clientId);

        for (Portfolio portfolio : portfoliosByClientId) {
            BigDecimal portfolioAum = calculatePortfolioAum(portfolio);
            log.debug("Portfolio {} AUM: ${} CAD", portfolio.getPortfolioId(), portfolioAum);
            totalAum = totalAum.add(portfolioAum);
        }

        log.info("Total AUM for client {}: ${} CAD", clientId, totalAum);
        return totalAum;
    }

    /**
     * Calculates the total management fee for a given client.
     * <p>
     * This method:
     * 1. Retrieves all portfolios associated with the client
     * 2. Calculates AUM and fee for each portfolio
     * 3. Sums up all portfolio fees to get the total client fee
     *
     * @param clientId The client ID for which to calculate the total fee
     * @return The total fee in CAD
     */
    public BigDecimal calculateClientTotalFee(String clientId) {
        log.info("Calculating total fee for client ID: {}", clientId);
        BigDecimal totalFee = BigDecimal.ZERO;

        List<Portfolio> portfoliosByClientId = portfolioRepository.findAllByClientId(clientId);
        log.debug("Found {} portfolios for client {}", portfoliosByClientId.size(), clientId);

        for (Portfolio portfolio : portfoliosByClientId) {
            BigDecimal portfolioAum = calculatePortfolioAum(portfolio);
            BigDecimal portfolioFee = calculatePortfolioFee(portfolioAum, portfolio);
            log.debug("Portfolio {} Fee: ${} CAD", portfolio.getPortfolioId(), portfolioFee);
            totalFee = totalFee.add(portfolioFee);
        }

        log.info("Total fee for client {}: ${} CAD", clientId, totalFee);
        return totalFee;
    }

    /**
     * Calculates the effective fee rate for a client based on total fee and AUM.
     * <p>
     * This method:
     * 1. Takes the total fee and total AUM values
     * 2. Divides fee by AUM and multiplies by 100 to get percentage
     * 3. Returns 0 if AUM is zero (to avoid division by zero)
     *
     * @param totalFee The total fee amount in CAD
     * @param totalAum The total AUM amount in CAD
     * @return The effective fee rate as a percentage
     */
    public BigDecimal calculateEffectiveFeeRate(BigDecimal totalFee, BigDecimal totalAum) {
        log.debug("Calculating effective fee rate - Total Fee: ${}, Total AUM: ${}", totalFee, totalAum);
        BigDecimal effectiveFeeRate = BigDecimal.ZERO;

        if (totalAum.compareTo(BigDecimal.ZERO) > 0) {
            effectiveFeeRate = totalFee
                    .multiply(new BigDecimal("100"))
                    .divide(totalAum, 2, RoundingMode.HALF_UP);
        }

        log.debug("Calculated effective fee rate: {}%", effectiveFeeRate);
        return effectiveFeeRate;
    }

}
