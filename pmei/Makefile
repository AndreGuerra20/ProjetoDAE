.DEFAULT_GOAL := deploy

ifneq (,$(wildcard ./.env))
    include .env
    export
    APPLICATION_NAME ?= PMEI
endif

up:
	docker compose up -d

down:
	docker compose down --rmi local --volumes

down-all:
	docker compose down --rmi all --volumes

build:
	mvn clean package

bash:
	docker compose exec webserver bash

logs:
	docker compose logs -f webserver

sql:
	docker compose exec db psql --username ${DB_USER} --password ${DB_NAME}

ps:
	docker compose ps

deploy: build
	docker compose cp target/${APPLICATION_NAME}.war webserver:/opt/jboss/wildfly/standalone/deployments
	docker cp src/main/resources/products.csv pmei-webserver-1:/products.csv
	docker cp src/main/resources/direcoes.csv pmei-webserver-1:/direcoes.csv
	docker cp src/main/resources/direcoes2.csv pmei-webserver-1:/direcoes2.csv

monitor: deploy logs

mails:
	docker compose exec --workdir /var/mail smtp bash
