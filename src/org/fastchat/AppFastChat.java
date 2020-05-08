package org.fastchat;

import java.io.File;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

/**
 * 
 * @author halim
 *
 */
public class AppFastChat {
	
	KeyPair keys;
	
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
		// TODO Auto-generated method stub
		File fastchatobj = new File("FastChatObj"); // Za objekte
		if (!fastchatobj.exists())
			fastchatobj.mkdir();
        
		File fastchat = new File("FastChat"); // Za inboxe
		if (!fastchat.exists())
			fastchat.mkdir();

		
		File check = new File("Checker"); // Za checkove
		if (!check.exists())
			check.mkdir();
		
		File active = new File("active"); // Za aktivne korisnike
		if (!active.exists())
			active.mkdir();
		
		File users = new File("users.txt"); // Za korisnike
		if (!users.exists())
			users.createNewFile();
		
		StartFrame start=new StartFrame();
		start.setVisible(true);		
	}
}
