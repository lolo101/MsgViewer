#!/bin/sh

##################################
## OSX Packager for MSG Viewer
## 
## This requires JDK 14 or higher
##################################

echo "Building app.."
cd ..
./mvnw package

echo "Packaging DMG.." 
cd -
jpackage --type dmg \
         -i ../MSGViewer/target \
         -n "MSG Viewer" \
         --icon "./msg-viewer.icns" \
         --main-class net.sourceforge.MSGViewer.ModuleLauncher \
         --main-jar msgviewer.jar

echo "Moving to distribution folder.."
mv *.dmg ../dist

echo "packaging complete."
