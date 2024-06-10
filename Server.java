
////////////////////////////////////////////////////////////////////////////////////////////////////////
/////                            I got help from geek for geeks                                    /////



import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    ////////////////////////////////////////////// create serverSocket class
    private ServerSocket serverSocket;

    /////////////////////////////////////////// constructor of ServerSocket class
    public Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void serverStart(){

        try{
            ///////////////////////////////////// check and loop the serverSocket
            while(!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                System.out.println("New Friend Connected");
                ClientHandler clientHandler = new ClientHandler(socket);

                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e){

        }
    }
    /////////////////////////////////////////close the server
    public void closerServer(){
        try{
            if(serverSocket != null){
                serverSocket.close();
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        server.serverStart();
    }
}





class ClientHandler  implements Runnable{

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    public Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String name;

    ///////////////////////////////////////////////constructor
    public ClientHandler(Socket socket){
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.name = bufferedReader.readLine();
            clientHandlers.add(this);
            boradcastMessage(name + "   has entered in the group");

        } catch(IOException e){
            closeAll(socket, bufferedReader, bufferedWriter);
        }
    }
    /////////////////////////////////// overriding run method of thread
    @Override
    public void run() {

        String messageFromClient;

        while(socket.isConnected()){
            try{
                messageFromClient = bufferedReader.readLine();
                boradcastMessage(messageFromClient);
            } catch(IOException e){
                closeAll(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }
    public void boradcastMessage(String messageToSend){
        for(ClientHandler clientHandler: clientHandlers){
            try{
                if(!clientHandler.name.equals(name)){
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            } catch(IOException e){
                closeAll(socket, bufferedReader, bufferedWriter);

            }
        }
    }
    public void removeClientHandler(){
        clientHandlers.remove(this);
    }

    public void closeAll(Socket socket, BufferedReader buffReader, BufferedWriter buffWriter){

        removeClientHandler();
        try{
            if(buffReader!= null){
                buffReader.close();
            }
            if(buffWriter != null){
                buffWriter.close();
            }
            if(socket != null){
                socket.close();
            }
        } catch (IOException e){
            e.getStackTrace();
        }

    }

}