// UsuarioExistenteException.java
package com.gian.springboot.app.panaderia.panaderiabackend.exceptions;

public class UsuarioExistenteException extends RuntimeException {
    public UsuarioExistenteException(String message) {
        super(message);
    }
}