# Java Maven Project Template

This is a Java example project that can be used as a template for other Java projects. However, keep in
mind that you have to customize variables in this project (package names, project name, ...).

## Project setup

### Dependencies

Necessary are:

* Java development kit (JDK) > version 11
* Maven > version 3.5.0

Check in confluence the general programming instructions or the project specific pages in order to specify
which exact versions should be used.

### Building and testing

The project can be build without running any test with the following command. You can find the created
Jar under `./target` in the root directory:

```bash
mvn clean package -DskipTests
```

You may run the unit tests with:

```bash
mvn test
```

You may run integration tests and checks for duplicated classes on the classpath via:

```bash
mvn verify
```

## Developer tools

The following functionality is realized via Maven plugins. Take a look at the pom.xml for further information
and visit the corresponding [Maven plugin websites](https://maven.apache.org/plugins/) or the developer
websites at Github.

### Dependency check

The dependencies can be checked for common vulnerabilities and exposures (CVE). This process takes a while and is
therefore not part of the normal Maven building cycle. You can execute it with the following command:

```bash
mvn -P dependency-check
```

### NOTICE file

The notice file under src/main/resources lists all dependencies of the project including their licenses.

You may test, if the NOTICE file is up-to-date by executing the following command:

```bash
mvn notice:check
```

You can update the NOTICE file with the following command:

```bash
mvn notice:generate
```

### License headers

You may add license headers with the license-maven-plugin via:

```bash
mvn license:format
```

You can also remove them with the following command:

```bash
mvn license:remove
```

## Contribution guidelines ###

// TODO Add stuff

* Writing tests
* Code review
* Other guidelines

## License ###

// TODO Add license info, if necessary or remove

## Contact ###

// TODO Add contact info 