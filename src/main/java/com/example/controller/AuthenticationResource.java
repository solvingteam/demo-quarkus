package com.example.controller;

import com.example.dto.JWTTokeResponse;
import com.example.dto.SigninRequestDTO;
import com.example.exception.ApplicationException;
import com.example.model.User;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.build.Jwt;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Path("/auth")
@RequiredArgsConstructor
public class AuthenticationResource {


    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;

    @POST
    @Path("signin")
    public JWTTokeResponse signin(SigninRequestDTO signinRequestDTO) {
        User user = User.findByUsername(signinRequestDTO.getUsername()).su;
        if (user != null) {
            if (BcryptUtil.matches(signinRequestDTO.getPassword(), user.getPassword())) {
                String token = Jwt
                        .issuer(issuer)
                        .groups(user.getRole().name())
                        .subject(user.getUsername())
                        .expiresIn(Duration.of(1, ChronoUnit.HOURS))
                        .sign();
                return JWTTokeResponse.builder().token(token).build();
            }

        }
        throw new ApplicationException(401, "Not authenticated");
    }


}
