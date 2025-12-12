# ndaPrototypeApp
Final project for CS 1735 Software Design Methodology for Fall 2025.  SRS and a prototype app developed in Android Studio are deliverables. Contributors: Justin Rhodes, Bilal Hassan and Seth Baker  


# Software Requirements Specification
[Click here to jump to the SRS](ndaAppSRS.pdf)  

The SRS was completed by myself, Justin Rhodes, and Bilal Hassan.  The SRS contains several different examples of types of requirements (user stories, use cases, wireflow, state diagram), as well as other important aspects of a requirements specification (functional and non-functional requirements, description and introduction).  


# Prototype App Created in Android Studio
[Click here to jump to the prototype's Android Studio project](ndaPrototype)  

I (Seth Baker) worked on the prototype and developed it in its entirety.  I worked with Justin and Bilal to ensure that the prototype followed the requirements and also satisfied important design requirements to ensure the best result.
The prototype simulates the main functions the final app would eventually implement.  It does this by utilizing both internal storage on the users Android device as well as read-only files stored within the project itself.  Upon full implementation, this file-based system would be replaced with a Database containing the NDAs and the user's account information, as well as being used to provide full functionality of user account creation.
The main functions contain: Sending a signed NDA to another user to be signed, Receiving an NDA sent to the user by another user and the option to either deny (with a message) the NDA or sign the NDA and make it legally binding, and Viewing NDAs (Inactive, active, and pending).  

[Click here to jump to the Kotlin source code files](ndaPrototype/app/src/main/java/com/example/myapp)
