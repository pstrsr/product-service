package com.github.almostfamiliar.product.persistence.node;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Node("Product")
public class ProductNode {
  @Id @GeneratedValue private Long id;

  /** This field is unique and indexed in the database. */
  private String name;

  private String description;
  private BigDecimal price;

  @Relationship(direction = Relationship.Direction.OUTGOING, type = "BELONGS_TO")
  private Set<CategoryNode> categories;
}
