package com.redearth.mysql.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "addresses")
public class AddressEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(nullable = false)
  private String line1;

  private String line2;

  @Column(nullable = false)
  private String city;

  @Column(nullable = false)
  private String state;

  @Column(name = "postal_code", nullable = false)
  private String postalCode;

  @Column(nullable = false)
  private String country;

  public Long getId() { return id; }
  public Long getUserId() { return userId; }
  public String getLine1() { return line1; }
  public String getLine2() { return line2; }
  public String getCity() { return city; }
  public String getState() { return state; }
  public String getPostalCode() { return postalCode; }
  public String getCountry() { return country; }

  public void setId(Long id) { this.id = id; }
  public void setUserId(Long userId) { this.userId = userId; }
  public void setLine1(String line1) { this.line1 = line1; }
  public void setLine2(String line2) { this.line2 = line2; }
  public void setCity(String city) { this.city = city; }
  public void setState(String state) { this.state = state; }
  public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
  public void setCountry(String country) { this.country = country; }
}
