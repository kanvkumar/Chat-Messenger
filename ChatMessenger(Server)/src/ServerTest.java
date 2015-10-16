import javax.swing.*;
public class ServerTest {
	public static void main(String[] args) {
		Server s=new Server();
		s.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		s.startRunningServer();
	}
}
