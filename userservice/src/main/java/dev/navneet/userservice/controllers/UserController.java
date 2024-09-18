package dev.navneet.userservice.controllers;

import dev.navneet.userservice.dtos.SetUserRolesRequestDto;
import dev.navneet.userservice.dtos.UserDto;
import dev.navneet.userservice.models.User;
import dev.navneet.userservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/")
     public List<UserDto> getAllUsers(){
         return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity <UserDto>  getUserDetails(@PathVariable("id") Long userId){
        UserDto userDto = userService.getUserDetails(userId);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping("/{id}/roles")
    public ResponseEntity <UserDto>  setUserRoles(@PathVariable("id") Long userId,
                                                  @RequestBody SetUserRolesRequestDto request){
        UserDto userDto = userService.setUserRoles(userId, request.getRoleIds());
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}
