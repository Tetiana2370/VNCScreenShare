#VNCScreenShare

install project with maven from the VNCScreenShare folder

`$ mvn install`

##ServerApplication

To be able to properly use server application install ***x11vnc***

`$ sudo apt-get install x11vnc`

Then execute app with

`$ mvn exec:java -pl server`

or

`$ java -jar server/target/server-1.0-SNAPSHOT.jar`

##ClientApplication

To be able to properly use server application install ***xtightvncviewer***

`$ sudo apt-get install xtightvncviewer`

Then execute with

`$ mvn exec:java -pl client`

or 

`$ java -jar client/target/client-1.0-SNAPSHOT.jar`