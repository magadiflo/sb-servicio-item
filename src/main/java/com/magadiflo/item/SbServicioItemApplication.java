package com.magadiflo.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @EnableFeignClients, importante para habilitar los clientes Feign que se
 * tengan implementados en el proyecto. Además, nos permite inyectar estos
 * clientes en nuestros controladores u otros componentes de Spring, por
 * ejemplo: en una clase service habilita la inyección de dependencia
 * (@Autowire)
 *  * 
 * @EnableEurekaClient, con solo tener en el pom.xml la dependencia de EurekaCliente ya está habilitado
 * pero sería mejor si lo habilitamos de forma explícita con esta anotación
 * 
 * ATENCIÓN: Se quitó ribbon, ya que esa dependencia se maneja de forma automática. Es decir, 
 * ya viene implícito en la dependencia de Eureka
 */

@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
public class SbServicioItemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SbServicioItemApplication.class, args);
	}

}
