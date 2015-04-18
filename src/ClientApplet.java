import javax.swing.JApplet;
import javax.swing.JFrame;


public class ClientApplet extends JApplet{
	
	Client client;
	
	public void init(){
		client = new Client("127.0.0.1");
		client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.startRunning();
	}
	
}


