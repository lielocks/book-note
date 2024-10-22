#!/bin/bash

cd /home/ubuntu/app

DOCKER_APP_NAME=spring
LOG_FILE="deploy.log"

# 실행중인 blue가 있는지
EXIST_BLUE=$(docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml ps | grep running)

# green이 실행중이면 blue up
if [ -z "$EXIST_BLUE" ]; then
	echo "$(date): blue up" >> $LOG_FILE
	docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml up -d --build >> $LOG_FILE 2>&1

	sleep 30

	docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml down >> $LOG_FILE 2>&1
	docker image prune -af >> $LOG_FILE 2>&1

# blue가 실행중이면 green up
else
	echo "$(date): green up" >> $LOG_FILE
	docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml up -d --build >> $LOG_FILE 2>&1

	sleep 30

	docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml down >> $LOG_FILE 2>&1
	docker image prune -af >> $LOG_FILE 2>&1
fi
