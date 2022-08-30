package com.magadiflo.item.config;

import java.time.Duration;

import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

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

	@LoadBalanced // De forma autmática usará Ribbon para el balanceo de carga, en este caso
					// usando RestTemplate
	@Bean("clienteRest")
	public RestTemplate registrarRestTemplate() {
		return new RestTemplate();
	}

	@Bean
	public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer() {
		// id, se aplicará a cualquier CircuitBreaker que tengamos en la aplicación
		// Por ejemplo, en el ItemControllers método detalle definimos un CircuitBreaker
		// llamado "items"
		return factory -> factory.configureDefault(id -> {
			return new Resilience4JConfigBuilder(id)
					.circuitBreakerConfig(CircuitBreakerConfig.custom()
							.slidingWindowSize(10) // Solamente 10 request
							.failureRateThreshold(50) // Tasa de fallo, por defecto es 50%, tmb lo dejamos en 50%
							.waitDurationInOpenState(Duration.ofSeconds(10L)) // Por defecto es 60s en estado abierto
							.permittedNumberOfCallsInHalfOpenState(5) //Por defecto son 10
							.build())
					.timeLimiterConfig(TimeLimiterConfig.ofDefaults()).build();
		});
	}

}
