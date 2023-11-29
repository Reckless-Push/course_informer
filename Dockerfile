# Stage 1: Build the React app
FROM node:lts-slim as react-build
WORKDIR /app
COPY my-app/package.json my-app/package-lock.json ./
RUN npm install
COPY my-app/. ./
RUN npm run build && rm -rf node_modules

# Stage 2: Build the Ktor app
FROM amazoncorretto:17-alpine-jdk as ktor-build
ARG KEY_ALIAS
ARG PRIVATE_KEY_PASSWORD
ARG KEYSTORE_PASSWORD

WORKDIR /build
# Copy the Ktor source code
COPY src/. ./src/.
COPY gradle/ ./gradle/.
COPY build.gradle.kts ./build.gradle.kts
COPY settings.gradle.kts ./settings.gradle.kts
COPY gradle.properties ./gradle.properties
COPY diktat-analysis.yml ./diktat-analysis.yml
COPY gradlew ./gradlew
# Create the keystore if it doesn't exist and copy it to the resources folder
RUN keytool -keystore keystore.jks -alias ${KEY_ALIAS} -genkeypair -keyalg RSA -keysize 4096 -validity 3  \
    -dname 'CN=localhost, OU=ktor, O=ktor, L=Unspecified, ST=Unspecified, C=US' -storepass ${KEYSTORE_PASSWORD}  \
    -keypass ${PRIVATE_KEY_PASSWORD} &&  \
    keytool -importkeystore -srckeystore keystore.jks -destkeystore keystore.p12 -srcstoretype JKS  \
    -deststoretype PKCS12 -srcstorepass ${KEYSTORE_PASSWORD} -deststorepass ${KEYSTORE_PASSWORD}
RUN cp keystore.jks src/main/resources/keystore.jks && \
    mkdir -p src/main/resources/cert && \
    cp keystore.p12 src/main/resources/cert/keystore.p12
# Copy the React app build from the previous stage
COPY --from=react-build /app/out src/main/resources/static
# Build the dcocumentation and the JAR
RUN ./gradlew dokkaHtml
RUN cp -r documentation/. src/main/resources/
RUN ./gradlew build && ./gradlew buildFatJar

# Stage 3: Create the final image to run the server
FROM public.ecr.aws/amazonlinux/amazonlinux:2023-minimal
# Install JRE (Runtime Environment)
RUN microdnf update -y && \
    microdnf install -y java-17-amazon-corretto-headless && \
    microdnf clean all
# Switch to a non-root user to run the app
USER nobody
WORKDIR /app
# Copy the built JAR from the Ktor build stage
COPY --from=ktor-build /build/build/libs/course-informer-all.jar /app/
EXPOSE 8080 8443
CMD ["java", "-jar", "course-informer-all.jar"]
HEALTHCHECK --interval=30s --timeout=30s --start-period=5s --retries=3 \
  CMD curl -f https://localhost:8553/health || exit 1
