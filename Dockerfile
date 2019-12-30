FROM openjdk:8-jdk-alpine

ADD /build/libs/restaurant-*.jar /

RUN mkdir /application

RUN mv ./restaurant-*.jar /application/app.jar

# Expose ports.
EXPOSE 9091
ENTRYPOINT exec java \
	-server -Xms256m -Xmx2048m \
	-Dfile.encoding=UTF-8 \
	-Djava.awt.headless=true \
	-Djava.security.egd=file:/dev/./urandom \
	-jar /application/app.jar
