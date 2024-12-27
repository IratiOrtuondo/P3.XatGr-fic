public class Client {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java Client <host> <port> <name>");
            return;
        }

        String hostname = args[0];
        int port = Integer.parseInt(args[1]);
        String username = args[2];

        MySocket socket = new MySocket(hostname, port);
        socket.write(username); 

        SwingGUI gui = new SwingGUI(username, socket);

        new Thread(() -> {
            String message;
            while ((message = socket.readLine()) != null) {
                if (message.startsWith("UPDATE_USERS:")) {
                    String[] users = message.substring("UPDATE_USERS:".length()).split(",");
                    gui.updateUserList(users); 
                } else {
                    gui.addMessage(message);
                }
            }
        }).start();
    }
}
