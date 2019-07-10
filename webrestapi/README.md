# Vacation tracking system

Java web application that will act as a vacation tracking system. Allows users to manage vacations.

## Prerequisites
 - Java 8
 - Maven
 - SQL SERVER

## How to run it
 - Start sql server
 - If you have issues with time zone then execute as sql query ``` set global time_zone = "YOURZONE?";```
 - Edit hibernate.cfg.xml (located in resources) with your sql username and password. For example username as root and password as "1234"
 - Start Application.sh (creates .war and .jar files)
 - Now you can deploy .war on your server or open localhost:8080 in browser
## Info

By default there are 4 teams and admins which are already registered. If you want to put/change data you should do it with sql queries directly to database.

| Teams | Admins | Password | Email |
| ------ | ------ | ------ | ------ |
| Developers | devadmin | 1234| axwayacademytest@mail.bg |
| Accountants | accadmin |       1234     | test@mail.bg |
| QA | qaadmin | 1234 | test2@mail.bg |
| Managers | mngadmin | 1234 | test3@mail.bg |

axwayacademytest@mail.bg is real and working email created for the purpose of the demo.Password is : 12345


## Built With
 - [Maven](https://maven.apache.org/) - Dependency Management
-  [Spark Framework](http://sparkjava.com/) - Web framework
-  [Hibernate](https://hibernate.org/) - ORM
-  [MySQL](https://www.mysql.com/) - Database
-  [Liquibase](https://www.liquibase.org/) - Source control for database;managing and initial setup

## Authors
- Krasimir Dimitrov - [GitLab](https://gitlab.com/kddimitrov)
- Yani Mihaylov - [GitLab](https://gitlab.com/yaniMihaylov)