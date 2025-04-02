<h3>The "ORDER-PROCESSING-SYSTEM" application (pet-project).</h3>

<p>
A microservice Spring Boot application, where users can register an order and query already registered orders.
</p>

<p>
The <strong>"order-registrar-service"</strong> service receives POST requests to create orders and publishes them to <strong>Kafka</strong>.<br/>
The <strong>"order-demonstrator-service"</strong> service consumes orders from <strong>Kafka</strong> and persists them in the <strong>PostgreSQL</strong> database.<br/>
The following Spring-Cloud technologies are also involved:<br/>
&nbsp;&nbsp; - <strong>eureka-server</strong> (to centralize all services);<br/>
&nbsp;&nbsp; - <strong>api-gateway</strong> (to provide one common URL for end-users);<br/>
&nbsp;&nbsp; - <strong>config-server</strong> (for storing all service properties in one place (in a separate GitHub repository here));
</p>

Microservices (as modules of a Maven project):

<ul>
	<li>
		1. Registrar-service (<strong>order-registrar-service</strong>): Responsible for creating orders.<br/>
	 - Technologies - REST (only POST), Kafka, Swagger.
	</li>
	<br/>
	<li>2. Demonstrator-service (<strong>order-demonstrator-service</strong>): Responsible for storing and demonstrating orders.<br/>
	 - Technologies - REST (only GET), Kafka, PostgreSQL, Flyway, Swagger.
	</li>
	<br/>
	<li>3. Spring Cloud Infrastructure Microservices:<br/>
        - <strong>eureka-server</strong> (spring-cloud-starter-netflix-eureka-server)<br/>
        - <strong>api-gateway</strong> (spring-cloud-starter-gateway)<br/>
        - <strong>config-server</strong> (spring-cloud-config-server)
	</li>
</ul>

