#!/bin/bash
if [ -z "$1" ]; then 
    echo ''
fi
if [ ! -d "jardir" ]; then  
    mkdir jardir
fi
unzip -o AdPluto.jar -d jardir
if [ -f "config.json" ]; then
    cp -f config.json jardir/
    cd jardir
    jar cvfM ../$1.jar  * META-INF/MANIFEST.MF
fi  
