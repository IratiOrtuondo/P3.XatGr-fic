package opcionalnio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;



public abstract class GenericServer {
    private Selector selector;
    private ServerSocketChannel serverChannel;
    private boolean running = true;

    public GenericServer(int port) throws IOException {
        selector = Selector.open();
        serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.bind(new InetSocketAddress(port));
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("Servidor genérico iniciado en el puerto " + port);
    }

    public void start() throws IOException {
        while (running) {
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                if (key.isAcceptable()) {
                    handleAccept((ServerSocketChannel) key.channel());
                } else if (key.isReadable()) {
                    handleRead(key);
                }
            }
        }
    }

    public void stop() {
        running = false;
        try {
            selector.close();
            serverChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleAccept(ServerSocketChannel serverChannel) throws IOException {
        SocketChannel clientChannel = serverChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);
        onClientConnected(clientChannel);
    }

    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(256);
        int bytesRead = clientChannel.read(buffer);

        if (bytesRead == -1) {
            onClientDisconnected(clientChannel);
            clientChannel.close();
            key.cancel();
        } else {
            buffer.flip();
            onDataAvailable(clientChannel, buffer);
        }
    }

    protected abstract void onDataAvailable(SocketChannel client, ByteBuffer data);

    protected void onClientConnected(SocketChannel client) throws IOException {
        System.out.println("Cliente conectado: " + client.getRemoteAddress());
    }

    protected void onClientDisconnected(SocketChannel client) throws IOException {
        System.out.println("Cliente desconectado: " + client.getRemoteAddress());
    }
}
