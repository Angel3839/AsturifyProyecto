package com.Angelvf3839.tarea3dwesangel;


import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Principal implements CommandLineRunner {

    @Override
    public void run(String... args) {
        System.out.println("Bienvenido a Asturify - Plataforma de Ocio en Asturias");
    }
}
