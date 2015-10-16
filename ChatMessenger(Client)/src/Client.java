import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.*;
import java.net.*;

public class Client extends JFrame{//for GUI
	
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message="";//message to sent to server
	private String serverIP;
	private Socket connection;
	
	//constructor..parameter host
	public Client(String host){
		super("Client's Chatroom :-)");
		serverIP=host;//imp
		userText=new JTextField();
		userText.setEditable(false);
		userText.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				sendData(event.getActionCommand());
				userText.setText("");
			}
		});
		add(userText,BorderLayout.SOUTH);
		
		chatWindow=new JTextArea();
		add(new JScrollPane(chatWindow),BorderLayout.CENTER);//scroll
		setSize(500,300);
		setVisible(true);
	}
	
	//connect to server
	public void startRunning(){
		try{
			connectToServer();
			setupStreams();
			whileChatting();
			
		}catch(EOFException eof){
			showMessage("\n client terminated");
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			closeCrap();//close everything
		}
	}
	
	//connect to server
	private void connectToServer() throws IOException{
		showMessage("Attempting Connection....\n");
		connection=new Socket(InetAddress.getByName(serverIP),6789);//(IPaddress,port)      
		showMessage("Connected to:" + connection.getInetAddress().getHostName());
	}
	
	//pathways..setup streams for sending and receiving data
	private void setupStreams() throws IOException{
		output=new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input=new ObjectInputStream(connection.getInputStream());
		showMessage("\n Streams connected");
	}
	
	//whileChatting with server
	private void whileChatting() throws IOException{
		ableToType(true);
		do{
			try{
				message=(String) input.readObject();
				showMessage("\n" + message);
			}catch(ClassNotFoundException err){
				showMessage("\n I don't know that! ");
			}
			
		}while(!message.equals("SERVER - END"));
	}
	
	//close the streams and sockets
	private void closeCrap(){
		showMessage("\nclosing connections...");
		ableToType(false);
		try{
			output.close();
			input.close();
			connection.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//send messages to server
	private void sendData(String message){
		try{
			output.writeObject("CLIENT - " + message);
			output.flush();
			showMessage("\n CLIENT - "+message);//show on chatWindow
			
		}catch(IOException ex){
			chatWindow.append("\n Something went wrong");
		}
	}
	
	//updates chatWindow
	private void showMessage(final String message){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				chatWindow.append(message);
			}
		});
	}
	
	//let the user type stuff in textbox
	private void ableToType(boolean val){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				userText.setEditable(true);
			}
		});
	}
	
}