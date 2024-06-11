# Populator
An utility to mass load data to a destination. I used it to populate a lab database with millions of dummy records.

It implements a state machine and has some multi-threading logic.

Some details:
Including this project as a dependency in a Spring project registers a `Populator` bean. The component can be commanded via an api that's exposed on `/populator`.

To function properly it needs an `InserterFactory<T>` implementation. Its job is to build instances of **T**, where **T** is any class that extends `AbstractDataInserter<R>`.

**R** represents the datatype of the records that are going to be ingested to populate the destination. For example: a String, a Java Record, a dto from an api, etc.
This class needs to implement the Producer and Consumer methods, to specify how the records are created and how are they inserted in the destination, respectively.
The destination can be anything here. A log call, a text file, a database, etc.

Other neat features I implemented:
 - The Populator controls are thread safe.
 - Any state changes are atomic.
 - Api operations are idempotent. That is, they don't produce any additional changes if called more than once in a row.

WIP: Describe API for controlling the application.
