#!/bin/bash
var="$(cat /home/ec2-user/server/grade/grade-service.pid)"
sudo kill $var
sudo rm -rf /home/ec2-user/server/grade/grade-service.pid