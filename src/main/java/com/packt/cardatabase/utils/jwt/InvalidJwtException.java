package com.packt.cardatabase.utils.jwt;

public class InvalidJwtException extends Exception {
    public InvalidJwtException(String message) {
        super(message);
    }
}
