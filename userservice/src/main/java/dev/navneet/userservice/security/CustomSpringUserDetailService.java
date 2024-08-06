package dev.navneet.userservice.security;

import dev.navneet.userservice.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dev.navneet.userservice.models.User;

import java.util.Optional;


@Service
public class CustomSpringUserDetailService implements UserDetailsService {
    UserRepository userRepository;

    CustomSpringUserDetailService (UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // todo implement the login service here.
        Optional<User> userOptional = userRepository.findByEmail(username);

        if(userOptional.isEmpty()){
            throw new UsernameNotFoundException(" User doesn't exist!");
        }

        User user = userOptional.get();
        return new CustomSpringUserDetails(user);

    }
}
