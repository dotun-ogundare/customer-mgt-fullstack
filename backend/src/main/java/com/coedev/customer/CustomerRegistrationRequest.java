package com.coedev.customer;

public record CustomerRegistrationRequest(
        String name,
        String email,
        Integer age
) {
}
