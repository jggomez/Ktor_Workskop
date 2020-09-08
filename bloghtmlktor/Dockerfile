FROM gradle:5.6.3 as gradle
COPY --chown=gradle . /home/app/blog
WORKDIR /home/app/blog
RUN gradle assemble --no-daemon

# Container in which to run the application
FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.6_10_openj9-0.18.1-alpine-slim

# Copy the jar from the builder container into the run container
COPY --from=gradle /home/app/blog/build/libs/blog-*-all.jar blog.jar
COPY --from=gradle /home/app/blog/build/resources/main/application.conf /resources/application.conf
COPY --from=gradle /home/app/blog/build/resources/main/logback.xml /resources/logback.xml
COPY --from=gradle /home/app/blog/build/resources/main/static/styles.css /resources/static/styles.css
COPY --from=gradle /home/app/blog/build/resources/main/templates/index.ftl /resources/templates/index.ftl

# Run the application
EXPOSE 8080
CMD ["java", "-server", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-XX:InitialRAMFraction=2", "-XX:MinRAMFraction=2", "-XX:MaxRAMFraction=2", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=100", "-XX:+UseStringDeduplication", "-Dserver.port=${PORT}", "-jar", "blog.jar"]
