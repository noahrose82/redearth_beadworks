package com.redearth.service;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.redearth.mongo.doc.ProductDoc;
import com.redearth.mongo.repo.ProductRepository;
import com.redearth.web.error.NotFoundException;

@Service
public class CatalogService {

    private final ProductRepository productRepository;

    public CatalogService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDoc> list(String q, String tag) {
        List<ProductDoc> products = productRepository.findAll();

        if (q != null && !q.isBlank()) {
            String search = q.toLowerCase(Locale.ROOT);
            products = products.stream()
                    .filter(p ->
                            containsIgnoreCase(p.getName(), search) ||
                            containsIgnoreCase(p.getDescription(), search))
                    .toList();
        }

        if (tag != null && !tag.isBlank()) {
            String tagSearch = tag.toLowerCase(Locale.ROOT);
            products = products.stream()
                    .filter(p ->
                            p.getTags() != null &&
                            p.getTags().stream().anyMatch(t -> t != null && t.toLowerCase(Locale.ROOT).contains(tagSearch)))
                    .toList();
        }

        return products;
    }

    public ProductDoc get(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
    }

    public ProductDoc create(ProductDoc doc) {
        doc.setId(null);
        return productRepository.save(doc);
    }

    public ProductDoc update(String id, ProductDoc updated) {
        ProductDoc existing = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setPriceCents(updated.getPriceCents());
        existing.setTags(updated.getTags());
        existing.setAttributes(updated.getAttributes());
        existing.setContentBlocks(updated.getContentBlocks());

        return productRepository.save(existing);
    }

    public void delete(String id) {
        if (!productRepository.existsById(id)) {
            throw new NotFoundException("Product not found");
        }

        productRepository.deleteById(id);
    }

    private boolean containsIgnoreCase(String value, String search) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(search);
    }
}