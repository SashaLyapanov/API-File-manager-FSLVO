# Стадия сборки
FROM maven:3.9.11-eclipse-temurin-17 AS builder
WORKDIR /app

#Копируем pom и устанавливаем зависимости в проект
COPY pom.xml .
RUN mvn dependency:go-offline -B

#Копируем код и собираем проект
COPY src ./src
RUN mvn clean package -DskipTests -B

#Финальная стадия
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

#Создаем пользователя для безопасности (не root)
RUN addgroup -S filemanager && adduser -S filemanager -G filemanager \
    && mkdir -p \
    /data/files \
    /data/files/aboutFederation \
    /data/files/regionalTeam \
    /data/files/activityFederationGeneral \
    /data/files/activityFederation3D \
    /data/files/activityFederationClassic \
    /data/files/activityFederationBiathlon \
    /data/files/articleImage \
    /data/files/competitions \
    /data/files/personalAccountImage \
    /data/files/regionalTeam \
    && chown -R filemanager:filemanager /data/files

#Копируем JAR из стадии сборки
COPY --from=builder --chown=filemanager:filemanager /app/target/FileManagerFSLVO-0.0.1-SNAPSHOT.jar app.jar

# Переменные окружения
ENV SPRING_PROFILES_ACTIVE=docker \
    SERVER_PORT=8081 \
    STORAGE_DIR=/data/files \
    JAVA_OPTS="-Xmx256m -Xms128m -Dfile.encoding=UTF-8" \
    TZ=Europe/Moscow

#Открываем порт
EXPOSE 8081

USER filemanager
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]

