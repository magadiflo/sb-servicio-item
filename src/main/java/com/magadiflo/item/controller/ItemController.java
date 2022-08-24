package com.magadiflo.item.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.magadiflo.item.models.Item;
import com.magadiflo.item.models.Producto;
import com.magadiflo.item.models.service.IItemService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@RequestMapping("/items")
public class ItemController {

	private final IItemService itemService;

	public ItemController(@Qualifier("serviceFeign") IItemService itemService) {
		this.itemService = itemService;
	}

	@GetMapping
	public List<Item> listar() {
		return this.itemService.findAll();
	}

	/**
	 * @HystrixCommand, en caso falle la comunicación, le damos un camino alternativo llamando a otro método.
	 * De esa manera evitamos errores en cascada que se puedan producir en nuestro ecosistema de microservicios.
	 */
	@HystrixCommand(fallbackMethod = "metodoAlternatrivo")
	@GetMapping(path = "/producto/{id}/cantidad/{cantidad}")
	public Item detalle(@PathVariable Long id, @PathVariable Integer cantidad) {
		return this.itemService.findById(id, cantidad);
	}
	
	public Item metodoAlternatrivo(Long id, Integer cantidad) {
		Producto producto = new Producto();		
		producto.setId(id);
		producto.setNombre("Cámara Sony");
		producto.setPrecio(250.0);
		
		Item item = new Item();
		item.setCantidad(cantidad);
		item.setProducto(producto);		
		return item;
	}

}
