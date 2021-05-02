package com.github.almostfamiliar.product.persistence.repository;

import com.github.almostfamiliar.product.persistence.node.CategoryNode;
import com.github.almostfamiliar.product.persistence.node.ProductNode;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface ProductRepository extends CrudRepository<ProductNode, Long> {
  boolean existsByName(String name);

  @Query("""
         MATCH (n:Product)
         WHERE NOT ID(n)=$id
         AND n.name=$name
         RETURN count(n) > 0
          """)
  boolean existsOtherByName(@Param("id") Long id, @Param("name") String name);

  @Query("""
         MATCH (c:Category) 
         WHERE ID(c)=$id
         MATCH (c)<-[:SUBCATEGORY*0..]-(cat:Category)<-[r:BELONGS_TO]-(p:Product)
         RETURN p, collect(r), collect(cat)
          """)
  Set<ProductNode> findAllByCategory(@Param("id") Long categoryId);

  Set<ProductNode> findAll();
}
