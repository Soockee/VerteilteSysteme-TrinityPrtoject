#!/bin/bash
cd factory_service
./gradlew build bootBuildImage
cd ../support_service
./gradlew build bootBuildImage
