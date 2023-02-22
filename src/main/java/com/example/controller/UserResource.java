package com.example.controller;

import com.example.dto.SignupRequestDTO;
import com.example.model.User;
import com.example.model.enumerations.Role;
import com.example.service.AuthenticationFacade;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Path("/users")
@Slf4j
@RequiredArgsConstructor
public class UserResource {


    @Inject
    AuthenticationFacade authenticationFacade;

    @GET
    @RolesAllowed("ADMIN")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<PanacheEntityBase>> list() {
        return User.listAll();
    }

    @POST
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Uni<Response> signup(SignupRequestDTO signupRequestDTO) {
        User user = new User();
        user.setUsername(signupRequestDTO.getUsername());
        user.setPassword(BcryptUtil.bcryptHash(signupRequestDTO.getPassword()));
        user.setFirstname(signupRequestDTO.getFirstname());
        user.setLastname(signupRequestDTO.getLastname());
        user.setRole(Role.valueOf(signupRequestDTO.getRole()));

        return Panache.<User>withTransaction(user::persist)
                .onItem().transform(inserted -> Response.ok().build());
    }

    @GET
    @Path("me")
    @RolesAllowed("ADMIN")
    @Produces(MediaType.APPLICATION_JSON)
    public User getMe(@Context SecurityContext ctx) {
        return authenticationFacade.getCurrentUser();
    }
}
