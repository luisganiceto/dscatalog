package com.devsuperior.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.factories.Factory;

@DataJpaTest
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository repository;
	
	@Test
	public void saveShouldSaveObjectAndAitoincrementWhenIdIsNull() {
		Product product = Factory.createProduct();
		product.setId(null);
		
		product = repository.save(product);
		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(26, product.getId());
	}
	
	@Test
	public void findByIdShouldFindObjectWhenIdExists() {
		
		Optional<Product> result = repository.findById(1L);
		
		Assertions.assertTrue(result.isPresent());
	}
	
	@Test
	public void findByIdShouldNotFindObjectWhenIdNotExists() {
		
		Optional<Product> result = repository.findById(100L);
		
		Assertions.assertTrue(result.isEmpty());
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		
		repository.deleteById(1L);
		Optional<Product> result = repository.findById(1L);
		
		Assertions.assertFalse(result.isPresent());
		
	}
	
	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			repository.deleteById(100L);
		});
	}
	
}
