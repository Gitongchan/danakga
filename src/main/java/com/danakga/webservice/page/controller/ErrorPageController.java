package com.danakga.webservice.page.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorPageController implements ErrorController {

    private final String VIEW_PATH = "/errors/";

    @RequestMapping("/error")
    public String error(HttpServletRequest request) {

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if(status != null) {
            int statusCode = Integer.valueOf(status.toString());
            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return VIEW_PATH + "404";
            }
            if (statusCode == HttpStatus.FORBIDDEN.value()) {
                return VIEW_PATH + "403";
            }
            if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return VIEW_PATH + "500";
            }
            if(statusCode == HttpStatus.BAD_REQUEST.value()) {
                return VIEW_PATH + "400";
            }
        }
        return "error";
    }

}
