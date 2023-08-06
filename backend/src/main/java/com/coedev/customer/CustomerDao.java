package com.coedev.customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerById(Integer id);
    void insertCustomer(Customer customer);
    boolean existPersonWithEmail(String email);
    boolean existsPersonWithId(Integer customerId);
    void deleteCustomerById(Integer customerId);
    void updateCustomer(Customer update);

    List<String> facultyDomains = List.of("@uns.ac.rs",
            "@yahoo.com", "@gmail.com"
    );
    default boolean emailValid(String email){
        int atIndex = email.indexOf('@');
        //Optional<Integer> atIndex = Optional.of(email.indexOf('@'));
        String domain = email.substring(atIndex);
       return facultyDomains.stream()
                .map(String::toLowerCase)
                .anyMatch(domain.toLowerCase()::equals);

    }

    default boolean emailValid2(String email) {
        Optional<String> optionalEmail = Optional.ofNullable(email);
        if(optionalEmail.isPresent()) {
            int atIndex = email.indexOf('@');
            String domain = email.substring(atIndex);
            return facultyDomains.stream()
                    .map(String::toLowerCase)
                    .anyMatch(domain.toLowerCase()::equals);
        }
        return false;
    }
}
