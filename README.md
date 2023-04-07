[![CircleCI](https://circleci.com/gh/lolo101/MsgViewer.svg?style=shield)](https://app.circleci.com/pipelines/github/lolo101/MsgViewer)
[![Build Status](https://travis-ci.com/lolo101/MsgViewer.svg?branch=master)](https://travis-ci.com/lolo101/MsgViewer)
[![CodeScene general](https://codescene.io/images/analyzed-by-codescene-badge.svg)](https://codescene.io/projects/2821)

# msgviewer

This project is a fork of https://sourceforge.net/projects/msgviewer

It has been Mavenized, and support for .eml to .msg has been added.

## Build

In order to build this project, make sure to have JDK 11+ installed, then clone the project sources:

`git clone https://github.com/lolo101/MsgViewer.git`

cd to the project directory:

`cd MsgViewer`

then run the Maven build:

`./mvnw package`

Building will generate a number of files. The main file is an 'uber-jar' placed under **MSGViewer/target** directory.

## GUI

You can run the application's Graphic User Interface by calling :

```
cd MSGViewer/target
java -jar msgviewer.jar
```

## Command Line

The application may also be used on the command line. Just type :

```
cd MSGViewer/target
java -jar msgviewer.jar -h
```

to display command line help.
