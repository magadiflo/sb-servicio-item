package com.magadiflo.item.models.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.magadiflo.item.models.Item;
import com.magadiflo.commons.models.entity.Producto;

@Service("serviceRestTemplate")
public class ItemServiceImpl implements IItemService {

	private final RestTemplate clienteRest;

	public ItemServiceImpl(RestTemplate clienteRest) {
		this.clienteRest = clienteRest;
	}

	@Override
	public List<Item> findAll() {
		Producto[] productosArray = this.clienteRest.getForObject("http://servicio-productos/productos",
				Producto[].class);
		List<Producto> productos = Arrays.asList(productosArray);
		return productos.stream().map(producto -> new Item(producto, 1)).collect(Collectors.toList());
	}

	@Override
	public Item findById(Long id, Integer cantidad) {
		Map<String, String> pathVariables = new HashMap<String, String>();
		pathVariables.put("id", id.toString());

		Producto prod = this.clienteRest.getForObject("http://servicio-productos/productos/{id}", Producto.class,
				pathVariables);
		return new Item(prod, cantidad);
	}

	@Override
	public Producto save(Producto producto) {
		// ** Primera forma
		// return this.clienteRest.postForObject("http://servicio-productos/productos", producto, Producto.class);

		// ** Segunda forma
		HttpEntity<Producto> body = new HttpEntity<Producto>(producto);
		ResponseEntity<Producto> response = this.clienteRest.exchange("http://servicio-productos/productos", HttpMethod.POST, body, Producto.class);
		Producto productoResponse = response.getBody();
		return productoResponse;
	}

	@Override
	public Producto update(Producto producto, Long id) {
		// ** Primera forma
		//this.restTemplate.put("http://servicio-productos/productos/{id}", producto, id);
		
		// ** Segunda forma
		Map<String, String> pathVariables = new HashMap<String, String>();
		pathVariables.put("id", id.toString());
		
		HttpEntity<Producto> body = new HttpEntity<>(producto);
		ResponseEntity<Producto> response = this.clienteRest.exchange("http://servicio-productos/productos/{id}", HttpMethod.PUT, body, Producto.class, pathVariables);
		return response.getBody();
	}

	@Override
	public void delete(Long id) {
		Map<String, String> pathVariables = new HashMap<String, String>();
		pathVariables.put("id", id.toString());
		this.clienteRest.delete("http://servicio-productos/productos/{id}", pathVariables);		
	}

}
