FROM node:18-alpine AS buildFrontend
COPY ./Frontend /home/node/src
WORKDIR /home/node/src
RUN npm ci
RUN npm run build

FROM eclipse-temurin:20-jdk AS buildBackend
COPY . /home/gradle/src
COPY --from=buildFrontend /home/node/src/dist /home/gradle/src/Backend/bootstrap/src/main/resources/static
WORKDIR /home/gradle/src
RUN ./gradlew shadowJar --no-daemon

FROM eclipse-temurin:20-jre
EXPOSE 8080:8080
RUN mkdir /app
COPY --from=buildBackend /home/gradle/src/Backend/bootstrap/build/libs/*.jar /app/jasstracker.jar
ENTRYPOINT ["java","-jar","/app/jasstracker.jar"]
