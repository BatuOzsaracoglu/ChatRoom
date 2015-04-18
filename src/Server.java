import java.io.*;
import java.awt.*;
import java.net.*;
import java.awt.event.*;
import javax.swing.*;

public class Server extends JFrame{

	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;
	
	//constructor
	public Server(){
		super("Batu's Instant Messanger");
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				sendMessage(e.getActionCommand());
				userText.setText("");
				
			}});
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow));
		setSize(300, 150);
		setVisible(true);
		
	}
	
	//set up and run the server
	public void startRunning(){
		try{
			server = new ServerSocket(1234, 100);
			while(true){
				try{
					//connect and have a conversation
					waitForConnection();
					setupStreams();
					whileChatting();
					
				}catch(EOFException e){
					showMessage("\n Server ended the connection!");
				}finally{
					closeInfo();
				}
			}
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	//wait for connection, then display connection information
	private void waitForConnection() throws IOException{
		showMessage("Waiting for someone to connect... \n");
		connection = server.accept();
		showMessage("Now connected to "+ connection.getInetAddress().getHostName());
	}
	
	//get stream to send and recieve data
	private void setupStreams() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n Streams are now set up! \n");		
	}
	
	//During the chat conversation
	private void whileChatting() throws IOException{
		String message = "You are now connected!";
		sendMessage(message);
		ableToType(true);
		
		do{
			//have a conversation
			try{
			message = (String) input.readObject();
			showMessage("\n" + message);
			}catch(ClassNotFoundException e){
				showMessage("\n Can't understand information sent");
			}
			
		}while(!message.equals("CLIENT - END"));
			
	}
	
	//close streams and sockets after you are done chatting
	private void closeInfo(){
		showMessage("\n Closing connectoins...");
		ableToType(false);
		try{
			output.close();
			input.close();
			connection.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	//sends message to client
	private void sendMessage(String message){
		try{
			output.writeObject("SERVER -  " + message);
			output.flush();
			showMessage("\n SERVER -  " + message);
		}catch(IOException e){
			chatWindow.append("\n ERROR: MESSAGE CAN'T BE SENT");
		}
	}
	
	//updates chat window
	private void showMessage(final String text){
		SwingUtilities.invokeLater(
			new Runnable(){
				public void run(){
					chatWindow.append(text);
				}
			}	
		);
	}
	
	//let the user type stuff into their box
	private void ableToType(final boolean tof){
		SwingUtilities.invokeLater(
				new Runnable(){
					public void run(){
						userText.setEditable(tof);
					}
				}	
		);
	}
	
	
}
