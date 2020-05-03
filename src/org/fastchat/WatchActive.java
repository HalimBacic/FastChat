package org.fastchat;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.nio.file.WatchEvent;
import javax.swing.JButton;
import static java.nio.file.StandardWatchEventKinds.*;

public class WatchActive extends Thread {
	Path dir;
	WatchService watchService;
	WatchKey key;
	UserFrame watchUser;
	ArrayList<JButton> btnsArrayList=new ArrayList<>();
	
	public WatchActive(UserFrame user,ArrayList<JButton> btns) throws IOException
	{
		watchService = FileSystems.getDefault().newWatchService();
		dir = Path.of("active");
		key = dir.register(watchService, ENTRY_CREATE,ENTRY_DELETE);
		watchUser=user;
	}
	
	@Override
	public void run()
	{
		try {
			startWatch();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}
	
	public void startWatch() throws Exception {
		while (true) {
			key = watchService.take();
			Thread.sleep(20);
			for (WatchEvent<?> event : key.pollEvents()) {
				WatchEvent.Kind<?> kind = event.kind();
				@SuppressWarnings("unchecked")
				WatchEvent<Path> ev=(WatchEvent<Path>) event;
				Path fileToRead=ev.context();
				String str=fileToRead.toString();
				int numOfBtn=0;
				for (JButton btn : btnsArrayList) {
					if(btn.getText().equals(str))
						numOfBtn=btnsArrayList.indexOf(btn);
					else {
						numOfBtn++;
					}
				}
				if (kind.equals(ENTRY_CREATE)) {					
					// BUTTON MUST BE GREEN
					watchUser.setBtnColor(true, numOfBtn);
                 }
				else if(kind.equals(ENTRY_DELETE))
				{
					watchUser.setBtnColor(false, numOfBtn);
				}
			}
			key.reset();
		}
	}
	
	
}
