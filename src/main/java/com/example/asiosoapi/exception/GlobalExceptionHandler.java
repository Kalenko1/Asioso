package com.example.asiosoapi.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.UUID;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private String logError(Exception ex, String contextMessage) {
        String errorId = UUID.randomUUID().toString(); // Unique error ID
        logger.error("[{}] {} - {}", errorId, contextMessage, ex.getMessage(), ex);
        return errorId;
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        String errorId = logError(ex, "General error occurred");
        model.addAttribute("errorMessage", "An unexpected error occurred.");
        model.addAttribute("errorId", errorId);
        return "error";
    }

    @ExceptionHandler(RestClientException.class)
    public String handleRestClientError(RestClientException ex, Model model) {
        String errorId = logError(ex, "API communication failed");
        model.addAttribute("errorMessage", "Failed to fetch product data.");
        model.addAttribute("errorId", errorId);
        return "error";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFound(NoHandlerFoundException ex, Model model) {
        String errorId = logError(ex, "404 Not Found: " + ex.getRequestURL());
        model.addAttribute("errorMessage", "Page not found.");
        model.addAttribute("errorId", errorId);
        return "error";
    }

    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException ex, Model model) {
        String errorId = logError(ex, "RuntimeException occurred");
        model.addAttribute("errorMessage", "An unexpected error occurred: " + ex.getMessage());
        model.addAttribute("errorId", errorId);
        return "error";
    }
}
