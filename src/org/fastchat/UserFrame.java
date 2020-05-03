package org.fastchat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.fastchat.User;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Font;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class UserFrame extends JFrame {

	private JPanel contentPane;
	private JTextField usernamefld;
	private ArrayList<User> users = new ArrayList<>(); // Korisnici u chatu
	private User userLogin;
	private final JButton btnLogout = new JButton("LogOut");
	private ArrayList<JButton> btnUsers = new ArrayList<>();
	
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
 //   private WatchInbox watch;
	/**
	 * Create the frame.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws InterruptedException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 */
	public UserFrame(String login) throws Exception {

	
		// Ucitavanje konfiguracije
		BufferedReader readUsers = new BufferedReader(new FileReader("users.txt"));
		User user = null;
		JFrame frameError = new JFrame();
		/***** KRITICNA SEKCIJA *******/
		/* Citanje svake linije u users.txt */
		/* Omogucuje otvaranja konfiguracija */
		String lineUser = readUsers.readLine();
		while (lineUser != null) {
			File pathToObj = new File("FastChatObj/" + lineUser + ".fco");
			if (pathToObj.exists()) {
				FileInputStream fileInputStream = new FileInputStream(pathToObj);
				ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
				user = (User) objectInputStream.readObject();

				/* Ako je ucitani user iz datoteke users.txt */
				/* onaj ciji se login trazi, on postaje korisnik */
				if (user.getUsername().equals(login)) {
					userLogin = user;
					File activeUserFile=new File("active/"+login);
					activeUserFile.createNewFile();
					
					/* Logovan je, status se mjenja u aktivan */
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
				/* Ili se dodaje u one gdje je moguca komunikacija */
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
				File activeUserFile=new File("active/"+login);
				activeUserFile.delete();
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
			btnUsers.get(i).setBounds(75, 65 + i * 38, 125, 35);
			if (users.get(i).getActive()) {
				setBtnColor(true, i);
				btnUsers.get(i).addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						SendChat chat;
						try {
							chat = new SendChat(userLogin, userToChat);
							chat.setVisible(true);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

				});
			}
			contentPane.add(btnUsers.get(i));
		}
	}
}
