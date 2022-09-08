# ProfileUtils

| :exclamation: Attention                 |
|-----------------------------------------|

This project is only part of a proof of concept and not production ready! It is not tested thoroughly in any way and
vital parts of functionality may be missing. Please use upon own responsibility.

## Description

Currently, the project only provides the utility to map FHIR resource files packed together in one directory to the
profile structure used by [DEMIS](https://www.rki.de/DE/Content/Infekt/IfSG/DEMIS/DEMIS_inhalt.html) for FHIR resources.

## Dependencies

- Java Development Kit (recommended version 17)
- Maven (recommended version 3.8.6)

## Usage

You have to compile the project with Maven:

```
mvn clean package
```

Then you can run the compiled Jar (that one ending on '-exe.jar'). You have to provide two paths:

```
java -jar ./target/profile-util-0.0.1-exe.jar --inputDirectory ./fsh --outputDirectory ./io.noves.example
```

## Contribution & Contact

Feel free to provide pull request with improvements. You may contact us via *info@noves.io*.