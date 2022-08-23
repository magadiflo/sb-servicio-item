package com.magadiflo.item.clientes;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.magadiflo.item.models.Producto;

/**
 * @FeignClient, con esto se define que esta interface es un cliente Feign.
 * Dentro del paréntesis colocamos el nombre del microservicio al que nos
 * queremos conectar. En este caso dicho nombre tiene que ser el mismo que se
 * definió en el application.properties del microservicio al que nos queremos
 * conectar. Además, debe llevar la url (localhost, ip de la pc o nombre de la
 * máquina) junto a su puerto definido.
 * 
 * Automáticamente, al estar anotado con @FeignClient pasa a ser un componente
 * manejado por spring, permitiéndonos hacer la Inyección de Dependencia.
 * 
 * ¡OJO! Los métodos definidos aquí, por debajo serán implementados en tiempo de ejecución.
 * 
 * @GetMapping, es el mismo que siempre usamos en los controladores (en los
 * controladores lo usamos para mapear nuestros métodos handler a endPoint, a
 * rutas url), excepto que aquí, en el Feign Clients, se usa para indicar la
 * ruta para CONSUMIR el servicio, el apiRest y obtener los datos del JSON pero
 * convertidos a nuestros objetos (Producto)
 */

@FeignClient(name = "servicio-productos", url = "127.0.0.1:8001/productos")
public interface IProductoClienteRest {

	@GetMapping
	public List<Producto> listar();

	@GetMapping("/{id}")
	public Producto detalle(@PathVariable Long id);

}
