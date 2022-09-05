package com.magadiflo.item.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.magadiflo.item.models.Item;
import com.magadiflo.commons.models.entity.Producto;
import com.magadiflo.item.models.service.IItemService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

/*
 * @RefreshScope, permite actualizas las clases anotadas con component, controllers, services, etc.. 
 * que están inyectando algunas dependencias como los environment en esta clase o algunas otras que
 * se anotan con el @Autowired, o con el @Value.
 *  
 * Actualiza, se refresca el contexto y vuelve actualizar los componentes con los cambios reflejados 
 * en tiempo real sin tener que actualizar la aplicación. Todo esto mediante una ruta url (endpoint) 
 * de spring actuator.
 * 
 * management.endpoints.web.exposure.include=*, esto se configura en el bootstrap.properties,
 * el * significa que incluye todos los endpoints de spring actuator (como el refresh, etc)
 * */


@RefreshScope 
@RestController
@RequestMapping("/items")
public class ItemController {
	
	private static final Logger LOG = LoggerFactory.getLogger(ItemController.class);
	

	private final IItemService itemService;
	private final CircuitBreakerFactory cbFactory;
	private final Environment env;
	
	//configuracion.texto, no está en el application.properties, application.yml, bootstrap.properties,
	//sino, por el contrario está definido en el servidor de configuración, más específicamente en el 
	//directorio: c:\config-repo\servicio-item.properties. Esto porque en el bootstrap.properties
	//se hará la llamada al servidor de configuraciones y este está enlazado a ese directorio
	@Value("${configuracion.texto}")
	private String texto;

	public ItemController(@Qualifier("serviceFeign") IItemService itemService, 
			CircuitBreakerFactory cbFactory, Environment env) {
		this.itemService = itemService;
		this.cbFactory = cbFactory;
		this.env = env;
	}

	@GetMapping
	public List<Item> listar(@RequestParam(name = "nombre", required = false) String nombre, @RequestHeader(name = "token-request", required = false) String token) {
		System.out.println(nombre);
		System.out.println(token);
		return this.itemService.findAll();
	}

	@GetMapping(path = "/producto/{id}/cantidad/{cantidad}")
	public Item detalle(@PathVariable Long id, @PathVariable Integer cantidad) {
		//items, nombre que le daremos al cortocircuito
		//Aplicamos resillience de forma programática
		return cbFactory.create("items")
				.run(() -> this.itemService.findById(id, cantidad), e -> metodoAlternatrivo(id, cantidad, e));
	}
	
	@CircuitBreaker(name = "items", fallbackMethod = "metodoAlternatrivo") //Usando anotaciones solo se aplica la configuración via application.properties o application.yml, ya no funcionaría con la clase de configuración que definimos en AppConfig
	@GetMapping(path = "/producto-2/{id}/cantidad/{cantidad}")
	public Item detalle2(@PathVariable Long id, @PathVariable Integer cantidad) {
		return this.itemService.findById(id, cantidad);
	}
	
	/**
	 * Para manejar el método alternativo es suficiente solo manejarlo 
	 * en el CircuitBreaker, ya que si se le pone en el TimeLimiter no funcionará,
	 * bueno, esto siempre y cuando ambas anotaciones trabajen juntas como en este caos.
	 * Vemos que tenemos tanto el CircuitBreaker y TimeLimiter juntos
	 */
	@CircuitBreaker(name = "items",  fallbackMethod = "metodoAlternatrivo2")
	@TimeLimiter(name = "items")
	@GetMapping(path = "/producto-3/{id}/cantidad/{cantidad}")
	public CompletableFuture<Item> detalle3(@PathVariable Long id, @PathVariable Integer cantidad) {
		return CompletableFuture.supplyAsync(() -> this.itemService.findById(id, cantidad));
	}
	
	public Item metodoAlternatrivo(Long id, Integer cantidad, Throwable e) {
		LOG.info(e.getMessage());
		
		Producto producto = new Producto();		
		producto.setId(id);
		producto.setNombre("Cámara Sony");
		producto.setPrecio(250.0);
		
		Item item = new Item();
		item.setCantidad(cantidad);
		item.setProducto(producto);		
		return item;
	}
	
	public CompletableFuture<Item> metodoAlternatrivo2(Long id, Integer cantidad, Throwable e) {
		LOG.info(e.getMessage());
		
		Producto producto = new Producto();		
		producto.setId(id);
		producto.setNombre("Cámara Sony");
		producto.setPrecio(250.0);
		
		Item item = new Item();
		item.setCantidad(cantidad);
		item.setProducto(producto);		
		return CompletableFuture.supplyAsync(() -> item);
	}
	
	/**
	 * ResponseEntity<?> es un objeto de spring que representa el
	 * contenido que vamos a guardar en el cuerpo de la respuesta.
	 * ?, le decimos que puede retornar cualquier tipo de objeto
	 */
	
	/**
	 * obtenerConfig(@Value("${server.port}") String puerto), es otra forma de 
	 * obtener el valor del propertie, pero en este caso directamente en el
	 * método
	 */
	
	@GetMapping(path = "/obtener-config")
	public ResponseEntity<?> obtenerConfig(@Value("${server.port}") String puerto) {
		LOG.info("Texto: {}", this.texto);
		LOG.info("Puerto: {}", puerto);
		
		Map<String, String> json = new HashMap<>();
		json.put("texto", this.texto);
		json.put("puerto", puerto);
		
		if(env.getActiveProfiles().length > 0 && env.getActiveProfiles()[0].equals("dev")) {
			json.put("autor.nombre", env.getProperty("configuracion.autor.nombre"));	
			json.put("autor.email", env.getProperty("configuracion.autor.email"));	
		}
		
		return new ResponseEntity<Map<String, String>>(json, HttpStatus.OK);
	}
	
	@PostMapping(path = "/producto")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Producto crear(@RequestBody Producto producto) {
		return this.itemService.save(producto);		
	}
	
	@PutMapping(path = "/producto/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public Producto editar(@RequestBody Producto producto, @PathVariable Long id) {
		return this.itemService.update(producto, id);
	}
	
	@DeleteMapping(path = "/producto/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void eliminar(@PathVariable Long id) {
		this.itemService.delete(id);
	}

}
