FROM node:22-alpine AS build_frontend
RUN corepack enable
COPY ./Frontend /home/node/src
WORKDIR /home/node/src
RUN pnpm i --frozen-lockfile
RUN pnpm build --outDir ./dist

FROM ghcr.io/graalvm/jdk-community:23 AS build_backend
COPY . /home/gradle/src
COPY --from=build_frontend /home/node/src/dist /home/gradle/src/Backend/bootstrap/src/main/resources/static
WORKDIR /home/gradle/src
RUN ./gradlew shadowJar --no-daemon

FROM ghcr.io/graalvm/jdk-community:23
EXPOSE 8080:8080
COPY --from=build_backend /home/gradle/src/Backend/bootstrap/build/libs/*.jar /app/jasstracker.jar
ENTRYPOINT ["java","-jar","/app/jasstracker.jar"]
