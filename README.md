# Contact Tracing System

This project is a comprehensive contact tracing system designed to track potential contacts with individuals carrying a virus, aiding in the effort to control its spread. It comprises multiple applications for both individuals and medical personnel, along with various backend services for authentication, data storage, and communication.

## Features

- **Token Server**: Manages user tokens for authentication and identification.
- **Desktop Application for Individuals**: Allows individuals to log in, report locations, exchange messages with medical staff, and receive notifications about potential exposure to the virus.
- **Central Registry Application**: Used by medical staff to track registered individuals, view locations, communicate with individuals, and mark infection status.
- **SOAP Service**: Provides functionalities like system login, token listing, token existence check, and token deactivation.
- **REST Service**: Enables access to and updating of data about individuals' locations and types within the central registry.
- **Socket Server**: Facilitates secure text message exchange between individuals and medical staff, including group messaging among medical staff.
- **RMI Application**: Manages storage, display, and retrieval of user documents stored on a separate file server.
- **Multicast for Message Broadcasting**: Used for message broadcasting between medical staff.
- **Notification Storage**: All notifications are serialized onto the individual's file system.


## Usage

1. Individuals should start by logging in to the desktop application, obtaining a unique token from the token server.
2. Once authenticated, individuals can report their locations, communicate with medical staff, and receive notifications about potential virus exposure.
3. Medical staff use the central registry application to track individuals, communicate with them, and manage infection status.
4. Various backend services handle authentication, data storage, and communication, ensuring seamless operation of the system.
