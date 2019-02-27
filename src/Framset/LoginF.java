package Framset;

 import javax.swing.JFrame;
 import javax.swing.event.AncestorEvent;
 import javax.swing.event.AncestorListener;

 import org.json.JSONObject;

 import javax.swing.*;
 import java.awt.*;
 import java.awt.event.*;
 import java.io.BufferedReader;
 import java.io.IOException;
 import java.io.InputStreamReader;
 import java.io.PrintWriter;
 import java.net.Socket;

 public class LoginF extends JFrame{
 private String username = "";
 private String password="";
 public JSONObject outputJS = new JSONObject();
 public boolean confirmflag = false;
 public LoginF() {
	 this.setBounds(((Toolkit.getDefaultToolkit().getScreenSize().width)/2)-250, ((Toolkit.getDefaultToolkit().getScreenSize().height)/2)-250,500,500);
	 this.setTitle("Log in here please");
	 this.setLayout(null);
	 Container C = this.getContentPane();
	 C.setLayout(new BorderLayout()); 
	 JPanel top = new JPanel();
	 JLabel title = new JLabel("Snake Race!");
	 title.setFont(new Font("Times",Font.BOLD,16));
	 top.add(title);
	 C.add(top, "North");
	 
	 JPanel input = new JPanel();
	 input.setLayout(null);
	 JLabel userName = new JLabel("Username: ");
	 userName.setFont(new Font("Times",Font.BOLD,14));
	 userName.setBounds(40, 150,110, 40);
     input.add(userName);
     
     JLabel passWord = new JLabel("Password: ");
	 passWord.setFont(new Font("Times",Font.BOLD,14));
	 passWord.setBounds(40, 200, 110, 40);
     input.add(passWord);
     
     JTextField UserName = new JTextField();
     UserName.setBounds(150, 160, 200, 25);
     
     input.add(UserName);
     
     JTextField PassWord = new JPasswordField();

    PassWord.setBounds(150, 208, 200, 25);
     input.add(PassWord);
     C.add(input, "Center");

     JPanel buttonP = new JPanel();
     JButton LogBT = new JButton("Log In");
     LogBT.setBounds(60, 400, 60, 30);
    LogBT.addActionListener(new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent arg0) {
			Socket socket;
			try {
			socket = new Socket("147.188.195.131",4399);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
			setusername(UserName.getText());
			setpassword(PassWord.getText());
			try {
			     outputJS.put("type", "login");
			     outputJS.put("username", getusername());
			     outputJS.put("password", getpassword());
			     }
			     catch(Exception e) {
			    	 e.printStackTrace();
			     }
			out.println(outputJS.toString());
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}

		}
    });
     
     
    JButton RegisterBT = new JButton("Register");
 
	RegisterBT.addActionListener(new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent arg0) {
			Register registF= new Register();
		   System.out.println("dianle");
			closeThis();
		}
		
	});
     buttonP.add(LogBT);
     buttonP.add(RegisterBT);
     C.add(buttonP, "South");
     

     
     this.setVisible(true);
   }
 
    public  void closeThis(){
	 
	 this.dispose();
    }
 
    public void setusername(String username){
    	this.username=username;
    }
    public void setpassword(String password){
    	this.password = password;
    }
    public String getusername(){
    	return this.username;
    }
    public String getpassword(){return this.password;}
    public String getJSON(){
    	return this.outputJS.toString();
    }

	public static void main(String[] args) {
		LoginF test = new LoginF();
	}
}