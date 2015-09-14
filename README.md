# wm-project

## Introduction

This is a simple implementation of the supplied project.  The project leverages a traditional combination of Java, Spring MVC, and Hibernate.  A non-durable H2 in-memory database is bootstrapped with sample data upon startup of the application.  An alternative implementation could've used Jersey or Vert.x with a Java serialization MapDB persistence tier, but I thought the traditional approach would be more demonstrative.

## Instructions

Once the project has been cloned, it can be built and unit tests executed by running the standard `./gradlew build` command.  Invoking jetty (`./gradlew jettyRun`) will launch a local instance.  Simple list and detail web pages can be accessed from [http://localhost:8080](http://localhost:8080).  These pages use AJAX to leverage the underlying service.

## Caveats, Assumptions, and Discussion

I have elaborated on the original, including event and venue, to make the problem a bit more meaningful.  Physical seat assignment would likely be dependent upon venue specific constraints such as balancing the reserved ticket holders across the levels while maximizing contiguous seating.  As this is a slotting problem requiring a rather complicated solution (possibly genetic in nature), I have assumed that seat assignment is done outside of this simple hold/reserve service.  For this demo, I also didn't elaborate the model all the way down to the row level; however, I do account for row data in the availability calculations.

The critical sections of the code are in the actual assignment, purging, and reservation operations.  For the sake of this demo, I have assumed that one app is running and able to meet volume needs.  I am using the DB as a synchronization point which technically will allow multiple app instances to operate concurrent, but not in a highly scalable manner.  An optimal solution would partition event operations across a cluster of applications (ideally with HA).  You would want to use something similar in function to [ActiveMQ message groups](http://activemq.apache.org/message-groups.html).  ActiveMQ could be embedded within the application and clustered via UDP multicast (assuming infrastructure supports this) and be coupled with a second JMS based TicketService implementation that would leverage synchronous messaging to ensure that the event operation was performed in the correct pinned application thus eliminating the need for external event locking/synchronization.  I haven't worked with ESBs before, but I have to assume that this is a commonly supported pattern.  I believe that Storm also attempts to solve this sort of problem, but would be overkill in this situation.

The UI is something that I hacked together very quickly to help visualize the service operation.  It certainly shouldn't be used as a style guide on pristine Angular usage.  It would be far more efficient with WebSockets and some JS optimization, but that seemed beyond the scope of this demo.

Lastly, I didn't use Java 8 specific language features (outside of the diamond operator).  I have been using Groovy with Java for the past couple of years.  I haven't yet become fluent in Java streams and lambda because frankly they still pale in comparison to the power and expressiveness of Groovy.
