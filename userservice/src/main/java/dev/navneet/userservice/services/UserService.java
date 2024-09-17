package dev.navneet.userservice.services;

import dev.navneet.userservice.dtos.UserDto;
import dev.navneet.userservice.models.Role;
import dev.navneet.userservice.models.User;
import dev.navneet.userservice.repositories.RoleRepository;
import dev.navneet.userservice.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public List <UserDto> getAllUsers() {
        List<User> allUsers= userRepository.findAll();
        List<UserDto> usersDto = new ArrayList<>();
        for(User user: allUsers){
            usersDto.add(UserDto.from(user));
        }
        return usersDto;
    }

    public UserDto getUserDetails(Long userId) {
        System.out.println("called from the product service");
//        return new UserDto();
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            return new UserDto();
        }
        return UserDto.from(userOptional.get());
    }

    public UserDto setUserRoles(Long userId, List<Long> roleIds) {
        Optional<User> userOptional = userRepository.findById(userId);
        List<Role> roles = roleRepository.findAllByIdIn(roleIds);

        if (userOptional.isEmpty()) {
            return null;
        }

        User user = userOptional.get();
        user.setRoles(Set.copyOf(roles));

        User savedUser = userRepository.save(user);

        return UserDto.from(savedUser);
    }
}