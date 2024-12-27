import java.util.concurrent.ConcurrentHashMap;

public class Server {

    private static ConcurrentHashMap<String, MySocket> users = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Server <port>");
            return;
        }

        int port = Integer.parseInt(args[0]);
        MyServerSocket serverSocket = new MyServerSocket(port);
        System.out.println("Server started.");

        while (true) {
            MySocket client = serverSocket.accept();

            new Thread(() -> {
                String username = client.readLine();
                addUser(username, client);

                String message;
                while ((message = client.readLine()) != null) {
                    broadcastMessage(message, username);
                }

                removeUser(username);
                client.close();
            }).start();
        }
    }

    private static void addUser(String username, MySocket socket) {
        users.put(username, socket);
        broadcastUserList();
    }

    private static void removeUser(String username) {
        users.remove(username);
        broadcastUserList();
    }

    private static void broadcastMessage(String message, String sender) {
        users.forEach((user, socket) -> {
            if (!user.equals(sender)) {
                socket.write(sender + ": " + message);
            }
        });
    }

    private static void broadcastUserList() {
        String userList = String.join(",", users.keySet());
        users.forEach((user, socket) -> socket.write("UPDATE_USERS:" + userList));
    }
}
