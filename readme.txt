
Server-Client components:
-------------------------
The messaging server is designed with two components here
1. Messaging server (designed as thread class)
	- Uses socket connection with port=6061 at localhost as default but can be configured to custom configurations by the user at the start time.
2. ChatClient client class (Designed as a thread class)
	- Uses socket connection with port=6061 with host=localhost as default but can be configured to custom configrations by the user at the start and instance time.


Release the port:
-----------------
MacBook-Pro-2:ams mukthar.ahmed$ lsof -i :6061
COMMAND   PID          USER   FD   TYPE            DEVICE SIZE/OFF NODE NAME
java    19263 mukthar.ahmed   26u  IPv6 0x95831c7926fbe45      0t0  TCP *:6061 (LISTEN)

MacBook-Pro-2:ams mukthar.ahmed$ kill -9 19263


Edge case coverage discussion:
------------------------------
1. Maintaining persistent socket connection with the chat client till the end of ther user singnal
	- Destination Id = user name or user screen name

2. Routing designation server responses to the user

3. Vertical scalability
	-

4. Horizontal scalability
	-