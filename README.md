# Eaglercraft 1.12 source

[![Java CI with Gradle](https://github.com/catfoolyou/Eaglercraft-1.12/actions/workflows/gradle.yml/badge.svg)](https://github.com/catfoolyou/Eaglercraft-1.12/actions/workflows/gradle.yml)

This repo contains the source code of my Eaglercraft 1.12 project. While I have made a somewhat functioning version of a desktop runtime, the web version has not yet been compiled. Please be patient as I expect to have a working build soon.
This client comes with Optifine build in.

### Compiling

Prerequisites: Java 17 and JDK 17 or higher

Download this project and run gradle's clean and build tasks (`./gradlew clean build` on Linux and `gradlew.bat clean build` on Windows), then look in build/distributions for the compiled runtime zip.

#### Releases can be outdated! To get the latest working version compile this repo yourself

### Web client
As of right now there is no system to compile an offline download, neither is there a web client.

### Web server
I have implemented a web server based on Lax1dude's EaglerXBungee plugin and Ayunami2000's replit, you can run it from a webserver by downloading the server folder of this repo and running it on Gitpod or Replit.

## Pros & Cons

Pros:
- Its a really slimmed down version of the 1.12 client that works
- ITS FREE!
- It has Optifine

Cons:
- No web or offline web client or download
- It's pirated
- No dedicated GPU support on Windows (yet)
