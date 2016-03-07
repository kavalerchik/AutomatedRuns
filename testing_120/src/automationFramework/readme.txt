Documentation about this LEARNING project

"testing_120" - PROJECT
A collection of related code. Generally speaking, each project encompasses one independent program. 
Each programming assignment you do will typically require ITS OWN project

"automationFramework" -  PACKAGE
a technique for organizing Java classes into namespaces (set of symbols that are used to organize objects of various kinds, 
so that these objects may be referred to by name).
Java packages can be stored in compressed files called JAR files, allowing classes to be downloaded faster as groups rather than individually. 
Programmers also typically use packages to organize classes belonging to the same category or providing similar functionality


** What is Asserts (=affirm, allege,claim,argue...)? **
Asserts mean verifying the values. 
Assert can be of Object type, Boolean type, Int type or any data type. 
It simply verify the actual value with the expected value and asserts records only the failed scenario.

** time parameters explained:
long timeGlobalTotal = timeGlobalFinish - timeGlobalStart; ==> used in main to calculate the Program Total running time
main.TotalPagesLoadTime ==> initiated as 0 when program starts. getting addition of __ after each 
	long secBeforeLoadingURL ==> timestamp before loading a new url. inside the URL[i] loop
	long timeS ==> timestamp taken after url[i] loads
	long timeF ==> timestamp taken when 
	long timePageLoad = timeS - secBeforeLoadingURL;
