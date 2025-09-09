package com.restaurant.shared.infrastructure.api;

import org.apache.kafka.common.requests.ApiError;
import  restaurant.order.menu.domain.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidPlateNameException.class)
    public ResponseEntity<ApiError> handleInvalidPlateName(InvalidPlateNameException ex) {
        ApiError error = new ApiError("InvalidPlateName", ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(TooManyIngredientsException.class)
    public ResponseEntity<ApiError> handleTooManyIngredients(TooManyIngredientsException ex) {
        ApiError error = new ApiError("TooManyIngredients", ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(IngredientNotFoundException.class)
    public ResponseEntity<ApiError> handleIngredientNotFound(IngredientNotFoundException ex) {
        ApiError error = new ApiError("IngredientNotFound", ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(Exception.class) // fallback for unexpected errors
    public ResponseEntity<ApiError> handleGenericException(Exception ex) {
        ApiError error = new ApiError("InternalServerError", "Unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
