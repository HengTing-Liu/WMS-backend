@echo off
set JAVA_HOME=d:\jdk1.8.0_482
set PATH=%JAVA_HOME%\bin;d:\apache-maven-3.9.14\bin;%PATH%
cd /d d:\gitwms\WMS-backend
call mvn clean compile -DskipTests
