package com.maimai.billingcalculationengine.service;

import com.maimai.billingcalculationengine.common.annotations.TrackExecution;
import com.maimai.billingcalculationengine.common.constants.MessageConstants;
import com.maimai.billingcalculationengine.common.enums.Layer;
import com.maimai.billingcalculationengine.common.exception.AccountNotFoundException;
import com.maimai.billingcalculationengine.common.exception.InactiveAccountException;
import com.maimai.billingcalculationengine.common.exception.PasswordMismatchException;
import com.maimai.billingcalculationengine.common.exception.UserAlreadyExistsException;
import com.maimai.billingcalculationengine.common.properties.JwtProperties;
import com.maimai.billingcalculationengine.common.utils.JwtUtil;
import com.maimai.billingcalculationengine.model.dto.UserLoginDTO;
import com.maimai.billingcalculationengine.model.dto.UserRegisterDTO;
import com.maimai.billingcalculationengine.model.entity.User;
import com.maimai.billingcalculationengine.model.response.UserLoginResponse;
import com.maimai.billingcalculationengine.repository.UserRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Slf4j
@Service
public class UserService {
    @Resource
    private UserRepository userRepository;
    @Resource
    private JwtProperties jwtProperties;
    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Authenticates a user and generates a JWT token.
     *
     * This method:
     * 1. Validates user credentials against stored information
     * 2. Checks account status
     * 3. Generates a JWT token for authenticated users
     *
     * @param userLoginDTO DTO containing login credentials
     * @return UserLoginResponse with user information and authentication token
     * @throws AccountNotFoundException if user account doesn't exist
     * @throws InactiveAccountException if account is inactive
     * @throws PasswordMismatchException if password is incorrect
     */
    @TrackExecution(Layer.SERVICE)
    public UserLoginResponse login(UserLoginDTO userLoginDTO) throws AccountNotFoundException {
        String email = userLoginDTO.getEmail();
        String password = userLoginDTO.getPassword();
        User userByEmail = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Login failed - user not found: {}", email);
                    return new AccountNotFoundException(MessageConstants.Auth.INVALID_CREDENTIALS);
                });

        // inactive account will share same logic in the client side because soft delete
        // add more account status in future?
        if (!userByEmail.isActive()) {
            log.warn("Login attempt for inactive account: {}", email);
            throw new InactiveAccountException(MessageConstants.Auth.ACCOUNT_INACTIVE);
        }
        // check password consistency
        if (!passwordEncoder.matches(password, userByEmail.getPassword())) {
            log.warn("Login failed - invalid password for user: {}", email);
            throw new PasswordMismatchException(MessageConstants.Validation.PASSWORD_MISMATCH);
        }

        // start generating token
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userByEmail.getId());
        String token = JwtUtil.createJwt(jwtProperties.getSecretKey(), jwtProperties.getTtl(), claims);
        log.debug("JWT: {}", token);
        log.debug("Generated for user: {}, userId: {}", email, userByEmail.getId());

        log.info("Login successful for user: {}", email);

        return UserLoginResponse.builder()
                .id(userByEmail.getId())
                .username(userByEmail.getEmail())
                .firstName(userByEmail.getFirstName())
                .token(token)
                .build();
    }

    /**
     * Registers a new user or reactivates an existing inactive account.
     *
     * This method:
     * 1. Checks if the email is already in use by an active account
     * 2. Checks if the email belongs to an inactive account that can be reactivated
     * 3. Creates a new user account if needed
     *
     * @param userRegisterDTO DTO containing user registration information
     * @throws UserAlreadyExistsException if email is already in use by an active account
     */
    public void register(UserRegisterDTO userRegisterDTO) {
        String email = userRegisterDTO.getEmail();
        log.info("Processing registration request for email: {}", email);
        String password = userRegisterDTO.getPassword();
        String encode = passwordEncoder.encode(password);

        // check if this user is existed and active
        Optional<User> existingUser = userRepository.findByEmailAndActiveTrue(email);
        if (existingUser.isPresent()) {
            log.warn("Registration failed - email already exists: {}", email);
            throw new UserAlreadyExistsException(MessageConstants.User.EMAIL_EXISTS);
        }

        // check if this user is existed and inactive (which means it got soft-deleted)
        Optional<User> inactiveUser = userRepository.findByEmailAndActiveFalse(email);
        if (inactiveUser.isPresent()) {
            log.info("Reactivating existing inactive account: {}", email);
            User user = inactiveUser.get();
            user.setActive(true);
            user.setPassword(encode);
            user.setFirstName(userRegisterDTO.getFirstName());
            user.setLastName(userRegisterDTO.getLastName());
            userRepository.save(user);
            log.info("Account reactivated successfully: {}", email);
            return;
        }

        User newUser = User.builder()
                .email(email)
                .password(encode)
                .firstName(userRegisterDTO.getFirstName())
                .lastName(userRegisterDTO.getLastName())
                .build();
        userRepository.save(newUser);
        log.info("New user registered successfully: {}", email);
    }

    /**
     * Deactivates a user account (soft delete).
     *
     * @param userId ID of the user account to deactivate
     * @throws AccountNotFoundException if user doesn't exist
     */
    public void deactivateAccount(Long userId) {
        log.info("Processing account deactivation request for user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Deactivation failed - user not found with ID: {}", userId);
                    return new AccountNotFoundException(MessageConstants.User.USER_NOT_FOUND);
                });

        if (!user.isActive()) {
            log.warn("Account already inactive for user ID: {}", userId);
            return;
        }

        user.setActive(false);
        userRepository.save(user);
        log.info("Account successfully deactivated for user ID: {}", userId);
    }

}
