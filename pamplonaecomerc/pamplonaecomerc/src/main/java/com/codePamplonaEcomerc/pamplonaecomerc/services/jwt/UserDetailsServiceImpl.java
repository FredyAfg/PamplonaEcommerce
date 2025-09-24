package com.codePamplonaEcomerc.pamplonaecomerc.services.jwt;


import com.codePamplonaEcomerc.pamplonaecomerc.entity.User;
import com.codePamplonaEcomerc.pamplonaecomerc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired

    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser= userRepository.findFirtsByEmail(username);

        if(optionalUser.isEmpty()) throw new UsernameNotFoundException("No se encontro el usuario", null);
        return new org.springframework.security.core.userdetails.User(optionalUser.get().getEmail(),optionalUser.get().getPassword(),
                new ArrayList<>());



    }
}
