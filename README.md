# Populator
An utility to mass load data to a destination. For example, to populate a fresh database with dummy records. Especially useful for large data sets since the amount of threads that are going to be launched can be specified.

Including this project as a dependency in a Spring project registers a `Populator` bean. The component can be commanded via an api that's exposed on `/populator`.

To function properly it needs an `InserterFactory<T>` implementation. Its job is to build instances of **T**, where **T** is any class that extends `AbstractDataInserter<R>`.

**R** represents the datatype of the records that are going to be ingested to populate the destination. For example: a String, a Java Record, a dto from an api, etc.
This class needs to implement the Producer and Consumer methods, to specify how the records are created and how are they inserted in the destination, respectively.
The destination can be anything here. A log call, a text file, a database, etc.

The Populator controls are thread safe. Any state changes are atomic. and api operations are idempotent.

WIP: Describe API for controlling the application.
