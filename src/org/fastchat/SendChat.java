package org.fastchat;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.JEditorPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

@SuppressWarnings("serial")
public class SendChat extends JFrame {

	private JPanel contentPane;
	private JTextField textField;

	/**
	 * Create the frame.
	 */
	public SendChat(User sender,User receiver) {
		setBounds(100, 100, 452, 369);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField = new JTextField();
		textField.setFont(new Font("Noto Sans CJK JP Black", Font.BOLD, 16));
		textField.setBackground(Color.YELLOW);
		textField.setText("  "+receiver.getName()+" "+receiver.getSurname()+" ");
		textField.setBounds(53, 22, 329, 39);
		contentPane.add(textField);
		textField.setEditable(false);
		textField.setColumns(10);
		
		JEditorPane editorPane = new JEditorPane();
		editorPane.setFont(new Font("Noto Sans CJK JP Light", Font.PLAIN, 14));
		editorPane.setBounds(53, 70, 329, 178);
		contentPane.add(editorPane);
		
		JButton btnSend = new JButton("SEND");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String message=editorPane.getText();
				try {
				//	sender.sendMessage(message, receiver);
					sender.sendSteganographyMessage(message, receiver,false);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				dispose();
			}
		});
		btnSend.setBounds(164, 260, 102, 26);
		contentPane.add(btnSend);
	}
}
