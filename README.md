[![Build Status](https://travis-ci.org/lolo101/MsgViewer.svg?branch=master)](https://travis-ci.org/lolo101/MsgViewer)
[![Codeship Status for lolo101/MsgViewer](https://app.codeship.com/projects/52c429a0-01fe-0135-ab0f-5a532b9c76c0/status?branch=master)](https://app.codeship.com/projects/213000)
[![CodeScene Code Health](https://codescene.io/projects/2821/status-badges/code-health)](https://codescene.io/projects/2821)

# msgviewer

This project is a fork of https://sourceforge.net/projects/msgviewer

It has been Mavenized, and support for .eml to .msg has been added.

## Build

In order to build this project, make sure to have Maven and JDK 11+ installed, then clone the project sources:

`git clone https://github.com/lolo101/MsgViewer.git`

`cd` to the sources directory:

`cd MsgViewer`

At last, run the Maven build:

`mvn package`

The build will generate a number of files. The main file is a 'uber-jar' placed under **MSGViewer/target** directory.

You can now run the project with:

```
cd MSGViewer/target
java -jar msgviewer.jar
```

## GUI

You can run the application's Graphic User Interface by calling :

`java -jar msgviewer.jar`

## Command Line

The application may also be used on the command line. Just type :

`java -jar msgviewer.jar -h`

to display command line help.
