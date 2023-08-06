package com.coedev.customer;

import com.coedev.exception.DuplicateResourceException;
import com.coedev.exception.InvalidEmailException;
import com.coedev.exception.RequestValidationException;
import com.coedev.exception.ResourceNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class) // can be replaced with Autocloseable variable
class CustomerServiceTest {

    @Mock private CustomerDao customerDao;
    private CustomerService underTest;

    @BeforeEach
    void setUp() {
        //AutoCloseable autoCloseable = MockitoAnnotations.openMocks(this); // can be replaced by annotating the class
        underTest = new CustomerService(customerDao);
    }

    @Test
    void getAllCustomers() {
        //GIVEN
        //WHEN
        underTest.getAllCustomers();
        //THEN
        Mockito.verify(customerDao).selectAllCustomers();
    }

    @Test
    void canGetCustomer() {
        //GIVEN
        int id = 10;
        Customer customer = new Customer(id, "alex", "alext@gmail.com", 99);
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        //WHEN
        Customer actualCustomer = underTest.getCustomer(id);

        //THEN
        assertThat(actualCustomer).isEqualTo(customer);
    }

    @Test
    void willThrowWhenGetCustomerReturnsEmptyOptional() {
        //GIVEN
        int id = 10;
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());
        //WHEN

        //THEN
        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer with id [%s] not found".formatted(id)); //.hasMessageContaining() can also be used;
    }

    @Test
    void addCustomer() {
        //GIVEN
        String email = "alex@yahoo.com";

        Mockito.when(customerDao.existPersonWithEmail(email)).thenReturn(false); // say email does not already exist -for testing purpose
        Mockito.when(customerDao.emailValid2(email)).thenReturn(true); // say email is valid - for testing purpose

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "alex", email, 19
        );

        //WHEN
        underTest.addCustomer(request);

        //THEN
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(
                Customer.class
        );
        Mockito.verify(customerDao).insertCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
    }

    @Test
    void willThrowEmailExistsWhileAddingCustomer() {
        //GIVEN
        String email = "alex@yahoo.com";

        Mockito.when(customerDao.existPersonWithEmail(email)).thenReturn(true);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "alex", email, 19
        );

        //WHEN
        assertThatThrownBy(() -> underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already taken");
        //THEN
        Mockito.verify(customerDao, Mockito.never()).insertCustomer(any());

    }

    @Test
    void willThrowEmailNotValid() {
        //GIVEN
        String email = "alex@yahoo.com";

        Mockito.when(customerDao.emailValid2(email)).thenReturn(false);


        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "alex", email, 19
        );

        //WHEN
        assertThatThrownBy(() -> underTest.addCustomer(request))
                .isInstanceOf(InvalidEmailException.class)
                .hasMessage("only faculty email is accepted");
        //THEN
        Mockito.verify(customerDao, Mockito.never()).insertCustomer(any());

    }

    @Test
    void deleteCustomerById() {
        //GIVEN
        int id = 10;

        Mockito.when(customerDao.existsPersonWithId(id)).thenReturn(true);

        //WHEN
        underTest.deleteCustomerById(id);

        //THEN
        Mockito.verify(customerDao).deleteCustomerById(id);
    }

    @Test
    void willThrowWhenDeleteCustomerByIdNotExist() {
        //GIVEN
        int id = 10;

        Mockito.when(customerDao.existsPersonWithId(id)).thenReturn(false);

        //WHEN
        assertThatThrownBy(() -> underTest.deleteCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                        .hasMessage("customer with id [%s] not found".formatted(id));

        //THEN
        Mockito.verify(customerDao, Mockito.never()).deleteCustomerById(id);
    }

    @Test
    void canUpdateAllCustomerProperties() {
        //GIVEN
        int id = 10;
        Customer customer = new Customer(id, "alex", "alext@gmail.com", 99);
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "alexis@gmail.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest("Alexis", newEmail, 101);
        Mockito.when(customerDao.existPersonWithEmail(newEmail)).thenReturn(false);

        //WHEN
        underTest.updateCustomer(id, updateRequest);
        //THEN
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
    }

    @Test
    void canUpdateOnlyCustomerName() {
        //GIVEN
        int id = 10;
        Customer customer = new Customer(id, "alex", "alext@gmail.com", 99);
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest("Alexis", null, null);

        //WHEN
        underTest.updateCustomer(id, updateRequest);
        //THEN
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void canUpdateOnlyCustomerEmail() {
        //GIVEN
        int id = 10;
        Customer customer = new Customer(id, "alex", "alext@gmail.com", 99);
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "alexis@gmail.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(null, newEmail, null);
        Mockito.when(customerDao.existPersonWithEmail(newEmail)).thenReturn(false);
        //WHEN
        underTest.updateCustomer(id, updateRequest);
        //THEN
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(newEmail);
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void canUpdateOnlyCustomerAge() {
        //GIVEN
        int id = 10;
        Customer customer = new Customer(id, "alex", "alext@gmail.com", 99);
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(null, null, 140);

        //WHEN
        underTest.updateCustomer(id, updateRequest);
        //THEN
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
    }

    @Test
    void willThrowWhenUpdateCustomerEmailWhenAlreadyTaken() {
        //GIVEN
        int id = 10;
        Customer customer = new Customer(id, "alex", "alext@gmail.com", 99);
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "alexis@gmail.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(null, newEmail, null);
        Mockito.when(customerDao.existPersonWithEmail(newEmail)).thenReturn(true);
        //WHEN
        assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already taken");
        //THEN

        Mockito.verify(customerDao, Mockito.never()).updateCustomer(any());

    }

    @Test
    void willThrowWhenCustomerUpdateHasNoChange() {
        //GIVEN
        int id = 10;
        Customer customer = new Customer(id, "alex", "alext@gmail.com", 99);
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(customer.getName(), customer.getEmail(), customer.getAge());

        //WHEN
        assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no data changes found");
        //THEN

        Mockito.verify(customerDao, Mockito.never()).updateCustomer(any());

    }


}