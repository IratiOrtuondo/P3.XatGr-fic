import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwingGUI {

    private JFrame frame; // Ventana principal
    private JTextArea messageArea; // Área para mostrar mensajes
    private JTextField inputField; // Campo de texto para escribir mensajes
    private JButton sendButton; // Botón para enviar mensajes
    private DefaultListModel<String> userListModel; // Modelo para gestionar la lista de usuarios
    private JList<String> userList; // Lista gráfica de usuarios

    private MySocket socket; 

    public SwingGUI(String username, MySocket socket) {
        this.socket = socket; 
        initializeGUI(username);
    }

    private void initializeGUI(String username) {
        frame = new JFrame("Chat - " + username);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // **Área de Mensajes**
        messageArea = new JTextArea();
        messageArea.setEditable(false); 
        JScrollPane messageScrollPane = new JScrollPane(messageArea);

        // **Área de Usuarios**
        userListModel = new DefaultListModel<>(); 
        userList = new JList<>(userListModel); 
        JScrollPane userScrollPane = new JScrollPane(userList);
        userScrollPane.setPreferredSize(new Dimension(150, 0)); 

        // **Área de Entrada**
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        sendButton = new JButton("Enviar");

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        frame.setLayout(new BorderLayout());
        frame.add(messageScrollPane, BorderLayout.CENTER); 
        frame.add(userScrollPane, BorderLayout.EAST); 
        frame.add(inputPanel, BorderLayout.SOUTH); 

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        frame.setVisible(true);
    }

    private void sendMessage() {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            socket.write(message); 
            inputField.setText(""); 
        }
    }

    public void addMessage(String message) {
        messageArea.append(message + "\n");
    }

    // Método para actualizar la lista de usuarios conectados
    public void updateUserList(String[] users) {
        userListModel.clear(); 
        for (String user : users) {
            userListModel.addElement(user); 
        }
    }
}
