*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Chat;

/**
 *
 * @author ortuo
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwingGUI {

    private JFrame frame;
    private JTextArea messageArea;
    private JTextField inputField;
    private JButton sendButton;
    private DefaultListModel<String> userListModel;
    private JList<String> userList;

    private String nick;
    private MySocket socket;

    public SwingGUI(String nick, MySocket socket) {
        this.nick = nick;
        this.socket = socket;
        initializeGUI();
    }

    private void initializeGUI() {
        // Crear ventana principal
        frame = new JFrame("Chat - " + nick);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        // Área de mensajes
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        JScrollPane messageScrollPane = new JScrollPane(messageArea);

        // **Lista de Usuarios**
        userListModel = new DefaultListModel<>(); // Modelo para gestionar usuarios
        userList = new JList<>(userListModel); // Lista gráfica basada en el modelo
        JScrollPane userScrollPane = new JScrollPane(userList);

        // Encapsular la lista en un panel con borde titulado
        JPanel userListPanel = new JPanel(new BorderLayout());
        userListPanel.setBorder(BorderFactory.createTitledBorder("Usuarios Conectados"));
        userListPanel.add(userScrollPane, BorderLayout.CENTER);


        // Panel de entrada
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        sendButton = new JButton("Enviar");

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        // Layout principal
        frame.setLayout(new BorderLayout());
        frame.add(messageScrollPane, BorderLayout.CENTER);
        frame.add(userListPanel, BorderLayout.EAST);
        frame.add(inputPanel, BorderLayout.SOUTH);

        // Acción del botón de enviar
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // Permitir presionar Enter para enviar mensajes
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // Mostrar ventana
        frame.setVisible(true);
    }

    // Método para enviar mensajes
    private void sendMessage() {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            socket.write(nick + "> " + message);
            addMessage("Yo> "+ message);
            inputField.setText(""); // Limpia el campo de texto
        }
    }

    // Método para añadir mensajes al área de mensajes
    public void addMessage(String message) {
        messageArea.append(message + "\n");
    }

    // Método para actualizar la lista de usuarios
    public void updateUserList(String[] users) {
    userListModel.clear(); // Limpia la lista actual
    for (String user : users) {
        userListModel.addElement(user); // Añade cada usuario al modelo
        
    }
   }
    
}
