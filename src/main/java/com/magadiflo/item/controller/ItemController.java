package com.magadiflo.item.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.magadiflo.item.models.Item;
import com.magadiflo.item.models.Producto;
import com.magadiflo.item.models.service.IItemService;

@RestController
@RequestMapping("/items")
public class ItemController {
	
	private static final Logger LOG = LoggerFactory.getLogger(ItemController.class);

	private final IItemService itemService;
	private final CircuitBreakerFactory cbFactory;

	public ItemController(@Qualifier("serviceFeign") IItemService itemService, CircuitBreakerFactory cbFactory) {
		this.itemService = itemService;
		this.cbFactory = cbFactory;
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
				.run(() -> this.itemService.findById(id, cantidad)/*, e -> metodoAlternatrivo(id, cantidad, e)*/);
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

}
