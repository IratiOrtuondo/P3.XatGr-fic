/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package opcionalnio;

/**
 *
 * @author ortuu
 */
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class ChatClient {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Uso: java SimpleChatClient <host> <puerto>");
            return;
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);
        

        try (SocketChannel clientChannel = SocketChannel.open(new InetSocketAddress(host, port))) {
            clientChannel.configureBlocking(true);

            System.out.println("Conectado al servidor de chat. Escribe tus mensajes:");

            // Hilo para leer mensajes del servidor
            new Thread(() -> {
                try {
                    ByteBuffer buffer = ByteBuffer.allocate(256);
                    while (true) {
                        buffer.clear();
                        int bytesRead = clientChannel.read(buffer);
                        if (bytesRead > 0) {
                            buffer.flip();
                            String message = new String(buffer.array(), 0, buffer.limit()).trim();
                            System.out.println( message);
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Conexión cerrada por el servidor.");
                }
            }).start();

            // Escribir mensajes al servidor
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String message = scanner.nextLine();
                ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
                clientChannel.write(buffer);
            }

        } catch (IOException e) {
            System.out.println("Error al conectar con el servidor: " + e.getMessage());
        }
    }
}

