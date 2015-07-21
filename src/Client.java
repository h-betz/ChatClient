import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
 
public class Client extends JFrame {
 
    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private String message = "";
    private String serverIP;
    private Socket sock;
     
    //constructor
    public Client(String host) {
         
        super("Client Hunter");
        serverIP = host;
        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(
             
            new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    sendMessage(event.getActionCommand());
                    userText.setText("");
                }
            }
             
        );
        add(userText, BorderLayout.NORTH);
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow), BorderLayout.CENTER);
        setSize(300, 150);
        setVisible(true);
         
    }
     
    //start connection
    public void start() {
         
        try {
            connectToServer();
            setupStreams();
            whileChat();
        } catch(EOFException eof) {
            showMessage("\n Client ended connection.");
        } catch(IOException ioe) {
            ioe.printStackTrace();
        } finally {
            close();
        }
    }
     
    //connect to server
    private void connectToServer() throws IOException {
         
        showMessage("Attempting connection... \n");
        sock = new Socket(InetAddress.getByName(serverIP), 8888);
        showMessage("Connected to: " + sock.getInetAddress().getHostName());
         
    }
     
    //setup streams for sending and receivig data
    private void setupStreams() throws IOException {
         
        oos = new ObjectOutputStream(sock.getOutputStream());
        oos.flush();
        ois = new ObjectInputStream(sock.getInputStream());
        showMessage("\n Streams are now set. \n");
         
    }
     
    //while communicating with server
    private void whileChat() throws IOException {
         
        ableToType(true);
         
        do {
            try {
                message = (String) ois.readObject();
                showMessage("\n" + message);
            } catch (ClassNotFoundException cnfe) {
                showMessage("\n Object type not known.");
            }
        } while(!message.equals("SERVER - END"));
         
    }
     
    //close the streams and sockets
    private void close() {
         
        showMessage("\n CLosing down...");
        ableToType(false);
        try {
             
            ois.close();
            oos.close();
            sock.close();
             
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
         
    }
     
    //send messages to server
    private void sendMessage(String txt) {
         
        try {
            oos.writeObject("CLIENT - " + txt);
            oos.flush();
            showMessage("\nCLIENT - " + txt);
        } catch (IOException ioe) {
            chatWindow.append("\n Message send failed!");
        }
         
    }
     
    //update chat window
    private void showMessage(final String s) {
         
        SwingUtilities.invokeLater (
            new Runnable() {
                public void run() {
                    chatWindow.append(s);
                }
            }
        );
         
    }
     
    //enable permissions for user to type
    private void ableToType(final boolean bool) {
        SwingUtilities.invokeLater (
            new Runnable() {
                public void run() {
                    userText.setEditable(bool);
                }               
            }
        );
    }
     
}