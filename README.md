# CSCI-3901-A1
Software Development Concepts - Decomposing a problem, designing and testing a java program

Overview
--------

This program reads the data from the file and stores a matrix data into a
data structure and perform set of operations on the data and print it to 
screen or the file mentioned. Total Summary of the necessary requirements
are in assignment #1 information in the course's brightspace space.

The problem uses different methods to solve based on the information
provided in the input file.

- The data will be in matrix format as rows and columns.
- Read the data from the file.
- Create new columns and do basic operations on the column Data.
- Print or Write to specific file after manuipulation of data.

Necessary operations on the datacan have their own methods to process and 
output is operated by the content stored in LinkedHashMap and ArrayLists.

Files and external data
-----------------------

There are two main files:
  - DataChangeUI.java  -- main for the program that prompts the user for the file name
  - DataTransformer.java -- class that makes the operations on the file and do operations on the data columns
  - readFile.txt -- Text file that reads the data
  - writeFile.txt -- Text file to write the data to the file

Data structures and their relations to each other
-------------------------------------------------

The program reads the matrix data in a form of linked hash maps and
Array lists. Each map uses the column Name as the key and an Array Lists
for the column Data as the value.

    clear 
  clear all the data in the object based on the conditions and returns true if deleted

    read 
  The program reads the filename and store the column names in hash map as the key and 
  store all the column data in the array lists and returns the number of data rows read.

    newColumn 

  The program adds the new column as keys in the map created.The column will only store an integer 
  and will begin with a value of 0 that are stored in array list. 

    calculate
  The program takes the equation as a string and applies the given equation by splitting the operands and operators 
  to perform operations to the data in the object using switch cases and return the number of rows that were calculated
  for the equation and retuens the number of rows that are calculated.

    top
  The program prints the first 5 rows to the screen which includes a starting row with the column
  names. The order of the columns will be the same as in the input file based on the linked hash map stored.

    print
  The program prints all rows to the screen which includes a starting row with the column
  names.

    write
  The program writes all the content present in the linked hash map to the give file which includes a
  first row with the names of the columns and returns the number of data rows written to the file.

Assumptions
-----------

  - input file will have at most 10 columns.
  - file names will contain the full path to the file, including any file name extensions.
  - maximum of 80 characters in each line.
  - column names will be a single alphabetic string with no spaces in between.
  - no equation will be asked to divide by zero.
  - data should be of same data type after created.
  - atlease one space between each component of the equation.

Key algorithms and design elements
----------------------------------

The program reads the data from the input file reading each line at a time.
In the output, it appears as the same as the with spaces and tabs between the 
columns with the data manipulated.

The program reads the text as the input and splits the data into column names and
stored as a key in the map created. The data after the first line with the tab space 
each will be stored in the array list as the value of the map created.

Upon the data structure created multiple operations can be done on the column data
based on the conditions and prints the data to the file given.
