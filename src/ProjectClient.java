import java.io.IOException;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*; 

@SuppressWarnings("serial")
class ProjectClient extends JFrame implements ActionListener
{
  JLabel title;
  JButton add_to_cart;
  JTextField quantity[]=new JTextField[10];
  JLabel item[]=new JLabel[10];
  JLabel email_id;
  JTextField email_id_tf;
  JPanel pan;
  JLabel item_label;
  JLabel item_price_label;
  JLabel item_quantity_label;
  JLabel price[]=new JLabel[10];
  private Socket socket;
  private DataOutputStream out;
  ItemClass obj[] =new ItemClass[10];
 
  public ProjectClient()
  {
     try
        {
    	 	//creating socket to initiate communication
            socket = new Socket(InetAddress.getByName("127.0.0.1"),1020);
            System.out.println("Connected");
            //attaching output stream with socket
            out = new DataOutputStream(socket.getOutputStream());
        }
        catch(UnknownHostException u)
        {
            System.out.println(u);
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
     title=new JLabel("A TO Z STORE");
     title.setForeground(Color.red);
     pan=new JPanel(new GridBagLayout());
     pan.setPreferredSize(new Dimension(800,800));
     email_id_tf=new JTextField(30);
     email_id=new JLabel("Email id:");
     email_id.setForeground(Color.red);
     item_label=new JLabel("ITEM");
     item_label.setForeground(Color.yellow);
     item_quantity_label=new JLabel("QUANTITY");
     item_quantity_label.setForeground(Color.yellow);
     item_price_label=new JLabel("PRICE");
     item_price_label.setForeground(Color.yellow);
     obj[0]=new ItemClass("Item0",100.0);
     obj[1]=new ItemClass("Item1",200.0);
     obj[2]=new ItemClass("Item2",150.0);
     obj[3]=new ItemClass("Item3",200.0);
     obj[4]=new ItemClass("Item4",250.0);
     obj[5]=new ItemClass("Item5",100.0);
     obj[6]=new ItemClass("Item6",400.0);
     obj[7]=new ItemClass("Item7",250.0);
     obj[8]=new ItemClass("Item8",180.0);
     obj[9]=new ItemClass("Item9",350.0);
    for(int i=0;i<10;i++)
    {
    	price[i]=new JLabel(String.valueOf(obj[i].item_price));
    	price[i].setForeground(Color.green);
    	item[i]=new JLabel(obj[i].item_name);
    	item[i].setForeground(Color.green);
    	quantity[i]=new JTextField(10);
    }
    add_to_cart=new JButton("ADD TO CART");
    GridBagConstraints c=new GridBagConstraints();
    pan.setBackground(Color.black);
    c.insets=new Insets(15,15,1,1);
    c.gridx=10;
    c.gridy=0;
    pan.add(title,c);
    c.gridx=0;
    c.gridy=1;
    pan.add(email_id,c);
    c.gridx=10;
    pan.add(email_id_tf,c);
    c.gridx=0;
    c.gridy=2;
    pan.add(item_label,c);
    c.gridx=10;
    pan.add(item_price_label,c);
    c.gridx=20;
    pan.add(item_quantity_label,c);
    c.gridx=0;
    for(int i=0;i<10;i++)
    {
    	c.gridx=0;
    	c.gridy+=1;
    	pan.add(item[i],c);
        c.gridx=10;
        pan.add(price[i],c);
        c.gridx=20;
        pan.add(quantity[i],c);
    }
    c.gridy+=1;
    c.gridx=10;
    pan.add(add_to_cart,c);
    add(pan);
    add_to_cart.addActionListener(this);
    setLayout(new FlowLayout());
    setVisible(true);
    setSize(1000,1000);
    setTitle("A to Z store");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
  public void actionPerformed(ActionEvent ae) 
  {
    if(ae.getSource()==add_to_cart)
    {
    	String line=email_id_tf.getText();
    	for(int i=0;i<10;i++)
    	{
    		if(quantity[i].getText().isEmpty())
    		{
    			obj[i].item_quantity=0;
    		}
    		else
    		{
    			obj[i].item_quantity=Integer.parseInt(quantity[i].getText());	
    		}
    		 line += obj[i].toStr();
    	}
    	try
    	 {
    	       //sending details to client                        
    	       out.writeUTF(line);
    	       JOptionPane.showMessageDialog(this,"Bill has been sent to your mail\nThank you!! ");  
    	 }
    	 catch(IOException e)
    	 {
    	       System.out.println(e);
    	 }
    }
  }
 public static void main(String[] args) {
   new ProjectClient();
 }
}