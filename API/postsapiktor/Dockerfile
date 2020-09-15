FROM gradle:5.6.3 as gradle
COPY --chown=gradle . /home/app/posts
WORKDIR /home/app/posts
RUN gradle assemble --no-daemon

# Container in which to run the application
FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.6_10_openj9-0.18.1-alpine-slim

# Copy the jar from the builder container into the run container
COPY --from=gradle /home/app/posts/build/libs/postsapi-*-all.jar postsapi.jar
COPY --from=gradle /home/app/posts/build/resources/main/application.conf /resources/application.conf
COPY --from=gradle /home/app/posts/build/resources/main/credentials.json /resources/credentials.json
COPY --from=gradle /home/app/posts/build/resources/main/logback.xml /resources/logback.xml

# Remove from .gitignore credentials.json
ENV GOOGLE_APPLICATION_CREDENTIALS=resources/credentials.json

# Run the application
EXPOSE 8080
CMD ["java", "-server", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-XX:InitialRAMFraction=2", "-XX:MinRAMFraction=2", "-XX:MaxRAMFraction=2", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=100", "-XX:+UseStringDeduplication", "-Dserver.port=${PORT}", "-jar", "postsapi.jar"]
