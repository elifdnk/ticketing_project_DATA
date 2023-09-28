package com.cydeo;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication  // This includes @Configurations
public class TicketingProjectDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketingProjectDataApplication.class, args);
    }

    // Write a method which return the obj that you trying to add in the container
    //                 annotate this method with @Bean
   @Bean
    public ModelMapper mapper(){
        return new ModelMapper();
    }

}
