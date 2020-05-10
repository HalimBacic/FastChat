package org.fastchat.startGUI;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextPane;
import java.awt.Font;

/**
 * 
 * @author halim
 */

//Just for About frame, shows app info
@SuppressWarnings("serial")
public class About extends JFrame {

	private JPanel contentPane;
	public About() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTextPane txtpnFastchatIsUser = new JTextPane();
		txtpnFastchatIsUser.setFont(new Font("Dialog", Font.PLAIN, 15));
		txtpnFastchatIsUser.setEditable(false);
		txtpnFastchatIsUser.setText("FastChat is user friendly  chat app.\nFirst, you must register profil with some informations.\nAfter that you can login in system and make secure chat\nwith all contacts in base.\nVersion v1.0\nProducer TinyCode");
		txtpnFastchatIsUser.setBounds(24, 29, 402, 219);
		contentPane.add(txtpnFastchatIsUser);
	}
}
