## Coding Challenge 

### Prerequisite:
 * sbt -> https://www.scala-sbt.org/ <enter>
 * java -> https://www.oracle.com/technetwork/java/javase/downloads/index.html
 * docker (Optional) -> https://www.docker.com/

### Running and testing:
 1. Open a terminal and locate to the project's root folder.
 2. Type "sbt reload" (Optional) to reload the project.
 3. Type "sbt test" to run the test.
 4. Type "sbt run" to run.

 ### Running on Docker:
 1. Open a terminal and locate to the project's root folder.
 2. Type "sbt reload" (Optional) to reload the project.
 3. Type "sbt docker:publishLocal" to build and Dockerized.
 4. Type "docker run -p8081:8081 coding-challenge:0.0.1-SNAPSHOT" to run the server in port 8081
 
#### healthcheck http://localhost:8081

### Routes
http://localhost:8081/all <br/> 
http://localhost:8081/fetch/2 <br/> 
http://localhost:8081/update/3/2 <br/> 
http://localhost:8081/insert/4000/3001/3001 <br/> 
