import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatClientGUI {

    private JFrame frame; 
    private JTextArea messageArea; 
    private JTextField inputField; 
    private JButton sendButton; 
    private DefaultListModel<String> userListModel; 
    private JList<String> userList; 

    private MySocket socket; 

    public ChatClientGUI(String username, MySocket socket) {
        this.socket = socket; 
        initializeGUI(username);
    }

    private void initializeGUI(String username) {
        // Crear ventana principal
        frame = new JFrame("Chat - " + username);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // Área de Mensajes
        messageArea = new JTextArea();
        messageArea.setEditable(false); // Solo lectura
        JScrollPane messageScrollPane = new JScrollPane(messageArea);

        // Área de Usuarios
        userListModel = new DefaultListModel<>(); 
        userList = new JList<>(userListModel); 
        JScrollPane userScrollPane = new JScrollPane(userList);
        userScrollPane.setPreferredSize(new Dimension(150, 0)); 

        // Área de Entrada
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        sendButton = new JButton("Enviar");

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        // Configurar el diseño principal
        frame.setLayout(new BorderLayout());
        frame.add(messageScrollPane, BorderLayout.CENTER); 
        frame.add(userScrollPane, BorderLayout.EAST);
        frame.add(inputPanel, BorderLayout.SOUTH); 

        // Acción del botón de enviar
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // Permitir enviar con la tecla Enter
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // Mostrar ventana
        frame.setVisible(true);
    }

    // Método para enviar mensajes al servidor
    private void sendMessage() {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            socket.write(message); 
            inputField.setText(""); 
        }
    }

    // Método para añadir un mensaje al área de mensajes
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
