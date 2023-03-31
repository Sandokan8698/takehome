### Task Notes

### Build

To compile the application run the following command, this will create a ready-to-run application
docker image (`example/takehome:0.0.1-SNAPSHOT`) where `0.0.1-SNAPSHOT` represents the current project
version and (`takehome`) is the name of the project, you can find the generated docker file in the build/docker
directory

```
./gradlew clean test dockerBuildImage 
```

or

```
 ./gradlew clean test dockerBuildImage -PappPorts=9090 -PappJvmArgs="-Xms256m -Xmx512m"
```

### Run

Run the application by executing the below command, it should allow to send request to the api,
on the chosen port.

```
docker run -d -p 8080:8080  example/takehome-service:0.0.1-SNAPSHOT
```

### Request Example

```
curl --location 'localhost:8080/countries?countryCode=CA&countryCode=US'
```

### Notice

Please notice that the rate limit functionality implementation is just a naive approach to what should be used in an
application;
this implementation is just a POC of one of the many approaches to getting it done.