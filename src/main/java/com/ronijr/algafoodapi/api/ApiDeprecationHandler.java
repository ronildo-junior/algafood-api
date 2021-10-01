package com.ronijr.algafoodapi.api;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.time.OffsetDateTime;

@Component
public class ApiDeprecationHandler implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getRequestURI().startsWith("/v1/")) {
            response.addHeader("X-AlgaFood-Deprecated", "Deprecated API version. Will cease to exist in 2035-01-01.");
            if (OffsetDateTime.now().getYear() >= 2035) {
                response.setStatus(HttpStatus.GONE.value());
                return false;
            }
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}