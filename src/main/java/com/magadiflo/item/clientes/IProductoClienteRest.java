package com.magadiflo.item.clientes;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
 * 
 * 
 * ¡ATENCIÓN! Los métodos HTTP definidos en esta interfaz con FEIGN 
 * (@GetMapping, @PostMapping, @PutMapping, @DeleteMapping)
 * deben ser similares a los métodos HTTP definidos en el controlador
 * del cual estamos consumiento, en este caso del servicio
 * productos, es decir tanto las rutas de mapeo como los métodos
 * HTTP definidos en dicho controlador deben ser iguales que en 
 * esta interfaz
 */

@FeignClient(name = "servicio-productos", path = "/productos")
public interface IProductoClienteRest {

	@GetMapping
	public List<Producto> listar();

	@GetMapping("/{id}")
	public Producto detalle(@PathVariable Long id);
	
	@PostMapping
	public Producto crear(@RequestBody Producto producto);
	
	@PutMapping("/{id}")
	public Producto update(@RequestBody Producto producto, @PathVariable Long id);
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id);

}
