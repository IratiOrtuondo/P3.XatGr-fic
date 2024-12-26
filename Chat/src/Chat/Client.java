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

        // Hilo para recibir mensajes del servidor
        new Thread(() -> {
            String message;
            while ((message = socket.readLine()) != null) {
                gui.addMessage(message); // Mostrar mensaje en el área de texto
            }
        }).start();

        // No se necesita un hilo separado para enviar mensajes:
        // La lógica está directamente gestionada por la GUI a través de socket.write()
    }
}
