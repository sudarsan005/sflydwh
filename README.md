# Readme
This project is to identify customers with highest Life Time Value.
I have used GSON library to parse JSON file. To run just import the file and add the GSON library as an external JAR (http://central.maven.org/maven2/com/google/code/gson/gson/2.8.0/gson-2.8.0.jar). Modify the input file for different scenarios.

# Assumption
1. To identify the week I have used week number of the calendar year. i.e (01/01/2017) will be week1.
2. There may be multiple expenditures made by a customer in a single week. I have taken the expenditure per visit as the average of all these individual expenditure amounts.
3. The LTV value is a double value with high precision and is assumed to be unique for each cutomer.

# Data Structure
1. The customer, order, vist and image data are ingested using custom data structure built on top of object oriented programming. Customer, order, vist and image extend from super class called Subject and each event is recorded by Event Class.
The event class uses Generics to decide the type of event at runtime. I have chosen to store all the objects (customers, site visits, images and orders) in HashMaps. HashMap was preferred considering the efficiency of retrieving the objects by their keys.
2. To retrieve the Top X customers, I have used TreeMap so the insert would always be log(n) and retrieval will be O(1).The LTV double value is used as the key and the TreeMap ensures that the entries are always stored in an ascending order. I could have enhanced this further to maintain a TreeMap at all times and just keep updating it on every event occurrance. By this, the TreeMap does not have to be built on every call to topXSimpleLTVCustomers(). However, this would depend on which operation is expected to happen more frequently and hence needs to be more efficient, arrival of events or the call to the topXSimpleLTVCustomers() method. Here, the assumption is the arrival of events is more frequent as compared to the call to topXSimpleLTVCustomers().

I have called topXSimpleLTVCustomers() with the input parameter of 2 which would return the top 2 customers with LTV values. The value of 'x' can be changed as desired in the main function.
