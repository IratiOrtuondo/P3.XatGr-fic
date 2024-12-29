package opcionalnio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

public class ChatServer extends GenericServer {
    private final Map<SocketChannel, String> clientNicknames = new HashMap<>();

    public ChatServer(int port) throws IOException {
        super(port);
    }

    @Override
    protected void onDataAvailable(SocketChannel client, ByteBuffer data) {
        String message = new String(data.array(), 0, data.limit()).trim();

        if (!clientNicknames.containsKey(client)) {
            if (clientNicknames.containsValue(message)) {
                sendToClient(client, "Alias en uso. Introduce otro: ");
            } else {
                clientNicknames.put(client, message);
                sendToClient(client, "Alias registrado como: " + message);
                System.out.println("Alias registrado: " + message);
            }
            return;
        }

        String nickname = clientNicknames.get(client);
        broadcastMessage(nickname, message);
    }

    @Override
    protected void onClientConnected(SocketChannel client) throws IOException {
        super.onClientConnected(client);
        sendToClient(client, "Introduce tu alias: ");
    }

    @Override
    protected void onClientDisconnected(SocketChannel client) throws IOException {
        super.onClientDisconnected(client);
        String nickname = clientNicknames.remove(client);
        if (nickname != null) {
            System.out.println("Cliente desconectado: " + nickname);
        }
    }

    private void sendToClient(SocketChannel client, String message) {
        try {
            client.write(ByteBuffer.wrap((message + "\n").getBytes()));
        } catch (IOException e) {
        }
    }

    private void broadcastMessage(String senderNickname, String message) {
        String fullMessage = senderNickname + ": " + message + "\n";
        ByteBuffer buffer = ByteBuffer.wrap(fullMessage.getBytes());

        clientNicknames.keySet().forEach(client -> {
            try {
                client.write(buffer.duplicate());
            } catch (IOException e) {
            }
        });
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Uso: java ChatServer <puerto>");
            return;
        }

        int port = Integer.parseInt(args[0]);

        try {
            new ChatServer(port).start();
        } catch (IOException e) {
        }
    }
}
