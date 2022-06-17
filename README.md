<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/Suracki/Mediscreen">
    <img src="/logomin.png" alt="Logo">
  </a>

<h3 align="center">Mediscreen: Patient Demographics</h3>

  <p align="center">
    Mediscreen Patient Demographics application.
    <br>
    Provides functionality for interfacing with MySQL database for Patient data storage.

  </p>
</div>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li><a href="#getting-started">Getting Started</a></li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

This is the Patient Demographics microservice for the Mediscreen application package.
<br><br>
This part of the application interfaces with the MySQL database to store Patient data, and serves front end pages for interfacing with this data, as well as the main app homepage.
<br><br>
In addition to the font end pages, the application also provides REST API endpoints.
<p align="right">(<a href="#top">back to top</a>)</p>



### Built With

* [Java 11](https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html)
* [Spring Boot](https://spring.io/projects/spring-boot)
* [Maven](https://maven.apache.org/)
* [Gson](https://github.com/google/gson)
* [RetroFit](https://square.github.io/retrofit/)
* [Thymeleaf](https://www.thymeleaf.org/)
* [Bootstrap](https://getbootstrap.com)


<p align="right">(<a href="#top">back to top</a>)</p>



<!-- GETTING STARTED -->
## Getting Started

This microservice is intended to be used as part of the Mediscreen package, and will not operate correctly without the other parts.
<br>Please refer to the Mediscreen Pagage repository for details on running the package as a whole.

<p align="right">(<a href="#top">back to top</a>)</p>



<!-- USAGE EXAMPLES -->
## Usage

### Front End

The application serves a Front End UI using Thymeleaf and Bootstrap, which can be accessed via the following URLs:

/ -> Home Page, welcome page with links to guide user through interface<br>
/patient/list -> List of all Patients currently stored in the system<br>
/patient/add -> UI for adding a new Patient to the system<br>
/patient/view/{id} -> UI to view details of a Patient in the system<br>
/patient/update/{id} -> UI to update details of a Patient in the system<br>

### API

The application provides a REST API with the following endpoints

/patient/api/add -> add a Patient to the system<br>
/patient/api/get/{id} -> get a Patient from the system<br>
/patient/api/update -> update a Patient in the system<br>

_For full details of API usage, please refer to the [API specification document](/REST%20API%20Specification.pdf)_

<p align="right">(<a href="#top">back to top</a>)</p>


<!-- CONTACT -->
## Contact

Simon Linford - simon.linford@gmail.com

Project Link: [https://github.com/Suracki/Mediscreen](https://github.com/Suracki/Mediscreen)

<p align="right">(<a href="#top">back to top</a>)</p>
