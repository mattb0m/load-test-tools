# Gatling Tools
This sample test code offers reusable code to structure load tests as Steps, and Cases.

It also provides code to inject Dynatarce headers and other dynamic values on all requests. 

Sample command line to run the demo test (with 5 users):

`gradle gatlingRun-demo.test01 -Dusers=5`

Test components are as follows:
* demo/01_TestSteps/* : Individual test steps (actions for test cases)
* demo/02_TestCases/* : User-level test cases
* demo/test01.scala : Demo load test
* com/mattb0m/perf/* : Generic helper libraries
