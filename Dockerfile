FROM openjdk:8-jdk-alpine

ADD /build/libs/restaurant-*.jar /

RUN mkdir /opt

RUN mv ./restaurant-*.jar /opt/app.jar

# Expose ports.
EXPOSE 9091
ENTRYPOINT exec java \
	-server -Xms256m -Xmx2048m \
	-Dfile.encoding=UTF-8 \
	-Djava.awt.headless=true \
	-Djava.security.egd=file:/dev/./urandom \
	-jar /opt/app.jar
