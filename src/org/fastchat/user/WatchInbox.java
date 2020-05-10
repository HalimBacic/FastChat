package org.fastchat.user;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.fastchat.UserGUI.ChatRec;
import org.fastchat.UserGUI.Confirm;

import static java.nio.file.StandardWatchEventKinds.*;


//Watch inbox changes
public class WatchInbox extends Thread {

	Path dir;
	WatchService watchService;
	WatchKey key;
	User watchUser;

	
	public WatchInbox(User us) throws IOException {
		watchService = FileSystems.getDefault().newWatchService();
		dir = us.getCheck();
		key = dir.register(watchService, ENTRY_MODIFY);
		watchUser=us;
	}

	@Override
	public void run()
	{
		try {
			startWatch();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}
	public void startWatch() throws Exception {
		while (true) {
			key = watchService.take();
			Thread.sleep(30);
			for (WatchEvent<?> event : key.pollEvents()) {
				WatchEvent.Kind<?> kind = event.kind();
				@SuppressWarnings("unchecked")
				WatchEvent<Path> ev=(WatchEvent<Path>) event;
				Path fileToRead=ev.context();
				String str=fileToRead.toString();
				
				if (kind.equals(ENTRY_MODIFY)) {					
					//Operations for get sender 
                    
                    String[] sender=str.split("\\.");
                    FileInputStream fileInputStream=new FileInputStream("FastChatObj/"+sender[0]+".fco");
                    ObjectInputStream objectInputStream=new ObjectInputStream(fileInputStream);
                    User sendUser=(User) objectInputStream.readObject();
                    objectInputStream.close();
                    

                    
                   if(sender[1].equals("stg"))
                    {
                	   
                	     Confirm confirmDialog=new Confirm(sendUser,watchUser);
                	     confirmDialog.setVisible(true);
                    }
                   else if(sender[1].equals("lst"))
                   {
                       String message=watchUser.readSteganograhyMessage(sendUser);
                       ChatRec chatWindow=new ChatRec(watchUser, sendUser, message,true);
                       chatWindow.setVisible(true);
                   }
                    else
                    {
                    String message=watchUser.readMessage(sendUser);
                    ChatRec chatWindow=new ChatRec(watchUser, sendUser, message,false);
                    chatWindow.setVisible(true);
                    } 
                 }
			}
			key.reset();
		}
	}
}
;