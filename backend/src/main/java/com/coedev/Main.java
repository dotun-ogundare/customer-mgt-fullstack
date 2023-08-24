package com.coedev;

import com.coedev.customer.Customer;
import com.coedev.customer.CustomerRepository;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Random;


@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext =
                SpringApplication.run(Main.class, args);

       // printBeans(applicationContext);
        //comment
     }

     public Foo getFoo(){
        return new Foo("bar");
     }
     record Foo(String name){}

     private static void printBeans(ConfigurableApplicationContext ctx){
         String[] beanDefinitionNames = ctx.getBeanDefinitionNames();
         for(String beanDefinitionName : beanDefinitionNames){
             System.out.println(beanDefinitionName);
         }
     }

     @Bean
     CommandLineRunner runner(CustomerRepository customerRepository){
        return args -> {
            var faker = new Faker();
            Random random = new Random();
            Customer customer = new Customer(
                    faker.name().fullName(),
                    faker.internet().safeEmailAddress(),
                    random.nextInt(16, 99)
            );
            //Customer customer = new Customer("Alex", "alexis@gmail.com", 21);
            //Customer jamila = new Customer("Jamila", "jammey@gmail.com", 26);
            //List<Customer> customers = List.of(alex, jamila);
            customerRepository.save(customer);
        };
     }

}
