# Release Version 0.27 & 0.28

**Updates**
* Updated dependency versions


# Release Version 0.26

**Updates**
* Added tasking support


# Release Version 0.25

**Updates**
* Updated StatusCodeException to include the URL that returned the error


# Release Version 0.24

**Updates**
* Changed method signature for EntityList.replaceAll to match related methods


# Release Version 0.23

**Updates**
* Allow ObservedProperty/definition to be set as a String.
* Allow (Multi)Datastream/observedArea to be a non-polygon.


# Release Version 0.22

**Updates**
* Fixed selfLinks being serialised.


# Release Version 0.21

**Updates**
* Fixed de-serialising of empty lists.


# Release Version 0.20

**Updates**
* Improved error handling.
* Initial support for JsonPatch.
* Fixed building on JDK 9+.
* Fixed loss of precision when reading numbers with trailing zeroes.


# Release Version 0.19

**Updates**
* Improved handling of StringIDs, when those IDs are urls.
* Added support for Delete on Collections using $filter, See https://github.com/opengeospatial/sensorthings/issues/44
  `service.observations().query().filter("phenomenonTime lt 2018-01-01T12:00:00Z").delete();`
* Added support for Observations with result explicitly set to null.


# Release Version 0.18

**Updates**
* Fixed it being impossible to clear the select on a query.
* Fixed service not being set on all entities when using $expand.
* Allow Location.location to be any object type, not just geoJSON.
* Added quoteForUrl convenience method.
* Added checks to ensure collections are not created with the singular entity type.


# Release Version 0.17

**Updates**
* add missing id() on entity builders.


# Release Version 0.16

**Updates**
* Added Builder-pattern for creating entities. Thanks to Aurélien Bourdon!


# Release Version 0.15

**Updates**
* We have a name: FROST-Client


# Release Version 0.14

**Updates**
* Support for non-integer ids.
* Added a json properties field to (Multi)Datastream, FoI, Location, ObservedProperty and Sensor.


# Release Version 0.13

**Updates**
* Use BigDecimal for floats so precision is maintained.


# Release Version 0.12

**Updates**
* Allow filter on the query to be changed.
* Added Utils class with escape helper.


# Release Version 0.11

**Updates**
* Added $select option to Query.
* Improved $expand handling.


# Release Version 0.10

**Updates**
* Fixed fetching first item of empty list.
* Allow setting the url after the Service is created (but before it is used).


# Release Version 0.9

**Updates**
* Added a getter for the entityClass of a Query.
* Added initial support for creating Observations through DataArrays.


# Release Version 0.8

**Updates**
* Allow overriding httpclient.
* Added convenience functions.
* Throw error when trying to generate an entity path for an entity without id.
* Don't sent @iot.selfLink when updating.


# Release Version 0.7

**Updates**
* Made httpClient use system settings (for proxy).
* Added missing historicalLocations link to Thing.
* Added hasNext and fetchNext to entityList.
* Added toString() methods to Entities.
* Changed dependency on logback to just slf4j-api.
* Generics improvements.
* Initial support for OpenID Connect secured services.


# Release Version 0.6

**Updates**
* Properly close http responses.
* Added MultiDatastream support.
* Fixed incorrect EntityType for Sensor.
* Added equals and hashCode methods.
* Added method to get copy of entity with only the ID set.


# Release Version 0.5

**Updates**
* Added missing name property to FeatureOfInterest.


# Release Version 0.4

**Updates**
* Throw exception when server returns error when fetching lists.
* Added support for intervals in Observation.phenomenonTime.


# Release Version 0.3

**Updates**
* Fixed json serialisation.
* Added a few initial tests.


# Release Version 0.2

**Updates**
* Tuning the build process: Added javadoc and sources.


# Release Version 0.1

**Updates**
* Documentation update.
* Added lazy loading of relations.
* Added querying of related entity sets.
* Show server output on error.
* Reuse httpClient per service to avoid running out of sockets.
* Added name property to Thing and other entities.
* Entities can supply the dao used to create them.
* Service can directly create and delete entities.
* Don't ignore lists when serialising. Can't set Location of Thing otherwise.
* Lists have a type.
* Lists have a full iterator that follows nextLinks.
* Set id of entity after creation.
