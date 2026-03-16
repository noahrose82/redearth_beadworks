package com.redearth.mongo.repo;

import com.redearth.mongo.doc.ProductDoc;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<ProductDoc, String> {
  List<ProductDoc> findByTagsContainingIgnoreCase(String tag);
  List<ProductDoc> findByNameContainingIgnoreCase(String name);
}
