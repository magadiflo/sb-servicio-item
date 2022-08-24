package com.magadiflo.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
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
 * 
 * @EnableCircuitBreaker, envuelve a ribbon para agregarle tolerancia a fallos, manejo de latencia y time out 
 * RECORDAR: ribbon, si bien es cierto, no lo tenemos en el pom.xml ya viene incluido dentro del cliente eureka. 
 * Ahora, si tenemos una versión más actual de Spring Boot +2.5 no usará ribbon sino Spring Cloud Load Balancer 
 * y en vez de usar Hystrix usaíamos resilence4j) 
 */

@EnableCircuitBreaker
@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
public class SbServicioItemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SbServicioItemApplication.class, args);
	}

}
