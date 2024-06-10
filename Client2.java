
////////////////////////////////////////////////////////////////////////////////////////////////////////
/////                            I got help from geek for geeks                                    /////



import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client2 {
    // private classes for the clien
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String name;

    public Client2(Socket socket, String name){
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.name = name;
        }
        catch (IOException e){
            closeAll(socket, bufferedReader, bufferedWriter);
        }
    }

    public static void main(String[] args) throws IOException{
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your name");
        String name = scanner.nextLine();
        Socket socket = new Socket("localhost", 1234);
        Client2 client = new Client2(socket, name);
        client.readMessage();
        client.sendMessage();
    }

    public void sendMessage(){
        try{
            bufferedWriter.write(name);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            Scanner scanner = new Scanner(System.in);

            while(socket.isConnected()){
                String messageToSend = scanner.nextLine();
                bufferedWriter.write(name + " : " + messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();

            }
        } catch(IOException e){
            closeAll(socket, bufferedReader, bufferedWriter);

        }
    }
    public void readMessage(){
        new Thread( new Runnable() {
            @Override
            public void run() {
                String msfFromGroupChat;

                while(socket.isConnected()){
                    try{
                        msfFromGroupChat = bufferedReader.readLine();
                        System.out.println(msfFromGroupChat);
                    } catch (IOException e){
                        closeAll(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }
    public void closeAll(Socket socket, BufferedReader buffReader, BufferedWriter buffWriter){
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
