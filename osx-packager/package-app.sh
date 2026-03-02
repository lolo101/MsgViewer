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
         --name "MSG Viewer" \
         --icon ./msg-viewer.icns \
         --input ../MSGViewer/target \
         --main-jar msgviewer.jar \
         --main-class net.sourceforge.MSGViewer.ModuleLauncher

echo "Moving to distribution folder.."
mkdir -p ../dist
mv *.dmg ../dist

echo "packaging complete."
