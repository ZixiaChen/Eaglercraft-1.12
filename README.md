# Eaglercraft 1.12 source

[![Java CI with Gradle](https://github.com/catfoolyou/Eaglercraft-1.12/actions/workflows/gradle.yml/badge.svg)](https://github.com/catfoolyou/Eaglercraft-1.12/actions/workflows/gradle.yml)

This repo contains the source code of my Eaglercraft 1.12 project. While I have made a somewhat functioning version of a desktop runtime, the web version has not yet been compiled (And will not be any time soon).
This client comes with Optifine built in.


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

## Singleplayer

Singleplayer worlds are located in the `saves` folder.

You can also import and export your existing vanilla Minecraft 1.12 worlds into Eaglercraft by copying the world folders into the `saves` folder in your compiled desktop runtime. Beware that the inventories of LAN world players are not saved when the world is converted to vanilla, and pets (dogs, cats, horses, etc) might sometimes forget their owners due to the UUID changes.

## Multiplayer

### LAN Worlds

LAN is currently not supported, to start a localhost game you have to start the server inside the `server` folder (See the Making a server section.). Note that this only works on Linux. EaglercraftX clients can connect to this server.

### Connecting to a server

Eaglercraft 1.12 can connect to any cracked minecraft server that does not require authentication. The 1.12 client can play on any existing Eaglercraft 1.5.2 or 1.8.8 servers.

Eaglercraft 1.12 DOES NOT yet support websocket `wss://` connections! When connecting to an Eaglercraft server, you CANNOT have `wss://` in the IP or IT WILL NOT WORK!!

### Making a Server

To make a server for Eaglercraft 1.12 there is a modified version of EaglercraftXBungee ("EaglerXBungee") which is included in this repository in the `server` folder, though its Linux-only. To run a server on Windows use the vanilla 1.12 server jar. Existing EaglercraftX clients CANNOT connect to neither the vanilla server nor the modified EaglerXBungee, please keep that in mind.
If you want to have a server accessible for both Eaglercraft 1.12 clients and EaglercraftX clients, please use Lax1dude's unmodified EaglerXBungee server.

When connecting from EaglercraftX, you MUST use and offline download and use `ws://` instead of `wss://` in the IP or it will not connect.

### Web client
As of right now there is no system to compile an offline download, neither is there a web client. I have recently begun working on the web client, look at the teavm branch for progress.

