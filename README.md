# Chatroom-Server-Client-

This chat room is made using socket and port and their basic concepts in Java. This chat room needs at least two clients to work. Its mechanism is that every message goes to the server through every client, and the server is responsible for transferring between running threads.
In this chat, I used thread because the messages should be updated online and shown to each user that this process is done by overriding the run method and the start command on the server calls this method.
