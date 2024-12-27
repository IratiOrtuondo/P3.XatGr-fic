package Chat;

public class Client {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java Client <host> <port> <name>");
            return;
        }

        // Datos de conexión
        String hostname = args[0];
        int port = Integer.parseInt(args[1]);
        String nick = args[2];

        // Conectar al servidor
        MySocket socket = new MySocket(hostname, port);
        socket.write(nick); // Enviar el nombre de usuario al servidor

        // Crear la interfaz gráfica
        SwingGUI gui = new SwingGUI(nick, socket);

        
            new Thread(() -> {
        String message;
        while ((message = socket.readLine()) != null) {
            if (message.startsWith("UPDATE_USERS:")) {
                String[] users = message.substring("UPDATE_USERS:".length()).split(",");
                gui.updateUserList(users); // Actualiza la lista en la GUI
            } else {
                gui.addMessage(message); // Añade el mensaje al área de texto
            }
        }
    }).start();
        
    
    }
}