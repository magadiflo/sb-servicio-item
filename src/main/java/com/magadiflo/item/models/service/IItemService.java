package com.magadiflo.item.models.service;

import java.util.List;

import com.magadiflo.item.models.Item;
import com.magadiflo.item.models.Producto;

public interface IItemService {

	List<Item> findAll();

	Item findById(Long id, Integer cantidad);
	
	Producto save(Producto producto);
	
	Producto update(Producto producto, Long id);
	
	void delete(Long id);
	
}
