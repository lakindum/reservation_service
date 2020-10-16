# Getting Started

# Starting the service
* ./gradlew bootRun will start the service with the in memory H2 database with some pre-populated data.

# Postman Collection
* I have added a postman collection with some tests to the root of the project.

### Assumptions

* For the purpose of this exercise I have taken it as restaurant has a fixed set of timeslots as 11AM - 1PM, 1PM - 3PM, 3PM - 5PM, 5PM - 7PM.
If you need to change this to allow two hour timeslots with any start and end times (Ex: 11.15 AM - 1.15 PM) let me know.

* I think soft deleting a reservation is a better choice than deleting the reservation record from the database, but I have implemented it to remove the 
reservation from the database assuming that's what's expected here as per the task description.

### Design Decisions

* Two database tables restaurant_table and timeslot are the source of truth for tables and timeslot configurations. 
  So we can easily change the restaurantTable configurations and timeslot configurations just by updating those database tables.
  Ex: Adding new tables, changing the timeslot configurations to be 1 hour timeslots etc.
  
  However if we need to update these configurations while the service is in use, so the existing reservations won't be affected,
  then we may need to introduce a new column as "isUsed" in restaurant_table and timeslot tables to mark if those configurations 
  are used or not for future reservations.
  
* I have removed the "id" in request body of the PUT method.
  I think we don't need this since the id of the existing resource is available as a path parameter, and I think
  we should not allow it to update the id of a reservation.

* For the purpose of the test I have only used the default Spring profile, so if this needs to be used in 
  different environments then we will need to introduce few more Spring profiles for them.
  ex: application-test.yaml, application-dev.yaml, application-prod.yaml etc
  
* I will add in a swagger for the API definitions soon.

* I have done minor modifications to some of the service endpoints in the task description to keep a pattern of it

### Tests

* ./gradlew test should run all austomated tests available in the project.

* I have added a considerable amount of unit tests to this project, but I think it can still be improved.
  It would be good if we can have some black box tests (with Rest Assured), component tests and integration tests 
  in addition to the unit tests I have added, but to add them it needs a bit of more time.



