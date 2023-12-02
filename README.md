# Course Informer

Course Informer is a server application that provides an API for retrieving information about courses at the University of Massachusetts Amherst.

## Prerequisites

Before you start, ensure you have the following installed:
- **Docker**: Version 24.0.7 or later recommended. Installing via [Docker Desktop](https://docs.docker.com/desktop/) is recommended.
- **Docker Compose**: Version 2.23.0 or later recommended. Docker Compose is included with Docker Desktop.
- **Git**: For cloning the repository. You can also download the repository as a ZIP file.
- **Operating System**: A Linux-based operating system is preferred. Windows users should use [WSL](https://learn.microsoft.com/en-us/windows/wsl/install).
- **Web Browser**: For accessing the Index and API documentation. [Firefox](https://www.mozilla.org/en-US/firefox/new/) is recommended for self-signed certificates.

## Installation Instructions

**Step 1: Install Docker**
- Follow the instructions in [Docker's official documentation](https://docs.docker.com/desktop/).
- Verify the installation: `docker --version`.

**Step 2: Install Git**
- Follow the instructions in [Git's official documentation](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git).
- Verify the installation: `git --version`.

**Step 3: Clone the Repository**
- Clone using: `git clone https://github.com/Reckless-Push/course_informer.git`.
- More about cloning: [GitHub's documentation](https://docs.github.com/articles/cloning-a-repository).

**Step 4: Prepare the Environment**
- Navigate to the repository: `cd course_informer`.
- Copy the '.env.template' file to '.env': `cp .env.template .env`.
- Edit the '.env' file and replace the values with your own:
  - `KEY_ALIAS`: The alias of the key in the keystore.
  - `KEYSTORE_PASSWORD`: The password for the keystore.
  - `PRIVATE_KEY_PASSWORD`: The password for the private key.
  - `GOOGLE_CLIENT_ID`: The client ID for Google OAuth.
  - `GOOGLE_CLIENT_SECRET`: The client secret for Google OAuth.
- More about environment variables: [Docker's documentation](https://docs.docker.com/compose/environment-variables/env-file/).
- If you do not have a Google OAuth client ID and secret, you can create one by following the instructions in [Google's documentation](https://developers.google.com/identity/protocols/oauth2).

**Step 5: Build and run the Server**
- Build using Docker Compose
  ```
  docker compose up --build
  ```
- Verify the server's operation by accessing `http://localhost:8080`.

**Step 6 (Optional): Download the self-signed certificate**
- After starting the server, you can download the self-signed certificate by accessing `http://localhost:8080/cert/keystore.p12`.
- The certificate can be imported into a web browser or other application to enable HTTPS.
- For Firefox:
  - Open the menu and select "Preferences".
  - Scroll to Privacy & Security.
  - Go to 'View Certificates' under 'Security'.
  - In the Certificate Manager, go to 'Your Certificates'.
  - Click 'Import' and select your .p12 file.
  - Enter the `keystorePassword` you used while building the image

## Usage

For API usage instructions, refer to the [documentation](documentation) directory. Alternatively, you can access the documentation by accessing `http://localhost:8080/html/index.html` after starting the server.

## Troubleshooting

- Run `docker ps` to see if the container is running. If it is not, run `docker logs <container id>` to see the logs.
- If the logs indicate that the server failed to start, ensure that the keystore was generated correctly and that the passwords are correct.
- If the logs indicate that the server started successfully, ensure that the ports are correct and that the server is accessible.

## Getting Help

If you encounter issues or require further assistance, please submit a query in the [issue tracker](https://github.com/Reckless-Push/course_informer/issues).
