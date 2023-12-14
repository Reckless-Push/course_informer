# Stage 1: Build the React app
FROM node:20.10-slim as react-build
WORKDIR /app
COPY my-app/package.json my-app/package-lock.json ./
RUN npm install
COPY my-app/. ./
RUN npm run build

FROM amazoncorretto:21 as gradle-cache
WORKDIR /cache
COPY gradlew gradle.properties diktat-analysis.yml ./
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts ./
RUN ./gradlew --no-daemon dependencies

# Stage 2: Build the Ktor app
FROM amazoncorretto:21 as ktor-build
ARG KEY_ALIAS
ARG PRIVATE_KEY_PASSWORD
ARG KEYSTORE_PASSWORD
ARG GOOGLE_CLIENT_ID
ARG GOOGLE_CLIENT_SECRET
ARG JDBC_H2_URL
ARG JDBC_H2_DRIVER
ARG JDBC_DATABASE_URL
ARG JDBC_POSTGRES_DRIVER

ENV GOOGLE_CLIENT_ID=${GOOGLE_CLIENT_ID}
ENV GOOGLE_CLIENT_SECRET=${GOOGLE_CLIENT_SECRET}
ENV JDBC_URL=${JDBC_H2_URL}
ENV JDBC_DRIVER=${JDBC_H2_DRIVER}

WORKDIR /build
COPY gradlew gradle.properties diktat-analysis.yml ./
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts ./
COPY --from=gradle-cache /root/.gradle /root/.gradle
COPY src src
COPY documentation ./src/main/resources/documentation
RUN keytool -keystore keystore.jks -alias ${KEY_ALIAS} -genkeypair -keyalg RSA -keysize 4096 -validity 3  \
    -dname 'CN=localhost, OU=ktor, O=ktor, L=Unspecified, ST=Unspecified, C=US' -storepass ${KEYSTORE_PASSWORD}  \
    -keypass ${PRIVATE_KEY_PASSWORD} &&  \
    keytool -importkeystore -srckeystore keystore.jks -destkeystore keystore.p12 -srcstoretype JKS  \
    -deststoretype PKCS12 -srcstorepass ${KEYSTORE_PASSWORD} -deststorepass ${KEYSTORE_PASSWORD}
RUN cp keystore.jks src/main/resources/keystore.jks && \
    mkdir -p src/main/resources/cert && \
    cp keystore.p12 src/main/resources/cert/keystore.p12
COPY --from=react-build /app/out /build/src/main/resources/static
RUN ./gradlew clean test
ENV JDBC_URL=${JDBC_DATABASE_URL}
ENV JDBC_DRIVER=${JDBC_POSTGRES_DRIVER}
RUN ./gradlew clean build -x test

# Stage 3: Create the final image to run the server
FROM amazonlinux:2023
# Install JRE (Runtime Environment)
RUN yum update -y && \
    yum install -y java-21-amazon-corretto-headless && \
    yum install -y python && \
    yum install -y shadow-utils && \
    yum clean all
# Switch to a non-root user to run the app
RUN useradd -m myuser
USER myuser
RUN python -m ensurepip --upgrade
RUN python -m pip install --upgrade pymupdf
WORKDIR /app
# Copy the built JAR and keystore from the Ktor build stage
COPY --from=ktor-build /build/build/libs/course-informer-all.jar ./
COPY --from=ktor-build /build/keystore.jks ./
COPY extractor/extractor.py ./

EXPOSE 8080 8443
CMD ["java", "-jar", "course-informer-all.jar"]
HEALTHCHECK --interval=30s --timeout=30s --start-period=5s --retries=3 \
  CMD curl -f -k https://localhost:8443/health || exit 1
