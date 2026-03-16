package com.redearth.config;

import com.redearth.mongo.doc.ProductDoc;
import com.redearth.mongo.repo.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
public class MongoSeedConfig {

  @Bean
  public CommandLineRunner seedProducts(ProductRepository repo) {
    return args -> {
      if (repo.count() > 0) return;

      ProductDoc p1 = new ProductDoc();
      p1.setName("Turquoise Bead Bracelet");
      p1.setDescription("Handmade bracelet featuring turquoise beads and sterling accents.");
      p1.setPriceCents(3500);
      p1.setTags(List.of("bracelet", "turquoise", "handmade"));
      p1.setAttributes(Map.of("material", "turquoise", "size", "M"));
      p1.setContentBlocks(List.of(Map.of("type","image","url","/placeholder-bracelet.jpg")));

      ProductDoc p2 = new ProductDoc();
      p2.setName("Navajo-Inspired Necklace");
      p2.setDescription("Statement necklace with layered beadwork and traditional styling.");
      p2.setPriceCents(8900);
      p2.setTags(List.of("necklace", "statement", "beadwork"));
      p2.setAttributes(Map.of("material", "mixed", "lengthInches", 18));
      p2.setContentBlocks(List.of(Map.of("type","image","url","/placeholder-necklace.jpg")));

      repo.saveAll(List.of(p1, p2));
    };
  }
}
