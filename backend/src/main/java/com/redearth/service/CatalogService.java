package com.redearth.service;

import com.redearth.mongo.doc.ProductDoc;
import com.redearth.mongo.repo.ProductRepository;
import com.redearth.web.error.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogService {
  private final ProductRepository products;

  public CatalogService(ProductRepository products) {
    this.products = products;
  }

  public List<ProductDoc> list(String q, String tag) {
    if (tag != null && !tag.isBlank()) return products.findByTagsContainingIgnoreCase(tag.trim());
    if (q != null && !q.isBlank()) return products.findByNameContainingIgnoreCase(q.trim());
    return products.findAll();
  }

  public ProductDoc get(String id) {
    return products.findById(id).orElseThrow(() -> new NotFoundException("Product not found: " + id));
  }

  public ProductDoc create(ProductDoc doc) {
    doc.setId(null);
    return products.save(doc);
  }

  public ProductDoc update(String id, ProductDoc updated) {
    ProductDoc existing = get(id);
    updated.setId(existing.getId());
    return products.save(updated);
  }

  public void delete(String id) {
    ProductDoc existing = get(id);
    products.delete(existing);
  }
}
