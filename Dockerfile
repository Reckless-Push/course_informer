# Stage 1: Build the React app
FROM node:latest as react-build
WORKDIR /app
COPY my-app/package.json my-app/package-lock.json ./
RUN npm install
COPY my-app/ ./
RUN npm run build

# Stage 2: Build the Ktor app
FROM amazonlinux:2 as ktor-build
# Install OpenJDK 11 to build the Ktor project
RUN yum update -y && \
    yum install -y java-17-amazon-corretto-devel && \
    yum clean all
ENV JAVA_HOME=/usr/lib/jvm/java-17-amazon-corretto
ENV PATH="$JAVA_HOME/bin:${PATH}"
WORKDIR /build
COPY . /build/
# Copy the React app build from the previous stage
COPY --from=react-build /app/out/. /build/course-informer/src/main/resources/static
RUN ./gradlew build && ./gradlew buildFatJar

# Stage 3: Create the final image to run the server
FROM amazonlinux:2
# Install JRE (Runtime Environment)
RUN yum update -y && \
    yum install -y java-17-amazon-corretto-headless && \
    yum clean all
WORKDIR /app
# Copy the built JAR from the Ktor build stage
COPY --from=ktor-build /build/build/libs/course-informer-all.jar /app/
EXPOSE 8080
CMD ["java", "-jar", "course-informer-all.jar"]
