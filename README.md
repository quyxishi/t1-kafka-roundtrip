# kafka-roundtrip
> T1-debut task

## Getting Started
```shell
git clone https://github.com/quyxishi/t1-kafka-roundtrip
cd ./t1-kafka-roundtrip
```

### *via `docker-compose.yaml`*
```shell
sudo docker-compose up --build
```

### *via `Dockerfile`*
* #### Build
```shell
sudo docker build --tag spring-boot .
```

* #### Running
```shell
sudo docker-compose up -d kafka
sudo docker run --rm -p 8080:8080 \
            -e SPRING_KAFKA_BOOTSTRAP_SERVERS='kafka:9092' \
            --network t1-kafka-roundtrip_default \
            --link kafka:kafka \
            spring-boot
```

## Development
* #### Build
```shell
./gradlew clean build --no-daemon
```

* #### Running
```shell
./gradlew bootRun
```
