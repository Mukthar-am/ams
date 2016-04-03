
AMS (Ahmed's Messaging server) Server-Client components:
---------------------------------------------------------
The messaging server is designed with two components here
1. Messaging server (designed as thread class)
	- Uses socket connection with port=6061 at localhost as default but can be configured to custom configurations by the user at the start time.

2. ChatClient client class (Designed as a thread class)
	- Uses socket connection with port=6061 with host=localhost as default but can be configured to custom configrations by the user at the start and instance time.
	- Chat client class has a main method to span multiple client instances.


Helping to Release the port if already in use:
----------------------------------------------
MacBook-Pro-2:ams mukthar.ahmed$ lsof -i :6061
COMMAND   PID          USER   FD   TYPE            DEVICE SIZE/OFF NODE NAME
java    19263 mukthar.ahmed   26u  IPv6 0x95831c7926fbe45      0t0  TCP *:6061 (LISTEN)

MacBook-Pro-2:ams mukthar.ahmed$ kill -9 19263


Edge case coverage discussion:
------------------------------
1. Maintaining persistent socket connection with the chat client till the end of ther user singnal
	- Destination Id = "user name" or "user screen name"

2. Establishing a threaded communication with the end user and few automated responses hard coded at the server side, directly and only routed to the end user rather than publishing to all connected user. 

3. Vertical scalability
	- Vertical scalability has been thought through here by using a server class in the form of thread and server-thread spawned for each client when individual clients try to connect the the messaging server. 

4. Horizontal scalability
	- This can be thought through, by implementing RADIC implementation of socket servers which will have redundent arrays of messaging server nodes, where the call will be routed or handed over to the other live node. 

5. Chat client, simulation is implemented using JFrame (Didn't know much about it, but did a quick research and got it implemented over night). Client prompts for a chat user name and establishes a 1-1 connection to the server. 

6. When does chat or server-to-communication ends?
	- When user types in "bye" (case insensitive), the server terminates the session/thread with a appropriate return greetings. 

7. Chat server and client start process.
	- Port communication is implemented either by a default port 6061 or user defined port and same is the case of chat client which either works on the default coded port or the user defined port

	
