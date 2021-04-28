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

As the direction that this project will take is unclear, I recommend going with a hexagonal
architecture to ensure that the size of the project scales over time.

### Database

As we have very little information on how this service will be used this decision is the hardest to
make. As we can not make an informed decision, by the requirements alone, I decided to go with SQL,
as I've been told most of the databases already in use are SQL DBs.



