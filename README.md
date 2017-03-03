# Readme
This project is to identify customers with highest Life Time Value.
I have used GSON library to parse JSON file. To run just import the file and add the GSON library. Modify the input file for different scenarios.

# Assumption
1. To identify the week I have used week number of the calendar year. i.e (01/01/2017) will be week1.

# Data Structure
1. The customer, order, vist and image data are ingested using custom data structure built on top of object oriented programming. Customer, order, vist and image act as sub class for a super class called Subject and each event is recorded by Event Class.
2. To pick the Top X customers, I have used TreeMap so the insert would always be log(n) and retrieval will be O(1).The LTV double value is used as the key and the TreeMap ensures that the entries are always stored in an ascending order.
