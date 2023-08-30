FROM maven:3.9.3-eclipse-temurin-17-focal

# Create and set a working directory for our app; would expect this to be bound to the cwd
RUN mkdir -p /app
VOLUME /app
WORKDIR /app

# Default is to run verify lifecycle with coverage profile
CMD [ "mvn", "clean", "verify", "-P coverage", "-Dstyle.color=always" ]
