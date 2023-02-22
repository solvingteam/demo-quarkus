package com.example.controller.exceptionhandler;

import com.example.dto.ErrorDTO;
import com.example.exception.ApplicationException;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ExeptionMapperProvider implements ExceptionMapper<ApplicationException> {

    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response toResponse(ApplicationException e) {

        return Response.status(e.getHttpStatus()).entity(ErrorDTO.builder().message(e.getMessage()).build().getMessage()).build();
    }
}
