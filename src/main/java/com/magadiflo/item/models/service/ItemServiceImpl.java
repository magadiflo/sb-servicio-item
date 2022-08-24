package com.magadiflo.item.models.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.magadiflo.item.models.Item;
import com.magadiflo.item.models.Producto;

@Service("serviceRestTemplate")
public class ItemServiceImpl implements IItemService {

	private final RestTemplate clienteRest;

	public ItemServiceImpl(RestTemplate clienteRest) {
		this.clienteRest = clienteRest;
	}

	@Override
	public List<Item> findAll() {
		Producto[] productosArray = this.clienteRest.getForObject("http://servicio-productos/productos", Producto[].class);
		List<Producto> productos = Arrays.asList(productosArray);
		return productos.stream().map(producto -> new Item(producto, 1)).collect(Collectors.toList());
	}

	@Override
	public Item findById(Long id, Integer cantidad) {
		Map<String, String> pathVariables = new HashMap<String, String>();
		pathVariables.put("id", id.toString());

		Producto prod = this.clienteRest.getForObject("http://servicio-productos/productos/{id}", Producto.class, pathVariables);
		return new Item(prod, cantidad);
	}

}
