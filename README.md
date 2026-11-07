# Hospital Prescription

A Vaadin application for managing doctors, patients and their prescriptions.

<p>
  <a href="https://buymeacoffee.com/lucky_1uck">
    <img src="https://img.shields.io/badge/Support_the_project-FFDD00?style=for-the-badge&amp;logo=buymeacoffee&amp;logoColor=black" alt="Support the project" width="260" height="40">
  </a>
</p>

## Features

- Separate doctor, patient and prescription views.
- Forms for maintaining records and assigning prescription details.
- Prescription priorities represented by the domain model.
- Local HSQLDB persistence through JDBC DAOs.

## Stack

Java 8 · Vaadin 7.6 · HSQLDB · Maven · Jetty

## Run locally

Install JDK 8 and Maven 3, then run from the repository root:

```bash
mvn package
mvn jetty:run
```

Open `http://localhost:8080`. Keep the working directory at the repository root:
the JDBC connection uses the relative file database `db`.

## Project layout

| Location | Contents |
| --- | --- |
| [model](src/main/java/com/zufar/testtask/model) | Doctors, patients, prescriptions and priorities |
| [dao](src/main/java/com/zufar/testtask/dao) | Persistence contracts and connection management |
| [hsqldb](src/main/java/com/zufar/testtask/hsqldb) | Database-specific DAO implementations |
| [ui](src/main/java/com/zufar/testtask/ui) | Vaadin layouts and editing windows |

This repository retains the original Vaadin 7 and Jetty 8 toolchain; use the
declared Java 8 environment when reproducing the exercise.

## ☕ Support the project

If this project helps you learn or build, you can support its development.

<p>
  <a href="https://buymeacoffee.com/lucky_1uck">
    <img src="https://img.shields.io/badge/Support_the_project-FFDD00?style=for-the-badge&amp;logo=buymeacoffee&amp;logoColor=black" alt="Support the project" width="260" height="40">
  </a>
</p>
