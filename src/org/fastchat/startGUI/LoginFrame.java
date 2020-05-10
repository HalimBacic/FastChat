package org.fastchat.startGUI;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import java.awt.SystemColor;
import javax.swing.border.LineBorder;

import org.fastchat.UserGUI.UserFrame;
import org.fastchat.user.User;

import java.io.*;
import java.security.cert.X509Certificate;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JEditorPane;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.UIManager;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class LoginFrame extends JFrame {

	private JPanel contentPane;
	private JPasswordField passLogin;

	public LoginFrame() {
		setBounds(100, 100, 436, 282);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JTextArea txtrUsername = new JTextArea();
		txtrUsername.setText("USERNAME:");
		txtrUsername.setFont(new Font("Noto Sans CJK JP Black", Font.BOLD, 20));
		txtrUsername.setEditable(false);
		txtrUsername.setDragEnabled(true);
		txtrUsername.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		txtrUsername.setBackground(SystemColor.window);
		txtrUsername.setBounds(26, 12, 136, 37);
		contentPane.add(txtrUsername);

		JTextArea txtrPassword = new JTextArea();
		txtrPassword.setText("PASSWORD:");
		txtrPassword.setFont(new Font("Noto Sans CJK JP Black", Font.BOLD, 19));
		txtrPassword.setEditable(false);
		txtrPassword.setDragEnabled(true);
		txtrPassword.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		txtrPassword.setBackground(SystemColor.window);
		txtrPassword.setBounds(26, 71, 136, 37);
		contentPane.add(txtrPassword);

		JEditorPane userLogin = new JEditorPane();
		userLogin.setFont(new Font("Dialog", Font.PLAIN, 15));
		userLogin.setBounds(174, 12, 227, 37);
		contentPane.add(userLogin);

		passLogin = new JPasswordField();
		passLogin.setBounds(174, 71, 227, 37);
		contentPane.add(passLogin);

		
		JButton btnLogin = new JButton("LOGIN");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// user = seccess login
				User user = null;
				String toFind = userLogin.getText();
				File fileToFind = new File("FastChatObj/" + toFind + ".fco");
				JFrame frameError = new JFrame();
				if (fileToFind.exists()) {
					try {
						/* Read conf for active user */
						FileInputStream fileInputStream = new FileInputStream(fileToFind);
						ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
						user = (User) objectInputStream.readObject();
						objectInputStream.close();
						fileInputStream.close();
					} catch (IOException | ClassNotFoundException e1) {
						e1.printStackTrace();
					}

					char[] pass1 = passLogin.getPassword();
					char[] pass2 = user.getPass().toCharArray();
					/* If password is ok, there is new GUI form */
					if (Arrays.equals(pass1, pass2)) {
						try {
							UserFrame userFrame;
							try {
								X509Certificate userCertificate=user.loadCertificate();
								userCertificate.checkValidity();
								userFrame = new UserFrame(user.getUsername());
								userFrame.setVisible(true);
							} catch (Exception e) {
							
								e.printStackTrace();
							}
							
						} catch (Exception e) {
							
							e.printStackTrace();
						} 
					} else {

						JOptionPane.showMessageDialog(frameError, "Wrong password!", "Inane error",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(frameError, "Wrong username!", "Inane error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		/********************************/
		btnLogin.setBorder(UIManager.getBorder("CheckBox.border"));
		btnLogin.setBackground(SystemColor.info);
		btnLogin.setBounds(237, 151, 99, 45);
		contentPane.add(btnLogin);
	}

}
