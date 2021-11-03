package com.devsuperior.dscatalog.factories;

import java.time.Instant;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

public class Factory {
	
	public static Product createProduct() {
		Product product = new Product(1L, "Notebook Dell Inspiron do Luis", "Good Notebook", 3000.0, "https://img.com/favicon", Instant.parse("2017-09-27T15:53:42.0Z"));
		product.getCategories().add(new Category(10L, "Notebooks"));
		return product;		
	}
	
	public static ProductDTO createProductDTO() {
		Product product = createProduct();
		return new ProductDTO(product, product.getCategories());
		
	}

}
