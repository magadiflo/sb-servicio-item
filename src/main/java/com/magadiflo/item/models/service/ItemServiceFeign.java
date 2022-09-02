package com.magadiflo.item.models.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.magadiflo.item.clientes.IProductoClienteRest;
import com.magadiflo.item.models.Item;
import com.magadiflo.item.models.Producto;

@Service("serviceFeign")
public class ItemServiceFeign implements IItemService {

	private IProductoClienteRest clienteFeign;

	public ItemServiceFeign(IProductoClienteRest clienteFeign) {
		this.clienteFeign = clienteFeign;
	}

	@Override
	public List<Item> findAll() {
		return this.clienteFeign.listar().stream().map(producto -> new Item(producto, 1)).collect(Collectors.toList());
	}

	@Override
	public Item findById(Long id, Integer cantidad) {
		Producto producto = this.clienteFeign.detalle(id);
		return new Item(producto, cantidad);
	}

	@Override
	public Producto save(Producto producto) {
		return this.clienteFeign.crear(producto);
	}

	@Override
	public Producto update(Producto producto, Long id) {
		return this.clienteFeign.update(producto, id);
	}

	@Override
	public void delete(Long id) {
		this.clienteFeign.delete(id);		
	}

}
