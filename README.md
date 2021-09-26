#VNCScreenShare

install project with maven from the VNCScreenShare folder 

`$ mvn install`

##ServerApplication

To be able to properly use server application install ***x11vnc***

`$ sudo apt-get install x11vnc`

Then execute app with 

`$ mvn exec:java -pl server -Dexec.mainClass=ServerApplication`

##ClientApplication

To be able to properly use client application install ***xtightvncviewer***

`$ sudo apt-get install xtightvncviewer`

Then execute with

`$ mvn exec:java -pl client -Dexec.mainClass=ClientApplication`