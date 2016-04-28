package com.vmware.sdugar.rest.exception;

import com.vmware.sdugar.model.User;
import com.vmware.sdugar.rest.UserController;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by sourabhdugar on 4/10/16.
 */
@ControllerAdvice(basePackageClasses = UserController.class)
@SuppressWarnings("unused")
public class RestExceptions {
    @ExceptionHandler(User.UserAlreadyExistsException.class)
    public void handleDuplicateUserName(HttpServletResponse response, Exception e)
            throws IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                e.getMessage());
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public void handleNoAuthenticationObject(HttpServletResponse response, Exception e)
            throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                "unable to authorize user");
    }
}
