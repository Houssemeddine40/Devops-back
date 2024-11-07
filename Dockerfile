# Utiliser l'image OpenJDK 17
FROM openjdk:17-jdk-slim



EXPOSE 8089

# Copy the JAR file to the Docker container
ADD target/tpAchatProject-1.0.jar tpAchatProject-1.0.jar

# Run the JAR file when the container starts
ENTRYPOINT ["java", "-jar", "/tpAchatProject-1.0.jar"]




## Expose le port 8082 pour votre application Spring Boot
#EXPOSE 8089
#
## Variables pour Nexus (à adapter selon votre configuration)
#ARG NEXUS_URL=http://192.168.72.128:8081
#ARG REPO_PATH=repository/maven-releases/com/esprit/examen/tpAchatProject/1.0/tpAchatProject-1.0.jar
#ARG NEXUS_USERNAME=admin
#ARG NEXUS_PASSWORD=jasser
#
## Récupérer le livrable depuis Nexus et le renommer
#RUN apt-get update && apt-get install -y curl && \
#    curl -o tpAchatProject-1.0.jar -u $NEXUS_USERNAME:$NEXUS_PASSWORD $NEXUS_URL/$REPO_PATH
#
## Commande pour lancer l'application Spring Boot
#ENTRYPOINT ["java", "-jar", "/tpAchatProject-1.0.jar"]