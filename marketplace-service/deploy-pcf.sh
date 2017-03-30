#!/bin/bash#!/bin/bash


ENV=manifest

while [ "$#" -gt 0 ]; do
  case "$1" in
    -e|--environment)
		ENV="$2"
		shift 2
		;;
  esac
done

# Build the project
./gradlew clean build

# Push application to PCF environment
cf push -f "${ENV}.yml"