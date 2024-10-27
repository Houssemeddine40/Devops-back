# Utiliser l'image OpenJDK 17
FROM openjdk:17-jdk-slim

# Expose le port 8082 pour votre application Spring Boot
EXPOSE 8082

# Variables pour Nexus (à adapter selon votre configuration)
ARG NEXUS_URL=http://192.168.109.129:8081
ARG REPO_PATH=repository/maven-releases/com/monprojet/5DS4-tpAchatProject/1.0/5DS4-tpAchatProject.jar
ARG NEXUS_USERNAME=admin
ARG NEXUS_PASSWORD=admin

# Récupérer le livrable depuis Nexus et le renommer
RUN apt-get update && apt-get install -y curl && \
    curl -o 5DS4-tpAchatProject.jar -u $NEXUS_USERNAME:$NEXUS_PASSWORD $NEXUS_URL/$REPO_PATH

# Commande pour lancer l'application Spring Boot
ENTRYPOINT ["java", "-jar", "/5DS4-tpAchatProject.jar"]
