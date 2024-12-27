package Chat;

public class Client {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java Client <host> <port> <name>");
            return;
        }

        String hostname = args[0];
        int port = Integer.parseInt(args[1]);
        String username = args[2];

        // Conectar al servidor
        MySocket socket = new MySocket(hostname, port);
        socket.write(username); // Enviar el nombre del usuario al servidor

        // Crear la interfaz gráfica
        ChatClientGUI gui = new ChatClientGUI(username, socket);

        // Hilo para recibir mensajes del servidor
        new Thread(() -> {
            String message;
            while ((message = socket.readLine()) != null) {
                if (message.startsWith("UPDATE_USERS:")) {
                    // Actualizar la lista de usuarios conectados
                    String[] users = message.substring("UPDATE_USERS:".length()).split(",");
                    gui.updateUserList(users);
                } else {
                    // Añadir mensaje al área de mensajes
                    gui.addMessage(message);
                }
            }
        }).start();
    }
}
