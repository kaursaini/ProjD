# ProjD

Web App created using JSP

The project will expose the key elements of JSP and leverage advanced features in Tomcat such as persistence scopes, filters for ad-hoc changes, and listeners for analytics. 
The project will also expose some of the Map APIs in Google's cloud platform.

## The Services
    S0: Dashboard
    Serving as a main menu for the webapp.
    S1: Prime Number Finder
    Find the next prime in a given range one by one (prompting in between).
    S2: GPS Distance Calculator
    Compute the distance (in km) between two points on the Earth surface given the latitude and longitude of each.
    S3: Drone Delivery Timer
    Compute the time it takes an average delivery drone to make a delivery given the starting and ending street addresses in a city.
    S4: Ride Cost Estimator
    Compute the cost of a ride (e.g. Uber) given its starting address and destination after factoring in the current traffic delays.
    S5: SIS Report Generator
    Generate a report from a Student Information System filtered based on a name prefix, a major, and a minimum GPA. The output is sorted on a custom column.

The web app fetch the data from the local database (running on the same machine as the webapp). Its data can be accessed via the following specs:
    Protocol: jdbc//derby.
    Host: localhost.
    Port: 64413.
    Database: EECS.
    Credentials: as in Project-B.
    Table: SIS.
    Columns: SURNAME, GIVENNAME, MAJOR, COURSES, and GPA
    
Filter -- October.java  
    Block the Ride service completely by serving a page that informs the user that this service is not available with a link to the Dashboard.
    Block any request for sorting in the SIS service. If any sorting is requested then serve a page that informs the user that this sorting is not available with a link to the Dashboard.

Listener -- Monitor.java 
    Determine the percentage of times the Drone service is used.
    Determine the likelihood that a user will use both S3 and S4.
