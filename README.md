# product-service

CRUD-API to manage products in categories

## Concept Phase

### Frontend

The frontend will be a single page application implemented in react, as required by the exercise.

### Backend

Technologies in the service have to be Spring Boot with Java.

#### Architecture Considerations

The question of which architecture type is appropriate for this type of application is not easy to
answer.

On the one hand the use case seems fairly simple at first glance, which would speak for a simple
layered architecture with minimal constraints for simplicity.

On the other hand the feature to convert currencies and already foreseeable changes to the API,
speak for the fact that this service will evolve into a more complex service.

#### Conclusion

From the specifications as they are I would certainly recommend a simple layered architecture. I
decided to go with the more "complicated" approach in the hexagonal architecture though, for the
following reasons:

- From the specification the direction of this project is very unclear. Most (/a lot of?) projects
  evolve to a service with logic from a CRUD app. Finding the point at, which to refactor the code
  is very hard to detect and often missed.

- The project already has a lot of not immediately obvious logic already. For example when changing
  the location of a subcategory, the impact on other categories has to be considered.

- Database considerations. As shown in the following point my database decision is not made with
  100% confidence and may be even controversial. There may be unexpected problems with the unknown
  tech of graph databases, which require a refactor to a different type of persistence. To avoid
  database-driven-design, we need dependency inversion for this use case.

- As this is a demo project to showcase how I would model most services, I would like to show how I
  would like to model a slightly more complicated setup, which takes extensibility maybe more into
  account than I usually would.

### Database

The data model is certainly a challenge with this problem. At first glance it seems simple:

![img](documentation/product_data_model.png)

There is a challenge though: The recursive relationship between categories.

Here are my thoughts in order how to solve this problem:

1. Graph Database

This type of hierarchy screams tree structure to me. Graph databases are designed to deal with
datasets like these.

| Pros  | Cons |
| ------------- | ------------- |
| Fast  | I do not know much about graph databases  |
| Intuitive  |   |

2. Flat Table

Thinking about the usage patterns I thought about dropping the categories table all together and
representing the categories as a key (e.g. /clothes/winter/men) similar to how S3 manages its data.
An index on this key would make queries by category really fast.

| Pros  | Cons |
| ------------- | ------------- |
| Simple  | Does not really work well, if products have to be in multiple categories |
| Fast queries on products | Queries on the category tree may be slow. This should not be a big problem though, because I would consider this data fairly static and the results can be cached. |

3. Tree in NoSql

Modeling a tree in a NoSql Db, with double references to childs and parents. The categories would
have a list of products in them, to be able to query easily by category. The products themselves
would have a reference to their categories in them.

| Pros  | Cons |
| ------------- | ------------- |
| Fast queries on both categories and products | Data Redundancy |
| Easy to adjust the model due to no schema | Complicated to keep the double references in sync |

=> Solution:

I will go with option 1 for the following reasons:

- That I do not much about it. I want to take this opportunity to learn more about it.
- I believe this should be the "correct" solution for this problem.

If I run into problems with this approach due to time trouble, I will revert back to option 3.
