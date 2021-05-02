package com.github.almostfamiliar.product.persistence.node;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Optional;
import java.util.Set;

@Data
@Node("Category")
public class CategoryNode {
  @Id @GeneratedValue private Long id;

  private String name;

  @Relationship(direction = Relationship.Direction.INCOMING, type = "SUBCATEGORY")
  private Set<CategoryNode> children;

  public Optional<Set<CategoryNode>> getChildren() {
    return Optional.ofNullable(children);
  }
}
