#!/bin/bash
chmod +x /home/ec2-user/server/grade/logs
chmod +x /home/ec2-user/server/grade/logs/error.log
chmod +x /home/ec2-user/server/grade/logs/debug.log
var="$(cat /home/ec2-user/server/grade/grade-service.pid)"
sudo kill $var
sudo rm -rf /home/ec2-user/server/grade/grade-service.pid