#!/usr/bin/env bash
cd /home/ec2-user/server/grade
sudo java -jar -Dserver.port=8091 -Dspring.profiles.active=prod -Dspring.datasource.url=jdbc:mysql://clickescuela.ccmmeszml0xl.us-east-2.rds.amazonaws.com:3306/clickescuela -Dspring.datasource.username=root -Dspring.datasource.password=secret123 \
    *.jar > /dev/null 2> /dev/null < /dev/null &