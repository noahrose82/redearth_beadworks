package com.redearth.mongo.doc;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(collection = "products")
public class ProductDoc {
  @Id
  private String id;

  private String name;
  private String description;
  private int priceCents;
  private List<String> tags;

  // Flexible attributes for different jewelry types: size, material, beadType, etc.
  private Map<String, Object> attributes;

  // Content blocks: images, markdown, etc. (simplified)
  private List<Map<String, Object>> contentBlocks;

  public String getId() { return id; }
  public String getName() { return name; }
  public String getDescription() { return description; }
  public int getPriceCents() { return priceCents; }
  public List<String> getTags() { return tags; }
  public Map<String, Object> getAttributes() { return attributes; }
  public List<Map<String, Object>> getContentBlocks() { return contentBlocks; }

  public void setId(String id) { this.id = id; }
  public void setName(String name) { this.name = name; }
  public void setDescription(String description) { this.description = description; }
  public void setPriceCents(int priceCents) { this.priceCents = priceCents; }
  public void setTags(List<String> tags) { this.tags = tags; }
  public void setAttributes(Map<String, Object> attributes) { this.attributes = attributes; }
  public void setContentBlocks(List<Map<String, Object>> contentBlocks) { this.contentBlocks = contentBlocks; }
}
