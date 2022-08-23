package com.magadiflo.item.models;

import java.util.Date;

/**
 * Replicamos los mismos datos de la clase producto del microservicio productos,
 * con la diferencia de que aquí nuestra clase Producto del microservicio item
 * no es un Entity, sino más bien será una clase que permitirá obtener los datos
 * que nos devuelva el microservicios productos.
 *
 * Esto con la finalidad de obtener mediante API REST la estructura JSON de
 * producto
 */

public class Producto {

	private Long id;
	private String nombre;
	private Double precio;
	private Date createAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Producto [id=");
		builder.append(id);
		builder.append(", nombre=");
		builder.append(nombre);
		builder.append(", precio=");
		builder.append(precio);
		builder.append(", createAt=");
		builder.append(createAt);
		builder.append("]");
		return builder.toString();
	}

}
