## About this project
In this project I want to show my skills in Java, OOP and SOLID principles, JDBC (with key CRUD operations), Web.
~~~
*Firstly, in this application you should create a new Driver and then pass authentication 
using your new profile.
*Inside the application, you can create new Drivers, Manufacturers, Cars, and add Drivers to Cars.
*You can also view list of existing Drivers, Manufacturers, and Cars.
*You can delete Drivers, Manufacturers, and Cars.
*Application doesn't allow you to create Drivers with same License Numbers 
(License Number must be in particular format) or Manufacturers with same names.
~~~    
You can test the application by using the following url 
[https://still-brook-27820.herokuapp.com/](https://still-brook-27820.herokuapp.com/)  

## Technologies which I used
Project is created with:
```
* Java 11
* MySQL
* Maven
* Javax servlet API
* Jstl
* Tomcat 9.0.50 (to run app locally. But you can run this app without installing Tomcat, see below)
```

## Setup
You should install MySQL or another DB

Fork this project and clone it.

You can use file `src/main/resources/init_db.sql` to create test DB with necessary tables.

Now there is dependency "MySQL DB" in `pom.xml`.
~~~
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.25</version>
</dependency>
~~~
If you have another DB, you should change this dependency to better fit your DB.

Also, in the file `src/main/java/mate/util/ConnectionUtil.java` you should fill correct values in these fields
~~~
    public static final String DRIVER_NAME = "**DRIVER_NAME**";
    public static final String URL = "**URL**";
    public static final String USER = "**USER**";
    public static final String PASSWORD = "**PASSWORD**";
~~~

We can run this application without installing Tomcat by using next command
~~~
mvn clean package
java -jar target/dependency/webapp-runner.jar target/*.war
~~~

