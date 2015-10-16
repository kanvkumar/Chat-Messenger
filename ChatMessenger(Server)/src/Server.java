import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Server extends JFrame{//for GUI
	
	private JTextField userText;//message
	private JTextArea chatWindow;
	private ObjectOutputStream output;//going away from u
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;//for connection
	
	//constructor
	public Server(){
		super("Kanv's ChatMessenger :-)");
		userText=new JTextField();
		userText.setEditable(false);
		userText.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				sendMessage(event.getActionCommand());
				userText.setText("");
			}
		});
		add(userText,BorderLayout.SOUTH);
		
		chatWindow=new JTextArea();
		add(new JScrollPane(chatWindow),BorderLayout.CENTER);
		setSize(500,300);
		setVisible(true);
	}
	
	//set up and run the server
	public void startRunningServer(){
		try{
			server = new ServerSocket(6789,100);//port 6789, backlog 100
			while(true){
				try{
					waitForConnection();
					setupStreams();
					whileChatting();
				}
				catch(Exception e){
					showMessage("\n Server ended the conncetion");
				}
				finally{
					closeCrap();
				}	
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	//wait for the connection, then display connection info.
	private void waitForConnection() throws IOException{
		showMessage("Waiting for Someone to connect....\n");
		connection = server.accept();//socket created
		showMessage("Now Connected to " + connection.getInetAddress().getHostName());
		
	}
	
	//pathways....setting streams for sending and receiving data
	private void setupStreams() throws IOException{
		output=new ObjectOutputStream(connection.getOutputStream());
		output.flush();//flush to other side if anything left over....
		input=new ObjectInputStream(connection.getInputStream()); 
		
	}
	
	//during the chat conversation
	private void whileChatting() throws IOException{
		sendMessage("You are now connected");
		ableToType(true);
		String message="";
		do{
			try{
				message=(String) input.readObject();//read the incoming messages
				showMessage("\n" + message);
			}
			catch(Exception e){
				showMessage("\n session ended");
			}
		}while(!message.equals("CLIENT - END"));
	}
	
	//close streams and sockets after you are done chatting
	private void closeCrap(){
		showMessage("\n closing connections....\n");
		ableToType(false);
		try{
			output.close();
			input.close();
			connection.close();//socket closed
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//send a message to client
	private void sendMessage(String message){
		try{
			output.writeObject("SERVER - " + message);
			output.flush();
			showMessage("\nSERVER - " + message);
		}
		catch(Exception e){
			chatWindow.append("\n Some ERROR occured while sending!");
		}
	}
	
	//updates chatWindow
	private void showMessage(final String text){
		SwingUtilities.invokeLater(new Runnable(){//update GUI....using thread
			public void run(){
				chatWindow.append(text);
			}
		});
	}
	
	//let the user type stuff into the textbox
	private void ableToType(final boolean val){
		SwingUtilities.invokeLater(new Runnable(){//update GUI
			public void run(){
				userText.setEditable(val);
			}
		});
	}
}
