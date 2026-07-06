package com.taskforge.exception;

/**
 * CUSTOM EXCEPTION — thrown when a requested resource doesn't exist.
 *
 * Why a custom exception instead of just returning null?
 * 1. It's SELF-DOCUMENTING — "ResourceNotFoundException" tells you exactly what went wrong
 * 2. It can be caught by a GLOBAL EXCEPTION HANDLER (Module 4) and
 *    converted to a proper HTTP 404 response automatically
 * 3. It separates "not found" from other types of errors
 *
 * RuntimeException vs Exception:
 *   RuntimeException = UNCHECKED — you don't need try/catch (Spring handles it)
 *   Exception        = CHECKED   — forces try/catch everywhere (annoying)
 *
 * In Spring Boot, we always extend RuntimeException for custom exceptions.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
        // super() calls the parent RuntimeException constructor
        // which stores the error message — retrievable via getMessage()
    }
}
