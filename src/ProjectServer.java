import java.util.*;
import java.io.*;
import java.net.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.*;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import java.awt.*;
import java.awt.event.*;

class ProjectServer
{
	private Socket          socket   = null;
    private ServerSocket    server   = null;
    private DataInputStream in       =  null;
    public ProjectServer() throws IOException
    {
    	//establishes connection and waits for the client
            server = new ServerSocket(1020);
            while(true)
            {
            	System.out.println("Server started");
            	System.out.println("Waiting for a client ...");
            	try
            	{
            		socket = server.accept();
            		System.out.println("Client accepted");
            		//attaching input stream with socket
            		in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            		//creating a thread
            		Thread t = new ClientHandler(socket, in);
            		// Invoking the start() method
            		t.start();
            	}
            	catch(IOException i)
            	{
            		System.out.println(i);
            	}
            }
        }
 
    public static void main(String args[]) throws IOException
    {
        new ProjectServer();
    }
}

class ClientHandler extends Thread
{
	final DataInputStream dis;
    final Socket s;
    public ClientHandler(Socket s, DataInputStream dis) 
    {
    	this.s = s;
    	this.dis = dis;
   
    }
    public void run() 
	{
		String line = "";
        try
        {
        	//reading the message received from client
            line += dis.readUTF();
            System.out.println(line);
        }
        catch   (Exception i) 
        {
            System.out.println(i);
        }
        System.out.println("Closing connection");
        try 
        {
        	//closing socket
			s.close();
		} 
        catch (IOException e)
        {
			e.printStackTrace();
		}
        try 
        {
        	//closing input stream
			dis.close();
		} catch (IOException e)
        {
			e.printStackTrace();
		}
    	sendMessage(line);
 
	}
    private void sendMessage(String line)
    {
		 @SuppressWarnings("resource")
		 Formatter fmt = new Formatter();  
		 String item_name[]=new String[10];
		 String item_price[]=new String[10];
		 String item_quantity[]=new String[10];
		 String arr[]=line.split("/",20);
		 String message1="";
		 fmt.format("%15s%15s%15s%15s\n","ITEM","QUANTITY","PRICE PER ITEM","TOTAL PRICE");
		 double total=0.0;
		 for(int i=1;i<11;i++)
		 {
			 int j=i-1;
			String x[]= arr[i].split(",",20);
			item_name[j]=x[0];
			item_price[j]=x[1];
			item_quantity[j]=x[2];
			double price=Double.parseDouble(item_price[j]);
			int quantity=Integer.parseInt(item_quantity[j]);
			total+=price*quantity;
			fmt.format("%21s%15s%15.0f%23.0f\n",item_name[j],item_quantity[j],price,price*quantity);
		 }
		 message1+=fmt.toString();
		 message1+="Total:"+total;
		 String recipient = arr[0];
		 new ServerGUI(recipient,message1);		
	}

}
@SuppressWarnings("serial")
class ServerGUI extends JFrame implements ActionListener
{
	JLabel email_label;
	JTextField email_tf;
	JLabel password_label;
	JPasswordField password_tf;
	JPanel pan;
	JButton submit;
	String sender="";
	String password="";
	String message1="";
	String recipient="";
	ServerGUI(String x, String y)
	{
		message1=y;
		recipient=x;
		email_label=new JLabel("Email ID:");
		email_tf=new JTextField(15);
		password_label=new JLabel("Password");
		password_tf=new JPasswordField(15);
		submit=new JButton("submit");
		pan=new JPanel(new GridBagLayout());
	    pan.setPreferredSize(new Dimension(800,800));
	    GridBagConstraints c=new GridBagConstraints();
	    c.insets=new Insets(15,15,1,1);
	    c.gridx=0;
	    c.gridy=0;
	    pan.add(email_label,c);
	    c.gridx=1;
	    pan.add(email_tf,c);
	    c.gridx=0;
	    c.gridy=1;
	    pan.add(password_label,c);
	    c.gridx=1;
	    pan.add(password_tf,c);
	    c.gridx=1;
	    c.gridy=2;
	    pan.add(submit,c);
	    submit.addActionListener(this);
	    add(pan);
	    pack();
        setLocationRelativeTo(null);
	    setVisible(true);
	    setSize(500,500);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    //GUI to take sender credentials
	    setTitle("Sender Credentials");
	    
	    
		
	}
	public void actionPerformed(ActionEvent ae) 
	  {
	    if(ae.getSource()==submit)
	    {
	    	
	    	sender=email_tf.getText();
	    	password=String.copyValueOf(password_tf.getPassword());
	    	
	    	// Getting system properties
			 Properties properties = new Properties();
			 properties.put("mail.smtp.auth", "true");
			 properties.put("mail.smtp.starttls.enable", "true");
			 properties.put("mail.smtp.host","smtp.gmail.com");
			 properties.put("mail.smtp.port", "587");
			 
			  // Setting up mail server
			 // creating session object to get properties
			  Session session = Session.getInstance(properties,new Authenticator() {
		  	  @Override
		  	  protected PasswordAuthentication getPasswordAuthentication() {
		  	  return new PasswordAuthentication(sender,password);
		  	  }
			 });
		
		      try
		      {
			      MimeMessage message = new MimeMessage(session);
			      // Set From Field: adding senders email to from field.
			      message.setFrom(new InternetAddress(sender));
			      // Set To Field: adding recipient's email to from field.
			      message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
			      // Set Subject: subject of the email
			      message.setSubject("BILL");
			      // set body of the email.
			      message.setText(message1);
			      // Send email.
			      Transport.send(message);
			      System.out.println("Mail successfully sent");
		      }
		    catch (MessagingException mex)
		    {
		    	mex.printStackTrace();
		    }	
	    }
	  }
}

