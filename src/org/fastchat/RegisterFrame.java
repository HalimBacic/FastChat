package org.fastchat;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import java.awt.Font;
import javax.swing.JEditorPane;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.border.LineBorder;
import javax.swing.JButton;
import javax.swing.UIManager;
import java.awt.Cursor;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class RegisterFrame extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 * 
	 * @throws IOException
	 */
	public RegisterFrame() throws IOException {
		JEditorPane username = new JEditorPane();
		JEditorPane name = new JEditorPane();
		JEditorPane pass = new JEditorPane();
		JEditorPane surname = new JEditorPane();
		JButton addBtn = new JButton("ADD ME");

		setResizable(false);
		setBounds(200, 200, 432, 526);
		contentPane = new JPanel();
		contentPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JTextArea txtrName = new JTextArea();
		txtrName.setBounds(32, 30, 136, 37);
		txtrName.setDragEnabled(true);
		txtrName.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		txtrName.setBackground(SystemColor.window);
		txtrName.setFont(new Font("Noto Sans CJK JP Black", Font.BOLD, 25));
		txtrName.setEditable(false);
		txtrName.setText("NAME:");
		contentPane.add(txtrName);

		JTextArea txtrSurname = new JTextArea();
		txtrSurname.setBounds(32, 79, 136, 37);
		txtrSurname.setText("SURNAME:");
		txtrSurname.setFont(new Font("Noto Sans CJK JP Black", Font.BOLD, 20));
		txtrSurname.setEditable(false);
		txtrSurname.setDragEnabled(true);
		txtrSurname.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		txtrSurname.setBackground(SystemColor.window);
		contentPane.add(txtrSurname);

		surname.setBounds(171, 79, 227, 37);
		surname.setFont(new Font("Dialog", Font.PLAIN, 15));
		contentPane.add(surname);

		JTextArea txtrPassword = new JTextArea();
		txtrPassword.setBounds(32, 177, 136, 37);
		txtrPassword.setText("PASSWORD:");
		txtrPassword.setFont(new Font("Noto Sans CJK JP Black", Font.BOLD, 18));
		txtrPassword.setEditable(false);
		txtrPassword.setDragEnabled(true);
		txtrPassword.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		txtrPassword.setBackground(SystemColor.window);
		contentPane.add(txtrPassword);

		JTextArea txtrUsername = new JTextArea();
		txtrUsername.setBounds(32, 128, 136, 37);
		txtrUsername.setText("USERNAME:");
		txtrUsername.setFont(new Font("Noto Sans CJK JP Black", Font.BOLD, 18));
		txtrUsername.setEditable(false);
		txtrUsername.setDragEnabled(true);
		txtrUsername.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		txtrUsername.setBackground(SystemColor.window);
		contentPane.add(txtrUsername);
		addBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		addBtn.setBackground(SystemColor.info);
		addBtn.setBorder(UIManager.getBorder("CheckBox.border"));
		addBtn.setBounds(299, 408, 99, 45);
		contentPane.add(addBtn);

		name.setFont(new Font("Dialog", Font.PLAIN, 15));
		name.setBounds(171, 30, 227, 37);
		contentPane.add(name);

		username.setFont(new Font("Dialog", Font.PLAIN, 15));
		username.setBounds(171, 128, 227, 37);
		contentPane.add(username);

		pass.setFont(new Font("Dialog", Font.PLAIN, 15));
		pass.setBounds(171, 177, 227, 37);
		contentPane.add(pass);
		
		JTextArea btnEmail = new JTextArea();
		btnEmail.setText("EMAIL:");
		btnEmail.setFont(new Font("Dialog", Font.BOLD, 18));
		btnEmail.setEditable(false);
		btnEmail.setDragEnabled(true);
		btnEmail.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		btnEmail.setBackground(SystemColor.window);
		btnEmail.setBounds(32, 226, 136, 37);
		contentPane.add(btnEmail);
		
		JEditorPane email = new JEditorPane();
		email.setFont(new Font("Dialog", Font.PLAIN, 15));
		email.setBounds(171, 226, 227, 37);
		contentPane.add(email);
		
		JTextArea City = new JTextArea();
		City.setText("CITY:");
		City.setFont(new Font("Dialog", Font.BOLD, 18));
		City.setEditable(false);
		City.setDragEnabled(true);
		City.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		City.setBackground(SystemColor.window);
		City.setBounds(32, 275, 136, 37);
		contentPane.add(City);
		
		JEditorPane city = new JEditorPane();
		city.setFont(new Font("Dialog", Font.PLAIN, 15));
		city.setBounds(171, 275, 227, 37);
		contentPane.add(city);
		
		JTextArea employment = new JTextArea();
		employment.setText("EMPLOYMENT:");
		employment.setFont(new Font("Dialog", Font.BOLD, 15));
		employment.setEditable(false);
		employment.setDragEnabled(true);
		employment.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		employment.setBackground(SystemColor.window);
		employment.setBounds(32, 324, 136, 37);
		contentPane.add(employment);
		
		JEditorPane emp = new JEditorPane();
		emp.setFont(new Font("Dialog", Font.PLAIN, 15));
		emp.setBounds(171, 324, 227, 37);
		contentPane.add(emp);
		
		addBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				User user;
				try {
					user = new User(name.getText(), surname.getText(), username.getText(), pass.getText());
					FileWriter userWrite = new FileWriter("users.txt", true);
					userWrite.append(user.getUsername() + "\n");
					FileOutputStream objUser = new FileOutputStream("FastChatObj/" + user.getUsername() + ".fco");
					ObjectOutputStream objectOut = new ObjectOutputStream(objUser);
					String fname=name.getText();
					String fsurname=surname.getText();
					String femail=email.getText();
					String fcity=city.getText();
					String femp=emp.getText();
					
					BufferedWriter writeDetails=new BufferedWriter(new FileWriter
							(new File("administration/requests/"+username.getText()+".txt")));
					writeDetails.write("New request from:"+fname+" "+fsurname+"\n"+femail+"\n"+fcity+"\n"+femp);
					writeDetails.close(); 
					objectOut.writeObject(user);
					objectOut.close();
					objUser.close();
					userWrite.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				setVisible(false);
				dispose();
			}
		});
	}
}
