package com.maimai.billingcalculationengine.controller;

import com.maimai.billingcalculationengine.common.annotations.TrackExecution;
import com.maimai.billingcalculationengine.common.constants.MessageConstants;
import com.maimai.billingcalculationengine.common.enums.Layer;
import com.maimai.billingcalculationengine.common.result.Result;
import com.maimai.billingcalculationengine.model.dto.UserRegisterDTO;
import com.maimai.billingcalculationengine.model.dto.UserLoginDTO;
import com.maimai.billingcalculationengine.model.response.UserLoginResponse;
import com.maimai.billingcalculationengine.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "User Data", description = "APIs user data")
public class UserController {
    @Resource
    private UserService userService;

    @TrackExecution(Layer.CONTROLLER)
    @PostMapping("/login")
    public Result<UserLoginResponse> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        UserLoginResponse loginUser = userService.login(userLoginDTO);
        return Result.success(loginUser, MessageConstants.Auth.LOGIN_SUCCESS);
    }

    @PostMapping("/register")
    public Result<String> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        userService.register(userRegisterDTO);
        return Result.successWithoutData(MessageConstants.User.ACCOUNT_CREATED);
    }

}
