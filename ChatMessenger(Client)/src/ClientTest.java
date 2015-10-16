import java.awt.*;
import java.net.*;
import javax.swing.*;

public class ClientTest {

	public static void main(String[] args) {
		Client cl=new Client("127.0.0.1");//localhost
		cl.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		cl.startRunning();
	}

}