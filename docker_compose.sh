#!/bin/bash

# Function to get the health status of the db container
get_db_health() {
    docker inspect --format "{{json .State.Health.Status }}" db
}

# Start and build the db container
docker compose up -d db --build

# Wait for the db to be healthy
until [ "$(get_db_health)" == "\"healthy\"" ]
do
    echo "Waiting for db to be healthy..."
    sleep 5
done

# Build the Docker images
docker compose up --build