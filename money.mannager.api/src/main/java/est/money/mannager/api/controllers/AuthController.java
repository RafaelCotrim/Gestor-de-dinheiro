package est.money.mannager.api.controllers;

import est.money.mannager.api.dtos.UserDTO;
import est.money.mannager.api.dtos.UserForLogin;
import est.money.mannager.api.dtos.UserForRegister;
import est.money.mannager.api.services.AuthService;
import est.money.mannager.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public UserDTO login(@RequestBody UserForLogin user){
        return authService.login(user);
    }

    @PostMapping("/register")
    public UserDTO register(@RequestBody UserForRegister ufl){
        return authService.register(ufl);
    }

}
