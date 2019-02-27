package Framset;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
public class Register extends JFrame{
	public String username;
	public String password;
    public Register(){
    	this.setTitle("Register here please");
    	Container C = this.getContentPane();
    	C.setLayout(new BorderLayout());
    //	this.setBounds(500,100,500,500);
    	this.setSize(500, 500);
    	this.setLocationRelativeTo(null);
    	this.addWindowListener(new WindowAdapter() {
    		@Override
    		public void windowClosing(WindowEvent e) {
    			System.exit(0);
    		}
    	});
    	
    	
     JPanel top = new JPanel();
   	 JLabel title = new JLabel("Register your account");
   	 title.setFont(new Font("Times",Font.BOLD,16));
   	 top.add(title);
   	 C.add(top, "North");
   	 
   	 JPanel input = new JPanel();
   	 input.setLayout(null);
   	 JLabel userName = new JLabel("Set username: ");
   	 userName.setFont(new Font("Times",Font.BOLD,14));
   	 userName.setBounds(40, 100,130, 40);
        input.add(userName);
        
        JLabel passWord = new JLabel("Set password: ");
   	    passWord.setFont(new Font("Times",Font.BOLD,14));
   	    passWord.setBounds(40, 160, 130, 40);
        input.add(passWord);
     
        
       JLabel confirmP = new JLabel("Confirm password: ");
       confirmP.setFont(new Font("Times", Font.BOLD,14));
       confirmP.setBounds(40, 220, 150, 40);
       input.add(confirmP);
       
        JTextField UserName = new JTextField();
        UserName.setBounds(180, 105, 200, 25);
        input.add(UserName);
        String username = UserName.getText(); 
        
        JTextField PassWord = new JPasswordField();
       PassWord.setBounds(180, 162, 200, 25);
        input.add(PassWord);
       
     
        
        JTextField ConfirmP = new JPasswordField();
         ConfirmP.setBounds(180, 225, 200, 25);
          input.add(ConfirmP);
          C.add(input, "Center");
       
          

        JPanel buttonP = new JPanel();
        JButton ConfirmBT = new JButton("Confirm");
       
     	ConfirmBT.addActionListener(new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent arg0) {
			 String password = PassWord.getText();
			   String confirm = ConfirmP.getText();
			 
			if(PassWord.getText().equals(ConfirmP.getText())){
			createAccount(PassWord.getText());
			}
			else {
				JOptionPane.showMessageDialog(null, "The two passwords are not same", "Wrong",JOptionPane.WARNING_MESSAGE); 
			}
			
		}
		
	  });
   	
        buttonP.add(ConfirmBT);
        
        JButton CancelBT = new JButton("Cancel");
        CancelBT.addActionListener(new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent arg0) {
			LoginF newF= new LoginF();
		    System.out.println("huilai");
			closeThis();
		}
		
	  });
        
        buttonP.add(CancelBT);
        C.add(buttonP, "South");
        
        
    //    JSONObject test = new JSONObject(); 
      //  try {
      //  test.put("type", "login");
      //  test.put("username", this.username);
       // test.put("password", this.password);
       // }
        //catch(Exception e) {
       // }
            
   	 this.setVisible(true);
    }
    
    public void closeThis(){
    	
    	this.dispose();
    }
    //use the following function would be executed if the password and user name are good. 
    public void createAccount(String password){
    	this.password= password;
    	LoginF newF= new LoginF();
		this.dispose();
    	
    }
    // use following method to get response from Server.
   public String getResponse(){
	   return null;
   } 
    
    public void setPassword(String password){
    	this.password=password;
    }
    public static void main(String args[]){
    	Register test = new Register();
    	System.out.println("Start");
    }
}
