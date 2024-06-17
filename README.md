# Eaglercraft 1.12 source

[![Java CI with Gradle](https://github.com/catfoolyou/Eaglercraft-1.12/actions/workflows/gradle.yml/badge.svg)](https://github.com/catfoolyou/Eaglercraft-1.12/actions/workflows/gradle.yml)

This repo contains the source code of my Eaglercraft 1.12 project. This is a separate project from the javascript runtime, which is why there are two sepearate branches.
The source code for the javascript web runtime is available at the [teavm](https://github.com/catfoolyou/Eaglercraft-1.12/tree/teavm) branch (It is not yet finished, but you can check the commit history to see how far it's gotten).

For more information on the desktop client, see the [wiki](https://github.com/catfoolyou/Eaglercraft-1.12/wiki)

Because of the fact that a desktop client does not have the same limitations as a web client, the desktop runtime client comes with Optifine and Shadersmod built in. A full table comparing features between the web and desktop clients is available below. 

| Features implemented               | Desktop client | Web client |
|------------------------------------|:--------------:|:----------:|
| Ability to connect to 1.12 servers |       yes      |     yes    |
| 1.12 features                      |       yes      |     yes    |
| Websocket connections              |       no       |     yes    |
| TCP/IP connections                 |       yes      |     no     |
| Optifine                           |       yes      |     no     |
| Shadersmod                         |       yes      |     no     |

## Getting Started:

Prerequisites: Java 17 and JDK 17 or higher (preferrably OpenJDK)

### To compile the latest version of the client, on Windows:

1. Make sure you have at least Java 17 installed and added to your PATH
2. Download (clone) this repository to your computer
3. Run gradle's clean and build tasks `gradlew.bat clean build`, then look in build/distributions for the compiled runtime zip.

### To compile the latest version of the client, on Linux:

1. Make sure you have at least Java 17 installed
2. Download (clone) this repository to your computer
3. Open a terminal in the folder the repository was cloned to
4. Type `chmod +x gradlew` and hit enter
5. Run gradle's clean and build tasks `./gradlew clean build`, then look in build/distributions for the compiled runtime zip.

### Releases can be outdated! To get the latest working version compile this repo yourself

