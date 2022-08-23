package com.magadiflo.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @EnableFeignClients, importante para habilitar los clientes Feign que se
 * tengan implementados en el proyecto. Además, nos permite inyectar estos
 * clientes en nuestros controladores u otros componentes de Spring, por
 * ejemplo: en una clase service habilita la inyección de dependencia
 * (@Autowire)
 * 
 */

@EnableFeignClients
@SpringBootApplication
public class SbServicioItemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SbServicioItemApplication.class, args);
	}

}
