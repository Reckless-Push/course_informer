# Course Informer

Course Informer is a server application that provides an API for retrieving information about courses at the University of Massachusetts Amherst.

## Prerequisites

Before you start, ensure you have the following installed:
- **Docker**: Version 24.0.7 or later recommended.
- **Git**: For source control management.
- **Operating System**: A Linux-based operating system is preferred. Windows users should use [WSL](https://learn.microsoft.com/en-us/windows/wsl/install).
- **Web Browser**: For accessing the App and API documentation. Firefox is recommended for self-signed certificates.

## Installation Instructions

**Step 1: Install Docker**
- Follow the instructions in [Docker's official documentation](https://docs.docker.com/get-docker/).
- Verify the installation: `docker --version`.

**Step 2: Install Git**
- Follow the instructions in [Git's official documentation](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git).
- Verify the installation: `git --version`.

**Step 3: Clone the Repository**
- Clone using: `git clone https://github.com/Reckless-Push/course_informer.git`.
- More about cloning: [GitHub's documentation](https://docs.github.com/articles/cloning-a-repository).

**Step 4: Prepare the Environment** No action required.

**Step 5: Build the Server**
- Build using Docker:
  ```
  docker build -t course-informer --build-arg="KEY_ALIAS=sampleAlias" --build-arg="PRIVATE_KEY_PASSWORD=samplePassword" --build-arg="KEYSTORE_PASSWORD=samplePassword" . 
  ```
- Replace `sampleAlias` with your `keystorePassword`, and `privateKeyPassword` with your chosen values.

**Step 6: Run the Server**
- Start the server:
  ```
  docker run -p 8080:8080 -p 8443:8443 -e KEY_ALIAS=sampleAlias -e KEYSTORE_PASSWORD=samplePassword -e PRIVATE_KEY_PASSWORD=samplePassword course-informer
  ```
- Replace `sampleAlias`, `keystorePassword`, and `privateKeyPassword` with your chosen values.
- Verify the server's operation by accessing `http://localhost:8080`.

**Step 7 (Optional): Download the self-signed certificate**
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
