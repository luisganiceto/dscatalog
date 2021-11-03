package com.devsuperior.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(Pageable pageable) {
		Page<Product> list = repository.findAll(pageable);
		return list.map(x -> new ProductDTO(x));
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> optional = repository.findById(id);
		Product product = optional.orElseThrow(() -> new ResourceNotFoundException("Entity not Found"));
		return new ProductDTO(product, product.getCategories());
	}

	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product product = new Product();
		copyDtoToProduct(dto, product);
		product = repository.save(product);
		return new ProductDTO(product, product.getCategories());
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			Product product = repository.getOne(id);
			copyDtoToProduct(dto, product);
			product = repository.save(product);
			return new ProductDTO(product);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id " + id + " Not Found");
		}
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id " + id + " Not Found");
		} catch (DataIntegrityViolationException e) {
			throw new DataBaseException("Integrity violation");
		}

	}

	private void copyDtoToProduct(ProductDTO dto, Product product) {
		
		product.setName(dto.getName());
		product.setDescription(dto.getDescription());
		product.setDate(product.getDate());
		product.setImgUrl(dto.getImgUrl());
		product.setPrice(dto.getPrice());
		
		product.getCategories().clear();
		for (CategoryDTO categoryDto : dto.getCategories()) {
			Category category = categoryRepository.getOne(categoryDto.getId());
			product.getCategories().add(category);
		} 
		
	}
	
}
