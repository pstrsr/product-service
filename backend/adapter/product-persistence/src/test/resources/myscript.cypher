BEGIN
CREATE CONSTRAINT ON (node:__LiquigraphLock) ASSERT (node.name) IS UNIQUE;
CREATE CONSTRAINT ON (node:`UNIQUE IMPORT LABEL`) ASSERT (node.`UNIQUE IMPORT ID`) IS UNIQUE;
COMMIT
SCHEMA AWAIT
BEGIN
UNWIND [{_id:0, properties:{name:"clothes"}}, {_id:1, properties:{name:"women"}}, {_id:2, properties:{name:"summer"}}, {_id:3, properties:{name:"winter"}}, {_id:4, properties:{name:"sport"}}, {_id:5, properties:{name:"casual"}}, {_id:9, properties:{name:"Electronics"}}, {_id:10, properties:{name:"Tablet"}}, {_id:11, properties:{name:"Laptop"}}] AS row
CREATE (n:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: row._id}) SET n += row.properties SET n:Category;
UNWIND [{_id:6, properties:{price:"1.50", name:"Jacket", description:"Good for skiing!"}}, {_id:7, properties:{price:"1.50", name:"Dress", description:"Pretty!"}}, {_id:8, properties:{price:"1.50", name:"Shirt", description:"Pretty!"}}, {_id:12, properties:{price:"1.50", name:"Surface", description:"Pretty!"}}, {_id:13, properties:{price:"1.50", name:"iPad", description:"Pretty!"}}] AS row
CREATE (n:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: row._id}) SET n += row.properties SET n:Product;
COMMIT
BEGIN
UNWIND [{start: {_id:1}, end: {_id:0}, properties:{}}, {start: {_id:2}, end: {_id:1}, properties:{}}, {start: {_id:3}, end: {_id:1}, properties:{}}, {start: {_id:5}, end: {_id:3}, properties:{}}, {start: {_id:4}, end: {_id:3}, properties:{}}, {start: {_id:11}, end: {_id:9}, properties:{}}, {start: {_id:10}, end: {_id:9}, properties:{}}] AS row
MATCH (start:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: row.start._id})
MATCH (end:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: row.end._id})
CREATE (start)-[r:SUBCATEGORY]->(end) SET r += row.properties;
UNWIND [{start: {_id:6}, end: {_id:4}, properties:{}}, {start: {_id:7}, end: {_id:2}, properties:{}}, {start: {_id:8}, end: {_id:5}, properties:{}}, {start: {_id:12}, end: {_id:11}, properties:{}}, {start: {_id:12}, end: {_id:10}, properties:{}}, {start: {_id:13}, end: {_id:10}, properties:{}}] AS row
MATCH (start:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: row.start._id})
MATCH (end:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: row.end._id})
CREATE (start)-[r:BELONGS_TO]->(end) SET r += row.properties;
COMMIT
BEGIN
MATCH (n:`UNIQUE IMPORT LABEL`)  WITH n LIMIT 20000 REMOVE n:`UNIQUE IMPORT LABEL` REMOVE n.`UNIQUE IMPORT ID`;
COMMIT
BEGIN
DROP CONSTRAINT ON (node:`UNIQUE IMPORT LABEL`) ASSERT (node.`UNIQUE IMPORT ID`) IS UNIQUE;
COMMIT
