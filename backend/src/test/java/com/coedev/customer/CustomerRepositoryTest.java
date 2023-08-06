package com.coedev.customer;

import com.coedev.AbstractTestContainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest //loads essential beans
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestContainers {

    @Autowired
    private CustomerRepository underTest;

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp(){
        //underTest.deleteAll(); // deletes the data added through commandRunner in main, during test
        System.out.println(applicationContext.getBeanDefinitionCount());
    }

    @Test
    void existsCustomerByEmail() {
        //GIVEN
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.save(customer);

        //WHEN
        var actualCustomer = underTest.existsCustomerByEmail(email);

        //THEN
        assertThat(actualCustomer).isTrue();

    }

    @Test
    void existsCustomerByEmailFailsWhenEmailNotPresent() {
        //GIVEN
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        //WHEN
        var actualCustomer = underTest.existsCustomerByEmail(email);

        //THEN
        assertThat(actualCustomer).isFalse();

    }

    @Test
    void existsCustomerById() {
        //GIVEN
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.save(customer);

        int id = underTest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                //.map(Customer :: getId)
                .findFirst()
                .orElseThrow();
        //WHEN
        var actualCustomer = underTest.existsCustomerById(id);

        //THEN
        assertThat(actualCustomer).isTrue();

    }

    @Test
    void existsCustomerByIdFailsWhenIdNotPresent() {
        //GIVEN
        int id = -1;
        //WHEN
        var actualCustomer = underTest.existsCustomerById(id);

        //THEN
        assertThat(actualCustomer).isFalse();

    }
}