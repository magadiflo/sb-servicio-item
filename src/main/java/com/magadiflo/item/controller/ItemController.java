package com.magadiflo.item.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.magadiflo.item.models.Item;
import com.magadiflo.item.models.service.IItemService;

@RestController
@RequestMapping("/items")
public class ItemController {

	private final IItemService itemService;

	public ItemController(@Qualifier("serviceRestTemplate") IItemService itemService) {
		this.itemService = itemService;
	}

	@GetMapping
	public List<Item> listar() {
		return this.itemService.findAll();
	}

	@GetMapping(path = "/producto/{id}/cantidad/{cantidad}")
	public Item detalle(@PathVariable Long id, @PathVariable Integer cantidad) {
		return this.itemService.findById(id, cantidad);
	}

}
