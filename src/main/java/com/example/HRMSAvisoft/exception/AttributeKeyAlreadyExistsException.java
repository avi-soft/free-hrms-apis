package com.example.HRMSAvisoft.exception;

public class AttributeKeyAlreadyExistsException extends Exception {
    public AttributeKeyAlreadyExistsException(String attributeKey){
        super("Key "+ attributeKey+ " already exists");
    }
}
