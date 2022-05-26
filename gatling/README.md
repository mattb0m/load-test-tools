# Gatling Tools
This sample test code offers reusable code to structure load tests as Steps, and Cases.

It also provides code to inject Dynatrace headers and other dynamic values on all requests. 

Sample command line to run the demo test (with 5 users):

`gradle gatlingRun-demo.test01 -Dusers01=2 -Dusers02=3`

Test components are as follows:
* demo/01_TestSteps/* : Individual test steps (actions for test cases)
* demo/02_TestCases/* : User-level test cases
* demo/test01.scala : Demo load test
* com/mattb0m/perf/* : Generic helper libraries

The following load profile parameters are automatically loaded by the BasicTestConfig class from Java system properties.
The sample load test demonstrates how to read each of these values from the environment:
* rampUp (Int): Time over which to ramp up load, in seconds
* holdLoad (Int): Time over which to hold the full load, in seconds
* rampDown (Int): Time over which to ramp down the load, in seconds
* pacing (Int): Pacing (minimum iteration time) for test cases, in seconds
* pauses (Boolean): Whether or not to respect pauses in test steps ("true" or "false")
* users01 - users99 (Int): Number of users to attribute to each test case/scenario
