package com.example.service;

import com.example.model.User;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@Slf4j
@RequestScoped
public class AuthenticationFacade {

    
    @Inject
    JsonWebToken jwt;
    
    public User getCurrentUser(){
        return User.findByUsername(jwt.getSubject()).onItem().transform(panacheEntityBase -> panacheEntityBase.);
    }

}
