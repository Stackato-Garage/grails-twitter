Grails Twitter application
==========================

This moderately complex Twitter clone demonstrates how you can combine a standard SQL store, MongoDB, and Redis all within a single Grails application deployed on Cloud Foundry. It's pretty fast too!

Originally from [Jeff Brown's](https://github.com/jeffbrown) [grails-samples](https://github.com/grails-samples/grailstwitter) repo, this version was adapted from SpringSource's [cloudfoundry-samples](https://github.com/SpringSource/cloudfoundry-samples).

The messages (MongoDB)
----------------------

Each message that a user posts is stored in a MongoDB database as an instance of `org.grails.twitter.Status`. If you take a look at the class, you will see the line:

    static mapWith = "mongo"

which binds the domain class to MongoDB instead of MySQL. You will also see that there is a constraint limiting the size of a message to 160 characters.

Status messages have no complex relationships but you can expect a large volume of them. That makes MongoDB an ideal data store for them. In addition, MongoDB works well with embedded collections of simple types like strings, hence that's how tags are implemented:

    List<String> tags = []

The downside of this approach is that you don't have a separate domain class to query for tags. Instead, the `cacheTags()` method of `org.grails.twitter.TagService` uses a map-reduce function to aggregate tag information.

The users (MySQL)
-----------------

Access control for this sample application is handled via Spring Security. It's possible to configure the library to use MongoDB for the users, but it's far simpler to user MySQL instead. So, the `org.grails.twitter.auth.Person` and `org.grails.twitter.auth.Authority` domain classes are both mapped via Hibernate.

Now, in most Grails applications `Status` would have a direct reference to the `Person` class, i.e. the author of the status message, but this isn't possible when the two domain classes are mapped by different providers. So, `Status` stores an explicit `Authority` instance ID (as the property `authorId`) and provides easy access to the associated `Authority` instance via the read-only, transient `author` property.

Caching (Redis)
---------------

Evaluation of the tags and how many messages contain each of them is a fairly expensive operation, so we don't want to keep doing it every time the main page is hit. So, when a status message is posted and saved, the tags are re-evaluated on a background thread and cached in a Redis database. Every time the tags are required for the main page, they are pulled from Redis. This is all handled inside `TagService`.

Those are the basics of how the application fits together and how each of the data stores is used. Trying it out is a simple case of getting hold of the source code from GitHub, then building and running it.

Building and running
--------------------

To run this application, make sure you have the appropriate version of Grails installed (1.3.7 at the time of writing - but check application.properties). Also make sure you have instances of MongoDB and Redis running, both on the default settings. Then:

    grails run-app

This will start the application in development mode and use the local MongoDB and Redis instances plus an in-memory HSQLDB database.

Deploying to Stackato
---------------------

These instructions use the [stackato](http://www.activestate.com/stackato/download_client) client rather than the Grails cloudfoundry plugin, which does not currently work with Stackato.

First you need to build the .war file. To do this, you will need to install [Tomcat](http://tomcat.apache.org/download-70.cgi) and [Ant](http://ant.apache.org/), as well as set JAVA_HOME, CATALINA_HOME, and ANT_HOME. This works with Grails 2.2.0, but it should work with a greater version:

    grails prod war

Target a Stackato API endpoint with the client, then push:

    stackato push -n

You can specify your own application name to override the default 'grails-twitter':

    stackato push -n mytwitter




