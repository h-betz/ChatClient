import javax.swing.JFrame;
 
public class ClientTest {
 
    public static void main(String[] args) {
         
        Client cli;
        cli = new Client("127.0.0.1");
        cli.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cli.start();
         
    }
     
}