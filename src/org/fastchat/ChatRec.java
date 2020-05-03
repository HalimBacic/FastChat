package org.fastchat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.awt.Font;
import javax.swing.UIManager;
import javax.swing.JRadioButton;

@SuppressWarnings("serial")
public class ChatRec extends JFrame {

	private JPanel contentPane;
	private JTextField textField;

	/**
	 * fol - first or last
	 */
	public ChatRec(User receiver, User sender, String msg,boolean fol) {
		setType(Type.UTILITY);
		setTitle("NEW MESSAGE");
		setBounds(100, 100, 452, 369);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		textField = new JTextField();
		textField.setForeground(UIManager.getColor("MenuBar.highlight"));
		textField.setEditable(false);
		textField.setFont(new Font("Noto Sans CJK JP Black", Font.BOLD, 16));
		textField.setBackground(UIManager.getColor("OptionPane.questionDialog.border.background"));
		textField.setText("  " + sender.getName() + " " + sender.getSurname() + " ");
		textField.setBounds(53, 22, 329, 39);
		contentPane.add(textField);
		textField.setColumns(10);

		JRadioButton lastBtn = new JRadioButton("Finish Chat");
		lastBtn.setBounds(295, 261, 127, 24);
		contentPane.add(lastBtn);
		
		JEditorPane textarea = new JEditorPane();
		textarea.setFont(new Font("Noto Sans CJK SC Bold", Font.PLAIN, 14));
		JButton btnSend = new JButton("REPLY");
        btnSend.setEnabled(false);
		
		textarea.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				btnSend.setText("SEND");
				btnSend.setEnabled(true);
				textarea.setText("");
				textarea.setEditable(true);
				File bmp=new File(receiver.getInbox()+"/"+sender.getUsername()+".bmp");
			    bmp.delete();
				File msg=new File(receiver.getInbox()+"/"+sender.getUsername()+".msg");
				msg.delete();
				File kgn=new File(receiver.getInbox()+"/"+sender.getUsername()+".kgn");
				kgn.delete();
				File sign1=new File(receiver.getInbox()+"/"+sender.getUsername()+".ksgn");
				sign1.delete();
				File sign2=new File(receiver.getInbox()+"/"+sender.getUsername()+".msgn");
				sign2.delete();
			}
		});
		textarea.setBounds(53, 70, 329, 178);
		contentPane.add(textarea);
		textarea.setText(msg);
		textarea.setEditable(false);

		if(fol==true)
		{
			btnSend.setEnabled(false);
			textarea.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					textarea.setText("Last message...");
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					dispose();	
				}
			});
		}
		
		btnSend.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String msgToReply = textarea.getText();
				if(lastBtn.isSelected())
				{
					try {
						receiver.sendSteganographyMessage("LAST MESSAGE\n"+msgToReply, sender,true);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else {
				try {
					receiver.sendMessage(msgToReply, sender);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				}
				dispose();
			}
		});
		btnSend.setBounds(166, 260, 102, 26);
		contentPane.add(btnSend);
	}
}
