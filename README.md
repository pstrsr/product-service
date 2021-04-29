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

As we have very little information on how this service will be used this decision is the hardest to
make. As we can not make an informed decision, by the requirements alone, I decided to go with SQL,
as I've been told most of the databases already in use are SQL DBs.



