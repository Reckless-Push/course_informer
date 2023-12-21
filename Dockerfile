# Stage 1: Build the React app
FROM node:20.10-slim as react-build
ARG NEXT_PUBLIC_BASE_URL
ENV NEXT_PUBLIC_BASE_URL=${NEXT_PUBLIC_BASE_URL}
WORKDIR /app
# Cache dependencies by copying package files first
COPY my-app/package.json my-app/package-lock.json ./
RUN npm install
COPY my-app/. ./
RUN npm run build

# Stage 2: Cache Gradle dependencies
FROM amazoncorretto:21 as gradle-cache
WORKDIR /cache
# Copy gradle files first to leverage Docker cache
COPY gradlew gradle.properties diktat-analysis.yml ./
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts ./
# Cache dependencies
RUN ./gradlew --no-daemon dependencies

# Stage 3: Build the Ktor app
FROM amazoncorretto:21 as ktor-build
# Use ARG for build-time variables
ARG KEY_ALIAS
ARG PRIVATE_KEY_PASSWORD
ARG KEYSTORE_PASSWORD
ARG GOOGLE_CLIENT_ID
ARG GOOGLE_CLIENT_SECRET
ARG JDBC_H2_URL
ARG JDBC_H2_DRIVER
ARG JDBC_DATABASE_URL
ARG JDBC_POSTGRES_DRIVER
ARG BASE_URL
ARG KEYSTORE_URL
ARG IS_PROD

# Set ENV for runtime variables
ENV GOOGLE_CLIENT_ID=${GOOGLE_CLIENT_ID}
ENV GOOGLE_CLIENT_SECRET=${GOOGLE_CLIENT_SECRET}
ENV JDBC_URL=${JDBC_H2_URL}
ENV JDBC_DRIVER=${JDBC_H2_DRIVER}
ENV BASE_URL=${BASE_URL}
ENV KEYSTORE_URL=${KEYSTORE_URL}
ENV IS_PROD=${IS_PROD}

WORKDIR /build
# Copy only required files for gradle build
COPY gradlew gradle.properties diktat-analysis.yml ./
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts ./
COPY --from=gradle-cache /root/.gradle /root/.gradle
COPY src src
COPY documentation ./src/main/resources/documentation
# Keystore generation
RUN if [ "${IS_PROD}" = "true" ] ;  \
    then curl -L -o keystore.jks "${KEYSTORE_URL}";  \
    else keytool -keystore keystore.jks -alias ${KEY_ALIAS} -genkeypair -keyalg RSA -keysize 4096 -validity 3 \
    -dname 'CN=localhost, OU=ktor, O=ktor, L=Unspecified, ST=Unspecified, C=US' -storepass ${KEYSTORE_PASSWORD} \
    -keypass ${PRIVATE_KEY_PASSWORD}; fi
RUN cp keystore.jks src/main/resources/keystore.jks
COPY --from=react-build /app/out /build/src/main/resources/static
RUN ./gradlew clean test
ENV JDBC_URL=${JDBC_DATABASE_URL}
ENV JDBC_DRIVER=${JDBC_POSTGRES_DRIVER}
RUN ./gradlew clean build -x test

# Stage 4: Create the final image to run the server
FROM amazonlinux:2023
# Install JRE and other dependencies
RUN yum update -y && \
    yum install -y java-21-amazon-corretto-headless python311 shadow-utils && \
    yum clean all
# Download pip
RUN curl -O https://bootstrap.pypa.io/get-pip.py

# Switch to a non-root user to run the app
RUN useradd -m myuser
USER myuser
# Install pip and fitz
RUN python3 get-pip.py && \
    python3 -m pip install --upgrade pymupdf
WORKDIR /app

# Copy the built JAR and keystore from the Ktor build stage
COPY --from=ktor-build /build/build/libs/course-informer-all.jar ./
COPY --from=ktor-build /build/src/main/resources/keystore.jks ./
COPY extractor/extractor.py ./

EXPOSE 8080 8443 80 443
CMD ["java", "-jar", "course-informer-all.jar"]

# Healthcheck
HEALTHCHECK --interval=30s --timeout=30s --start-period=5s --retries=3 \
  CMD curl -f -k https://localhost:8443/health || exit 1
