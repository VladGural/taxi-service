How run this application 
You can use file src/main/resources/init_db.sql to create test DB with necessary tables.

Now there is dependency to MySQL DB in pom.xml.
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.25</version>
</dependency>
If you have another DB, you should change this dependency to more fitting to yours DB.

Also, you should in file src/main/java/mate/util/ConnectionUtil.java fill correct values to these fields
    public static final String DRIVER_NAME = "**DRIVER_NAME**";
    public static final String URL = "**URL**";
    public static final String USER = "**USER**";
    public static final String PASSWORD = "**PASSWORD**";

We can run this app without installed tomcat by next command
mvn clean package
java -jar target/dependency/webapp-runner.jar target/*.war

Also, you can test the work this application use next url https://still-brook-27820.herokuapp.com/  