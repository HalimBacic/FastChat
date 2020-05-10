package org.fastchat.UserGUI;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.fastchat.startGUI.About;
import org.fastchat.startGUI.LoginFrame;
import org.fastchat.startGUI.RegisterFrame;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;


//First GUI
@SuppressWarnings("serial")
public class StartFrame extends JFrame {

	private JPanel contentPane;
	
	public StartFrame() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 359, 515);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnRegister = new JButton("REGISTER");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegisterFrame registerFrame;
				try {
					registerFrame = new RegisterFrame();
					registerFrame.setVisible(true);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnRegister.setBounds(84, 122, 183, 70);
		contentPane.add(btnRegister);
		
		JButton btnLogin = new JButton("LOGIN");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoginFrame loginFrame = new LoginFrame();
				loginFrame.setVisible(true);
			}
		});
		btnLogin.setBounds(84, 223, 183, 70);
		contentPane.add(btnLogin);
		
		JButton btnAbout = new JButton("ABOUT");
		btnAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				About frame = new About();
				frame.setVisible(true);
			}
		});
		btnAbout.setBounds(84, 316, 183, 70);
		contentPane.add(btnAbout);
		
		
	}
}
