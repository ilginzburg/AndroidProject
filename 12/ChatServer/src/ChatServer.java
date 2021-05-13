import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {

    List<Client> clients = new ArrayList<>();
    ServerSocket server;

    public ChatServer() throws IOException {
        // создаем серверный сокет на порту 1234
        this.server = new ServerSocket(1234);
    }

    public void sendAll(String msg){
        for(Client cl:clients)
            cl.receive(msg);
    }

    private void run(){
          while(true) {
              System.out.println("Waiting...");
              // ждем клиента из сети
              try {
                  Socket socket = server.accept();
                  System.out.println("Client connected!");
                  // создаем клиента на своей стороне
                  clients.add(new Client(socket,this));
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }

      }

    public static void main(String[] args) throws IOException {
        new ChatServer().run();
    }
}

