/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifto.chat.blocking;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author rene
 */
public class ChatClient implements Runnable{

    public static final String SERVER_ADDRESS = "127.0.0.1";
    private ClientSocket clientSocket;
    private Scanner scanner;

    public ChatClient() {
        scanner = new Scanner(System.in);
    }

    public void start() throws IOException {
        try {
            clientSocket = new ClientSocket(new Socket(SERVER_ADDRESS, ChatServer.PORT));
            System.out.println(" Cliente conectado ao servidor em " + SERVER_ADDRESS + ":" + ChatServer.PORT);
            new Thread(this).start();
            messageLoop();

        } finally {
            clientSocket.close();
        }

    }
    
    @Override
    public void run(){
        String msg;
        while((msg = clientSocket.getMessage()) != null){
            System.out.printf("Msg recebida do servidor: %s\n", msg);
        }
        
    }

    

    private void messageLoop() {
        String msg;
        do {
            System.out.println("Digite uma mensagem (ou sair para finalizar):");
            msg = scanner.nextLine();
            clientSocket.sendMsg(msg);
        } while (!msg.equalsIgnoreCase("Sair"));
    }

    public static void main(String[] args) {
        try {
            ChatClient client = new ChatClient();
            client.start();
        } catch (IOException ex) {
            System.out.println("Erro ao iniciar cliente: " + ex.getMessage());
        }
        System.out.println("Cliente finalizado!");
    }

}
