# Table of Contents
1. [Course Informer](#course-informer)
2. [Prerequisites](#prerequisites)
3. [Development Build Instructions](#development-build-instructions)
   - [Dev Step 1: Install Docker](#dev-step-1-install-docker)
   - [Dev Step 2: Install Git](#dev-step-2-install-git)
   - [Dev Step 3: Clone the Repository](#dev-step-3-clone-the-repository)
   - [Dev Step 4: Prepare the Environment](#dev-step-4-prepare-the-environment)
   - [Dev Step 5: Build and run the Server](#dev-step-5-build-and-run-the-server)
4. [Production Deployment](#production-deployment)
   - [Prod Step 1: Build Production Image](#prod-step-1-build-production-image)
   - [Prod Step 2: Set up a Docker Hub Repository](#prod-step-2-set-up-a-docker-hub-repository)
   - [Prod Step 3: Login to Docker Hub](#prod-step-3-login-to-docker-hub)
   - [Prod Step 4: Push Production Image](#prod-step-4-push-production-image)
   - [Prod Step 5: Install JDK](#prod-step-5-install-jdk)
   - [Prod Step 2: Setup a domain](#prod-step-6-setup-a-domain)
   - [Prod Step 3: Set up a keystore](#prod-step-7-set-up-a-keystore)
   - [Prod Step 4: Host the keystore remotely](#prod-step-8-host-the-keystore-remotely)
   - [Prod Step 5: Connect to the server](#prod-step-9-connect-to-the-server)
   - [Prod Step 6: Install Git](#prod-step-10-install-git)
   - [Prod Step 7: Clone the Repository](#prod-step-11-clone-the-repository)
   - [Prod Step 8: Prepare the Environment](#prod-step-12-prepare-the-environment)
   - [Prod Step 9: Install Docker](#prod-step-13-install-docker)
   - [Prod Step 10: Build and run the Server](#prod-step-14-build-and-run-the-server)
5. [Usage](#usage)
6. [Troubleshooting](#troubleshooting)
7. [Getting Help](#getting-help)

# Course Informer

Course Informer is a server application that provides an API for retrieving information about courses at 
the University of Massachusetts Amherst.

## Prerequisites

Before you start, ensure you have the following installed:
- **Docker**: Version 24.0.7 or later recommended.
- **Docker Compose**: Version 2.23.0 or later recommended. Docker Compose is included with Docker Desktop.
- **Git**: For cloning the repository. You can also download the repository as a ZIP file.
- **Operating System**: A Linux-based operating system is preferred. Windows users should use 
[WSL](https://learn.microsoft.com/en-us/windows/wsl/install).
- **Web Browser**: For accessing the Index and API documentation.

## Development Build Instructions

### Dev Step 1: Install Docker
- Follow the instructions in [Docker's official documentation](https://docs.docker.com/desktop/).
- Verify the installation: `docker --version`  and `docker compose version`.

### Dev Step 2: Install Git
- Follow the instructions in 
[Git's official documentation](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git).
- Verify the installation: `git --version`.

### Dev Step 3: Clone the Repository
- Clone using: `git clone https://github.com/Reckless-Push/course_informer.git`.
- More about cloning: [GitHub's documentation](https://docs.github.com/articles/cloning-a-repository).

### Dev Step 4: Prepare the Environment
- Navigate to the repository: `cd course_informer`.
- Copy the '.env.template' file to '.env': `cp .env.template .env`.
- Edit the '.env' file and replace the values with your own:
  - `KEY_ALIAS`: The alias of the key in the keystore.
  - `KEYSTORE_PASSWORD`: The password for the keystore.
  - `PRIVATE_KEY_PASSWORD`: The password for the private key.
  - `GOOGLE_CLIENT_ID`: The client ID for Google OAuth.
  - `GOOGLE_CLIENT_SECRET`: The client secret for Google OAuth.
  - `JDBC_DATABASE_URL`: The JDBC URL for the prod database.
  - `JDBC_H2_URL`: The JDBC URL for the H2 test database.
  - `JDBC_H2_DRIVER`: The JDBC driver for the H2 test database.
  - `JDBC_POSTGRES_DRIVER`: The JDBC driver for the prod database.
  - `POSTGRES_USER`: The username for the prod database.
  - `POSTGRES_PASSWORD`: The password for the prod database.
  - `BASE_URL`: URL of the server
  - `NEXT_PUBLIC_BASE_URL`: URL for the server passed to NEXT build
  - `IS_PROD`: Flag for production environment, should be 'false' for local builds
- More about environment variables: 
[Docker's documentation](https://docs.docker.com/compose/environment-variables/env-file/).
- If you do not have a Google OAuth client ID and secret, you can create one 
by following the instructions in [Google's documentation](https://developers.google.com/identity/protocols/oauth2).

### Dev Step 5: Build and run the Server
- Build using Docker Compose
  ```
  docker compose up --build
  ```
- Verify the server's operation by accessing `https://localhost:8443`.


## Production Deployment
### Prod step 1: Build Production Image
- Follow steps 1-4 of the [development build instructions](#development-build-instructions).
- Replace the `.env` file variables with their production counterparts. 
- Build using Docker Compose
  ```
  docker compose build
  ```

### Prod step 2: Set up a Docker Hub Repository
- Create a [Docker Hub](https://hub.docker.com/) account.
- Create a new repository.
- Take note of the repository name as we will need it in the next step.

### Prod step 3: Login to Docker Hub
- Login to Docker Hub using the following command:
  ```
  docker login --username <user name>
  ```
- Enter your password when prompted.

### Prod step 4: Push Production Image
- Tag the image with the name of your repository
  ```
  docker tag <image id> <user name>/<repository name>:tag
  ```
- Push the image to the repository
  ```
    docker push <user name>/<repository name>:tag
  ```

### Prod Step 5: Install JDK
- To generate the keystore you must have keytool installed on your local machine.
- You can follow the
[Oracle JDK installation instructions](https://docs.oracle.com/en/java/javase/18/install/overview-jdk-installation.html)
 for your platform.

### Prod Step 6: Setup a domain
- Purchase a domain name, a free .live domain can be purchased through the 
[GitHub student developer pack and name.com](https://www.name.com/partner/github-students)
- Purchase a headless server with root and ssh access, you can add a 
[Digital Ocean Droplet](https://www.digitalocean.com/products/droplets) to your name.com account for $6 per month.
- Take note of the servers IPV4 and IPV6 addresses, you will need them in the next step.
- Set up the DNS records to point your domain to the server. The schema should be as follows:
  - A record: `@` -> `server ipv4 address`
  - A record: `www` -> `server ipv4 address`
  - AAAA record: `@` -> `server ipv6 address`
  - AAAA record: `www` -> `server ipv6 address`

### Prod Step 7: Set up a keystore
- Windows Users:
  - You may need to run keytool from `C:\Program Files\Java\jdk_version\bin`
- Generate a keystore using the following command:
  ```
  keytool -genkeypair -alias <alias> -keyalg RSA -keysize 4096 -storetype PKCS12 -keystore <keystore name>.jks
  ```
- Enter the keystore password when prompted.
- Extract the CSR from the keystore using the following command:
  ```
  keytool -certreq -alias <alias> -keyalg RSA -file <csr name>.csr -keystore <keystore name>.jks
  ```
- Submit the CSR to a certificate authority of your choice. 
The certificate authority will provide you with a certificate.
- Import the root certificate into the keystore using the following command:
  ```
  keytool -import -trustcacerts -alias root -file <root_certificate>.crt -keystore <keystore name>.jks
  ```
- Import the intermediate certificates
  ```
  keytool -import -trustcacerts -alias intermediate -file <intermediate_certificate>.crt -keystore <keystore name>.jks
  ```
- Import the signed certificate
  ```
  keytool -import -trustcacerts -alias <alias> -file <signed_certificate>.crt -keystore <keystore name>.jks
  ```
- Verify the Keystore
  ```
  keytool -list -v -keystore <keystore name>.jks
  ```

### Prod Step 8: Host the keystore remotely
- Upload this keystore to a private URL like an S3 bucket or Cloud Storage
- Note the direct download URL as we will need it in the next step. 
Here are the [instructions for Google Drive](https://gist.github.com/tanaikech/f0f2d122e05bf5f971611258c22c110f)

### Prod Step 9: Connect to the server
- Connect to your server via SSH. You can learn the basics in the 
[Digital Ocean SSH Essentials guide](https://www.digitalocean.com/community/tutorials/ssh-essentials-working-with-ssh-servers-clients-and-keys)

### Prod Step 10: Install Git
- If not already installed, follow the instructions in 
[Git's official documentation](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git).
- Verify the installation: `git --version`.

### Prod Step 11: Clone the Repository
- Clone using: `git clone https://github.com/Reckless-Push/course_informer.git`.
- More about cloning: [GitHub's documentation](https://docs.github.com/articles/cloning-a-repository).

### Prod Step 12: Prepare the Environment
- Navigate to the repository: `cd course_informer`.
- Copy the '.env.template' file to '.env': `cp .env.template .env`.
- Edit the '.env' file and replace the values with your own:
  - `KEY_ALIAS`: The alias of the key in the keystore.
  - `KEYSTORE_PASSWORD`: The password for the keystore.
  - `PRIVATE_KEY_PASSWORD`: The password for the private key.
  - `GOOGLE_CLIENT_ID`: The client ID for Google OAuth.
  - `GOOGLE_CLIENT_SECRET`: The client secret for Google OAuth.
  - `JDBC_DATABASE_URL`: The JDBC URL for the prod database.
  - `JDBC_H2_URL`: The JDBC URL for the H2 test database.
  - `JDBC_H2_DRIVER`: The JDBC driver for the H2 test database.
  - `JDBC_POSTGRES_DRIVER`: The JDBC driver for the prod database.
  - `POSTGRES_USER`: The username for the prod database.
  - `POSTGRES_PASSWORD`: The password for the prod database.
  - `DOCKER_IMAGE`: The name of the Docker image for prod compose.
  - `BASE_URL`: URL of the server
  - `NEXT_PUBLIC_BASE_URL`: URL for the server passed to NEXT build
  - `KEYSTORE_URL`: Remote URL for the keystore file, must be signed by an authority
  - `IS_PROD`: Flag for production environment, should be 'false' for local builds
- More about environment variables: 
[Docker's documentation](https://docs.docker.com/compose/environment-variables/env-file/).
- If you do not have a Google OAuth client ID and secret, you can create one by following the instructions in 
[Google's documentation](https://developers.google.com/identity/protocols/oauth2).

### Prod Step 13: Install Docker
- If you're deploying in a headless Ubuntu 22.04 environment the
  [Digital Ocean Docker instructions](https://www.digitalocean.com/community/tutorials/how-to-install-and-use-docker-on-ubuntu-22-04)
 are good place to start.
- Verify the installation: `docker --version` and `docker compose version`.

### Prod Step 14: Build and run the Server
- Build using Docker Compose
  ```
  docker compose -f docker-compose.prod.yml up
  ```
- Verify the server's operation by accessing `https://yourdomain.tld` from your local browser.


## Usage

For API usage instructions, refer to the [documentation](documentation) directory. Alternatively, you can access 
the documentation by accessing `https://localhost:8443/html/index.html` after starting the server.


## Troubleshooting

- Run `docker ps` to see if the container is running. If it is not, run `docker logs <container id>` to see the logs.
- If the logs indicate that the server failed to start, ensure that the keystore was generated correctly and that 
the passwords are correct.
- If the logs indicate that the server started successfully, ensure that the ports are correct and that the
 server is accessible.

## Getting Help

If you encounter issues or require further assistance, please submit a query in the 
[issue tracker](https://github.com/Reckless-Push/course_informer/issues).
