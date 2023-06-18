FROM maven:3.8.3-openjdk-17

WORKDIR sonata-authorization

COPY . .

ENTRYPOINT ["mvn", "-s settings.xml", "spring-boot:run"]