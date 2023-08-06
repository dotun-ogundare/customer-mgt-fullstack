package com.coedev.customer;

public record CustomerUpdateRequest(
        String name,
        String email,
        Integer age
) {
}
