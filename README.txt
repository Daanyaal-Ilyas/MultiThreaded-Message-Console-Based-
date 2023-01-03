To Run the Chat you will need to make sure you have the Google Json Libary Which is Google Gson 
TO install json do the following 

-------------------------------------------------------------------------------------
Open the Project in Intellij
-------------------------------------------------------------------------------------
open Intellij
Click open 
Find the Folder and open it 
It will say Project JDK is not defined and it will say Setup SDK
Make sure you setup the SDK
SDK should be 19 Oracle OpenJDK version 19.0.1
Now you just need to install Google Gson/Json Follow the next steps 
---------------------------------
If you still need install the sdk 
------------------------------------
Click on File 
Click on Project Structure 
click on Project 
SDK Select 19 Oracle OpenJDK version
Now you just need to install Google Gson/Json Follow the next steps 
-------------------------------------------------------------------------------------
ADD Google Json
------------------------------------------------------------------------------------
Click on File 
Click on Project Structure 
Click on Libraries 
at the top click the + 
Then Click From Maven
Search  Google Gson 
Pick com.google.api.client.gson:2.0.0 then click ok and then ok again  and then apply and then ok 
Now Your Server and Client Should work 
----------------------------------------------------------------------------------------------------------
(the jave files are in \src\Chat)

First run the server 
Right Click on the server.java and click Run Server.Main()
once your server is running it will say Server started on Port 12345
Now start the client 
Right Click on the client.java and click Run client.Main()
if you want to run more then one client 
Right Click on the client.java and click Run client.Main() again and your other client will be running 

For Client to quit the program you can do /quit


Video Presentation :
https://youtu.be/szV2BFW9UqM