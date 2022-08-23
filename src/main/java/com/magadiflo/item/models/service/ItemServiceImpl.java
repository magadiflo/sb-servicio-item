package com.magadiflo.item.models.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.magadiflo.item.models.Item;

@Service
public class ItemServiceImpl implements IItemService {

	private final RestTemplate clienteRest;

	public ItemServiceImpl(RestTemplate clienteRest) {
		this.clienteRest = clienteRest;
	}

	@Override
	public List<Item> findAll() {
		return null;
	}

	@Override
	public Item findById(Long id, Integer cantidad) {
		return null;
	}

}
