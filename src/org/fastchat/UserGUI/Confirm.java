package org.fastchat.UserGUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.fastchat.user.User;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;


// Handshake simulation class
@SuppressWarnings("serial")
public class Confirm extends JDialog {

	private final JPanel contentPanel = new JPanel();
	
	public Confirm(User sender,User watchUser)  {
		
		setBounds(100, 100, 450, 264);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNewLabel = new JLabel("Da li odobravate komunikaciju?");
			lblNewLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 15));
			lblNewLabel.setBounds(76, 146, 336, 46);
			contentPanel.add(lblNewLabel);
		}
		{
			String name=sender.getName()+" "+sender.getSurname();
			JLabel newMsg = new JLabel("NEW MESSAGE FROM: "+name);
			newMsg.setBounds(25, 12, 387, 19);
			newMsg.setForeground(Color.BLUE);
			newMsg.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 16));
			contentPanel.add(newMsg);
		}
		

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("YES");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
	                       String message=watchUser.readSteganograhyMessage(sender);   	
	                       ChatRec chatWindow=new ChatRec(watchUser, sender, message,false);
	                       chatWindow.setVisible(true);
	                       dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("NO");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String msg="Nije odobrena komunikacija!";
						try {
							watchUser.sendSteganographyMessage(msg, sender,true);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
