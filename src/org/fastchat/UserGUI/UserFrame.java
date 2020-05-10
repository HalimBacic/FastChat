package org.fastchat.UserGUI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.fastchat.user.User;
import org.fastchat.user.WatchActive;
import org.fastchat.user.WatchInbox;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class UserFrame extends JFrame {

	private JPanel contentPane;
	private JTextField usernamefld;
	private ArrayList<User> users = new ArrayList<>(); // Users in chat, online and offline
	private User userLogin;
	private final JButton btnLogout = new JButton("LogOut");
	private ArrayList<JButton> btnUsers = new ArrayList<>();   //Buttons for users
	
	public void setBtnColor(boolean color,int name)
	{
		if(color==true) {
			JButton btnToChange=btnUsers.get(name);
		    btnToChange.setBackground(Color.GREEN);
		}
		else {
			JButton btnToChange=btnUsers.get(name);
		    btnToChange.setBackground(Color.WHITE);
		}
	}
	
	public JButton fgetUserBtn(int i)
	{
		return btnUsers.get(i);
	}
	
	public String returnUserName()
	{
		return userLogin.getName()+" "+userLogin.getSurname();
	}
	public void setButtonActive(User userToChat,int i)
	{
		setBtnColor(true, i);
		btnUsers.get(i).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SendChat chat;
				try {
					chat = new SendChat(userLogin, userToChat);
					chat.setVisible(true);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

		});
	}
	
	public UserFrame(String login) throws Exception {

	
		// Load configuration after success login
		BufferedReader readUsers = new BufferedReader(new FileReader("users.txt"));
		User user = null;
		JFrame frameError = new JFrame();
		String lineUser = readUsers.readLine();
		while (lineUser != null) {
			File pathToObj = new File("FastChatObj/" + lineUser + ".fco");
			if (pathToObj.exists()) {
				FileInputStream fileInputStream = new FileInputStream(pathToObj);
				ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
				user = (User) objectInputStream.readObject();

				if (user.getUsername().equals(login)) {
					userLogin = user;
					File activeUserFile=new File("active/"+userLogin.getName()+" "+userLogin.getSurname());
					activeUserFile.createNewFile();
					
					userLogin.setActive(true);
					

					File toDel = new File("FastChatObj/" + userLogin.getUsername() + ".fco");
					toDel.delete();
					FileOutputStream fileOutputStream = new FileOutputStream(
							"FastChatObj/" + userLogin.getUsername() + ".fco");
					ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
					objectOutputStream.writeObject(userLogin);
					objectOutputStream.close();
					fileOutputStream.close();
					WatchInbox watchInbox=new WatchInbox(userLogin);
					watchInbox.start();	
					Thread.sleep(20);
					WatchActive watchActive=new WatchActive(this, btnUsers);
					watchActive.start();
				}
				else
					users.add(user);
				objectInputStream.close();
				lineUser = readUsers.readLine();
			} else {

				JOptionPane.showMessageDialog(frameError, "Wrong username.", "Inane error", JOptionPane.ERROR_MESSAGE);
			}
		}
		readUsers.close();

		/**********************************************/
		setBounds(200, 100, 271, 479);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		
		usernamefld = new JTextField();
		usernamefld.setEditable(false);
		usernamefld.setForeground(Color.GREEN);
		usernamefld.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		usernamefld.setBackground(new Color(0, 0, 0));
		usernamefld.setBounds(27, 12, 125, 39);
		usernamefld.setText(" @" + userLogin.getUsername());
		contentPane.add(usernamefld);
		usernamefld.setColumns(10);
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String toLogout = userLogin.getUsername();
				userLogin.setActive(false);
				File toDel = new File("FastChatObj/" + toLogout + ".fco");
				toDel.delete();
				FileOutputStream fileOutputStream;
				try {
					fileOutputStream = new FileOutputStream("FastChatObj/" + toLogout + ".fco");
					ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
					objectOutputStream.writeObject(userLogin);
					objectOutputStream.close();
					fileOutputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			dispose();
			File activeUserFile=new File("active/"+userLogin.getName()+" "+userLogin.getSurname());
			activeUserFile.delete();
			}
		});

		btnLogout.setForeground(new Color(255, 255, 255));
		btnLogout.setBackground(new Color(255, 0, 0));
		btnLogout.setFont(new Font("Dialog", Font.BOLD, 15));
		btnLogout.setBounds(163, 13, 94, 40);
		contentPane.add(btnLogout);

		
		for (int i = 0; i < users.size(); i++) {
			JButton button = new JButton(users.get(i).getName() + " " + users.get(i).getSurname());
			btnUsers.add(button);
			User userToChat = users.get(i);
			btnUsers.get(i).setBounds(75, 65 + i * 38, 145, 35);
			File activeFile=new File("active/"+userToChat.getName()+" "+userToChat.getSurname());
			if (activeFile.exists()) {
				setButtonActive(userToChat,i);
			}
			else 
				setBtnColor(false, i);
			contentPane.add(btnUsers.get(i));
		}
	}
}
