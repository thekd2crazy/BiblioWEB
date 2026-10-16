package be.ecam.biblioweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BiblioWebApplication {

    public static void main(String[] args) {

        System.out.println("Le BG steve " +
                " Le meilleur de sa generation");

        SpringApplication.run(BiblioWebApplication.class, args);

    }


}
