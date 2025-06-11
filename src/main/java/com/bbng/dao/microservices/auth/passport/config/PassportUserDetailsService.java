package com.bbng.dao.microservices.auth.passport.config;


import com.bbng.dao.microservices.auth.passport.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PassportUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

//       UserEntity user = userRepository.findByUsernameOrEmail(username, username).orElseThrow(() -> new UsernameNotFoundException(String.format("No such user with %s found", username)));

//        return new PassportUserDetails(user);
        var user = userRepository.findByUsernameOrEmail(username, username);


        return user.map(PassportUserDetails::new).orElseThrow(() -> new UsernameNotFoundException(String.format("No such user with %s found", username)));
    }
}
