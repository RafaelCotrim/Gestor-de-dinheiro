package est.money.mannager.api.controllers;

import est.money.mannager.api.dtos.UserDto;
import est.money.mannager.api.dtos.UserForLogin;
import est.money.mannager.api.dtos.UserForRegister;
import est.money.mannager.api.services.AuthService;
import est.money.mannager.api.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Operation(summary = "Log in user and return basic user data")
    @PostMapping("/login")
    public UserDto login(@RequestBody UserForLogin user){
        return authService.login(user);
    }

    @Operation(summary = "Register user and return basic user data")
    @PostMapping("/register")
    public UserDto register(@RequestBody UserForRegister ufl){
        return authService.register(ufl);
    }

}
