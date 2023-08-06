package com.coedev.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class CustomerJpaDataAccessServiceTest {

    private CustomerJpaDataAccessService underTest;
    private AutoCloseable autoCloseable;
   @Mock private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJpaDataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception{
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        //GIVEN

        //WHEN
        underTest.selectAllCustomers();
        //THEN
        Mockito.verify(customerRepository)
                .findAll();
    }

    @Test
    void selectCustomerById() {
        //GIVEN
        int id = 1;
        //WHEN
        underTest.selectCustomerById(id);
        //THEN
        Mockito.verify(customerRepository).findById(id);
    }

    @Test
    void insertCustomer() {
        //GIVEN
        Customer customer = new Customer( 1,"Ali", "ali@gmail.com", 19 );
        //WHEN
        underTest.insertCustomer(customer);
        //THEN
        Mockito.verify(customerRepository).save(customer);
    }

    @Test
    void existPersonWithEmail() {
        //GIVEN
        String email = "goo1@yahoo.com";
        //WHEN
        underTest.existPersonWithEmail(email);
        //THEN
        Mockito.verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void existsPersonWithId() {
        //GIVEN
        int id = 1;

        //WHEN
        underTest.existsPersonWithId(id);
        //THEN
        Mockito.verify(customerRepository).existsCustomerById(id);
    }

    @Test
    void deleteCustomerById() {
        //GIVEN
        int id = 1;

        //WHEN
        underTest.deleteCustomerById(id);

        //THEN
        Mockito.verify(customerRepository).deleteById(id);
    }

    @Test
    void updateCustomer() {
        //GIVEN
        Customer customer = new Customer(
                1, "Ali", "ali@gmail.com", 22
        );

        //WHEN
        underTest.updateCustomer(customer);

        //THEN
        Mockito.verify(customerRepository).save(customer); // this confirms that thesame customer passed into the method updateCustomer() above is same that was saved
    }
}