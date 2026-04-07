package com.redearth.config;

import java.util.List;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.redearth.mongo.doc.ProductDoc;
import com.redearth.mongo.repo.ProductRepository;

@Configuration
public class MongoSeedConfig {

  @Bean
  public CommandLineRunner seedProducts(ProductRepository repo) {
    return args -> {
      repo.deleteAll();

      ProductDoc p1 = new ProductDoc();
      p1.setName("Turquoise Bead Bracelet");
      p1.setDescription("Handmade bracelet featuring turquoise beads and sterling accents.");
      p1.setPriceCents(3500);
      p1.setTags(List.of("bracelet", "turquoise", "handmade"));
      p1.setAttributes(Map.of("material", "turquoise", "size", "M"));
      p1.setContentBlocks(List.of(Map.of("type", "image", "url", "/bracelet1.jpg")));

      ProductDoc p2 = new ProductDoc();
      p2.setName("Navajo-Inspired Necklace");
      p2.setDescription("Statement necklace with layered beadwork and traditional styling.");
      p2.setPriceCents(8900);
      p2.setTags(List.of("necklace", "statement", "beadwork"));
      p2.setAttributes(Map.of("material", "mixed", "lengthInches", 18));
      p2.setContentBlocks(List.of(Map.of("type", "image", "url", "/necklace1.jpg")));

      ProductDoc p3 = new ProductDoc();
      p3.setName("Layered Bead Bracelet");
      p3.setDescription("Multi-layer bracelet with traditional beadwork.");
      p3.setPriceCents(4200);
      p3.setTags(List.of("bracelet", "beaded"));
      p3.setAttributes(Map.of("material", "beads", "size", "M"));
      p3.setContentBlocks(List.of(Map.of("type", "image", "url", "/bracelet2.jpg")));

      ProductDoc p4 = new ProductDoc();
      p4.setName("Turquoise Drop Earrings");
      p4.setDescription("Elegant turquoise drop earrings with silver accents.");
      p4.setPriceCents(4800);
      p4.setTags(List.of("earrings", "turquoise"));
      p4.setAttributes(Map.of("material", "turquoise", "style", "drop"));
      p4.setContentBlocks(List.of(Map.of("type", "image", "url", "/earrings1.jpg")));

      ProductDoc p5 = new ProductDoc();
      p5.setName("Layered Bead Necklace");
      p5.setDescription("Layered necklace with traditional beadwork pattern.");
      p5.setPriceCents(8900);
      p5.setTags(List.of("necklace", "beaded"));
      p5.setAttributes(Map.of("material", "beads", "lengthInches", 20));
      p5.setContentBlocks(List.of(Map.of("type", "image", "url", "/necklace2.jpg")));

      ProductDoc p6 = new ProductDoc();
      p6.setName("Statement Bead Necklace");
      p6.setDescription("Bold statement necklace with handcrafted beadwork.");
      p6.setPriceCents(9200);
      p6.setTags(List.of("necklace", "statement"));
      p6.setAttributes(Map.of("material", "beads", "lengthInches", 22));
      p6.setContentBlocks(List.of(Map.of("type", "image", "url", "/necklace3.jpg")));

      ProductDoc p7 = new ProductDoc();
      p7.setName("Turquoise Pendant");
      p7.setDescription("Traditional turquoise pendant with handcrafted silver styling.");
      p7.setPriceCents(6100);
      p7.setTags(List.of("pendant", "turquoise"));
      p7.setAttributes(Map.of("material", "turquoise", "style", "pendant"));
      p7.setContentBlocks(List.of(Map.of("type", "image", "url", "/pendant1.jpg")));

      ProductDoc p8 = new ProductDoc();
      p8.setName("Silver Feather Earrings");
      p8.setDescription("Handcrafted sterling silver feather earrings inspired by Navajo design.");
      p8.setPriceCents(4500);
      p8.setTags(List.of("earrings", "silver", "handmade"));
      p8.setAttributes(Map.of("material", "silver", "style", "feather"));
      p8.setContentBlocks(List.of(Map.of("type", "image", "url", "/earrings2.jpg")));

      ProductDoc p9 = new ProductDoc();
      p9.setName("Traditional Beaded Necklace");
      p9.setDescription("Handcrafted necklace featuring traditional beadwork patterns.");
      p9.setPriceCents(8500);
      p9.setTags(List.of("necklace", "beaded", "handmade"));
      p9.setAttributes(Map.of("material", "beads", "lengthInches", 18));
      p9.setContentBlocks(List.of(Map.of("type", "image", "url", "/necklace4.jpg")));

      ProductDoc p10 = new ProductDoc();
      p10.setName("Sterling Silver Cuff Bracelet");
      p10.setDescription("Classic sterling silver cuff bracelet inspired by traditional Navajo jewelry.");
      p10.setPriceCents(9500);
      p10.setTags(List.of("bracelet", "silver", "cuff"));
      p10.setAttributes(Map.of("material", "silver", "style", "cuff"));
      p10.setContentBlocks(List.of(Map.of("type", "image", "url", "/cuff1.jpg")));

      ProductDoc p11 = new ProductDoc();
      p11.setName("Handmade Turquoise Ring");
      p11.setDescription("Handmade turquoise ring set in sterling silver with Southwestern styling.");
      p11.setPriceCents(5500);
      p11.setTags(List.of("ring", "turquoise", "silver"));
      p11.setAttributes(Map.of("material", "turquoise", "style", "ring"));
      p11.setContentBlocks(List.of(Map.of("type", "image", "url", "/ring1.jpg")));

      ProductDoc p12 = new ProductDoc();
      p12.setName("Silver Pendant Necklace");
      p12.setDescription("Elegant silver pendant necklace with artisan bead detailing.");
      p12.setPriceCents(6700);
      p12.setTags(List.of("necklace", "silver", "pendant"));
      p12.setAttributes(Map.of("material", "silver", "lengthInches", 18));
      p12.setContentBlocks(List.of(Map.of("type", "image", "url", "/pendant2.jpg")));

      repo.saveAll(List.of(
          p1, p2, p3, p4, p5, p6,
          p7, p8, p9, p10, p11, p12
      ));
    };
  }
}