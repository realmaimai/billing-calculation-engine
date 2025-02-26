package com.maimai.billingcalculationengine.service;

import com.maimai.billingcalculationengine.common.properties.CurrencyProperties;
import com.maimai.billingcalculationengine.model.entity.Asset;
import com.maimai.billingcalculationengine.model.entity.BillingTier;
import com.maimai.billingcalculationengine.model.entity.Client;
import com.maimai.billingcalculationengine.model.entity.Portfolio;
import com.maimai.billingcalculationengine.repository.AssetRepository;
import com.maimai.billingcalculationengine.repository.BillingTierRepository;
import com.maimai.billingcalculationengine.repository.ClientRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

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


    BigDecimal calculatePortfolioAum(Portfolio portfolio) {
        BigDecimal portfolioBalance = BigDecimal.ZERO;
        log.info("Calculating portfolio balance for portfolioId: {}", portfolio.getPortfolioId());

        // calculate all assets value to portfolio balance
        List<Asset> assetsByPortfolioId = assetRepository.findAllByPortfolioId(portfolio.getPortfolioId());
        log.info("Found {} assets for portfolioId: {}", assetsByPortfolioId.size(), portfolio.getPortfolioId());

        for (Asset asset : assetsByPortfolioId) {
            log.info("Processing asset: id={}, currency={}, value={}",
                    asset.getAssetId(), asset.getCurrency(), asset.getAssetValue());

            // TODO: use strategic pattern + simple factory
            if (asset.getCurrency().equals("CAD")) {
                portfolioBalance = portfolioBalance.add(asset.getAssetValue());
                log.debug("Added CAD asset value: {}, new portfolio balance: {}",
                        asset.getAssetValue(), portfolioBalance);
            } else if (asset.getCurrency().equals("USD")) {
                // if this is USD, we get USD to CAD ratio from currencyProperties
                BigDecimal usdToCadRatio = BigDecimal.valueOf(currencyProperties.getToUSD());
                BigDecimal convertedValue = asset.getAssetValue().multiply(usdToCadRatio);
                portfolioBalance = portfolioBalance.add(asset.getAssetValue().divide(convertedValue, 2, RoundingMode.HALF_UP));
                log.debug("Converted USD asset value to CAD: {} (CAD to USD Ratio: {}), new portfolio balance: {}",
                        convertedValue, usdToCadRatio, portfolioBalance);
            }
        }

        log.info("Final calculated portfolio balance: {}", portfolioBalance);
        return portfolioBalance;
    }

    BigDecimal calculatePortfolioFee(BigDecimal balance, Portfolio portfolio) {
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
        BigDecimal feeRate = feePercentage.get().divide(new BigDecimal("100"), RoundingMode.UNNECESSARY);
        BigDecimal portfolioFee = balance.multiply(feeRate).setScale(2, RoundingMode.HALF_UP);

        log.info("Calculated portfolio fee: {} (fee rate: {}) for clientId: {}, portfolioId: {}",
                portfolioFee, feeRate, clientId, portfolio.getPortfolioId());

        return portfolioFee;
    }

}
