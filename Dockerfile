FROM node:22-alpine AS build_frontend
RUN corepack enable
COPY ./Frontend /home/node/src
WORKDIR /home/node/src
RUN pnpm i --frozen-lockfile
RUN pnpm build --outDir ./dist

FROM eclipse-temurin:23-jdk-alpine AS build_backend
COPY . /home/gradle/src
COPY --from=build_frontend /home/node/src/dist /home/gradle/src/Backend/bootstrap/src/main/resources/static
WORKDIR /home/gradle/src
RUN ./gradlew shadowJar --no-daemon

FROM eclipse-temurin:23-jre-alpine
RUN apk add --no-cache argon2-libs
EXPOSE 8080:8080
RUN mkdir /app
COPY --from=build_backend /home/gradle/src/Backend/bootstrap/build/libs/*.jar /app/jasstracker.jar
ENTRYPOINT ["java","-jar","/app/jasstracker.jar"]
