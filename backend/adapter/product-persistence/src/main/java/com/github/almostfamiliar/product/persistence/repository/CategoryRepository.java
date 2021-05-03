package com.github.almostfamiliar.product.persistence.repository;

import com.github.almostfamiliar.product.persistence.node.CategoryNode;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface CategoryRepository extends CrudRepository<CategoryNode, Long> {
  @Query("""
  MATCH (n:Category)
  WHERE NOT (n)-[:SUBCATEGORY]->()
  MATCH p = (n)<-[:SUBCATEGORY*0..]-(c)
  WITH *, relationships(p) AS r
  RETURN n,collect(c),collect(r)
  """)
  Set<CategoryNode> findAllRoots();

  @Query("""
         MATCH (n:Category)
         WHERE NOT (n)-[:SUBCATEGORY]->()
         AND n.name = $name
         RETURN count(n) > 0
         """)
  boolean existsRoot(@Param("name") String name);

  @Query("""
          MATCH (c:Category)
          WHERE ID(c)=$id
          MATCH (c)-[:SUBCATEGORY*0..]->(cat:Category)
          MATCH (cat {name:$name})
          RETURN count(cat) > 0
          """)
  boolean existsParentByName(@Param("id") Long id, @Param("name") String name);

  @Query("""
         MATCH (c:Category)
         WHERE ID(c)=$id
         OPTIONAL MATCH (c)-[:SUBCATEGORY]->(p:Category)
         MATCH (p)<-[r:SUBCATEGORY]-(child)
         RETURN p, collect(r),collect(child)
         """)
  Optional<CategoryNode> findParent(@Param("id") Long id);

  Set<CategoryNode> findAll();

  @Query("""
         MATCH (c:Category)
         WHERE ID(c)=$id
         MATCH (c)<-[]-()
         RETURN count(c)>0
         """)
  boolean existsAnyIncomingRelationship(@Param("id") Long id);
}
