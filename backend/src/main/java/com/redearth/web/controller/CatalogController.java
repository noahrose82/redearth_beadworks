package com.redearth.web.controller;

import com.redearth.mongo.doc.ProductDoc;
import com.redearth.security.SecurityUtil;
import com.redearth.service.CatalogService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog/products")
public class CatalogController {

  private final CatalogService catalog;

  public CatalogController(CatalogService catalog) {
    this.catalog = catalog;
  }

  @GetMapping
  public List<ProductDoc> list(@RequestParam(name = "q", required = false) String q,
                               @RequestParam(name = "tag", required = false) String tag) {
    return catalog.list(q, tag);
  }

  @GetMapping("/{id}")
  public ProductDoc get(@PathVariable("id") String id) {
    return catalog.get(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ProductDoc create(@Valid @RequestBody ProductDoc doc) {
    if (!SecurityUtil.hasRole("ADMIN")) throw new com.redearth.web.error.NotFoundException("Not authorized");
    return catalog.create(doc);
  }

  @PutMapping("/{id}")
  public ProductDoc update(@PathVariable("id") String id, @Valid @RequestBody ProductDoc doc) {
    if (!SecurityUtil.hasRole("ADMIN")) throw new com.redearth.web.error.NotFoundException("Not authorized");
    return catalog.update(id, doc);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable("id") String id) {
    if (!SecurityUtil.hasRole("ADMIN")) throw new com.redearth.web.error.NotFoundException("Not authorized");
    catalog.delete(id);
  }
}
