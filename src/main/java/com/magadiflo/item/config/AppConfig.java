package com.magadiflo.item.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

	/**
	 * Por defecto el nombre sería el mismo que el del método, pero podemos
	 * cambiarlo, en este caso por "clienteRest"
	 * 
	 * Este bean lo registramos en el contenedor de Spring. Se usa para hacer
	 * inyección de dependencia, en este caso en la clase ItemServiceImpl vía
	 * constructor
	 */

	@Bean("clienteRest")
	public RestTemplate registrarRestTemplate() {
		return new RestTemplate();
	}

}