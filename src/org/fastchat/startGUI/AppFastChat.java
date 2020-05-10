package org.fastchat.startGUI;

import java.io.File;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import org.fastchat.UserGUI.StartFrame;

/**
 * 
 * @author halim
 *
 */
public class AppFastChat {
	
	KeyPair keys;
	
	//App keys
	public static KeyPair setKeys()
	{

        KeyPairGenerator keyPairGenerator=null;
		try {
			keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
        keyPairGenerator.initialize(1024);
        return keyPairGenerator.generateKeyPair();
	}

	public static void main(String[] args) throws Exception {
		
		File fastchatobj = new File("FastChatObj"); // For objects-users
		if (!fastchatobj.exists())
			fastchatobj.mkdir();
        
		File fastchat = new File("FastChat"); // For user inboxes
		if (!fastchat.exists())
			fastchat.mkdir();

		
		File check = new File("Checker"); // For inbox check
		if (!check.exists())              //Tested on Linux, on Windows it can be in inbox folder implemented
			check.mkdir();
		
		File active = new File("active"); // For online users
		if (!active.exists())
			active.mkdir();
		
		File users = new File("users.txt"); // All registered usernames
		if (!users.exists())
			users.createNewFile();
		
		StartFrame start=new StartFrame();
		start.setVisible(true);		
	}
}
