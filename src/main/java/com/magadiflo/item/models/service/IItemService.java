package com.magadiflo.item.models.service;

import java.util.List;

import com.magadiflo.item.models.Item;

public interface IItemService {

	List<Item> findAll();

	Item findById(Long id, Integer cantidad);

}
