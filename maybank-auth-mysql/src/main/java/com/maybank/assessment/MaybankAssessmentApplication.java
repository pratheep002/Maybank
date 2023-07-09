package com.maybank.assessment;

import com.maybank.assessment.entity.Customer;
import com.maybank.assessment.repository.UserRepository;
import com.maybank.assessment.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class MaybankAssessmentApplication {
    @Autowired
    private UserRepository repository;

    private final TransactionService transactionService;

    public MaybankAssessmentApplication(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostConstruct
    public void initCustomers() {
        List<Customer> customers = Stream.of(
                new Customer(101, "pratheep", "password", "pratheep@gmail.com"),
                new Customer(102, "222", "pwd1", "user1@gmail.com"),
                new Customer(103, "333", "pwd2", "user2@gmail.com"),
                new Customer(104, "444", "pwd3", "user3@gmail.com")
        ).collect(Collectors.toList());
        repository.saveAll(customers);
    }

    // Cross origin configuration
    //  Followed: https://spring.io/guides/gs/rest-service-cors/
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {

                registry.addMapping("/*").allowedHeaders("*").allowedOrigins("*")
                        .allowedMethods("*").allowCredentials(true);
            }
        };
    }

    @Bean
    public CommandLineRunner init() {
        return args -> {
            transactionService.processFileAndStoreTransactions();
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(MaybankAssessmentApplication.class, args);
    }

}
