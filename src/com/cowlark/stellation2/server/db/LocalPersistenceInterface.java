/* Local storage persistence plugin.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/server/db/LocalPersistenceInterface.java,v $
 * $Date: 2009/09/06 17:59:15 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.server.db;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import javax.servlet.ServletConfig;
import com.cowlark.stellation2.server.Log;

public class LocalPersistenceInterface extends PersistenceInterface
{
	private static final Semaphore _semaphore = new Semaphore(1);
	
	private String _filename;
	
	public LocalPersistenceInterface(ServletConfig config)
    {
		_filename = "/tmp/stellation.db";
    }
	
	public boolean isCached()
	{
		return true;
	}
	
	public void save(Map<Long, RootObject> db) throws FileNotFoundException
	{
		PrintWriter pw = new PrintWriter(_filename);

		Log.log("begin database save");
		long startTime = System.currentTimeMillis();
		int count = 0;
		
		for (RootObject o : db.values())
		{
			Map<String, String> map = DataMangler.serialise(o);
			pw.println(map.size());
			for (Map.Entry<String, String> entry : map.entrySet())
			{
				pw.println(entry.getKey());
				pw.println(entry.getValue());
			}
			count++;
		}
		
    	long delta = System.currentTimeMillis() - startTime;
    	Log.log("saved %d objects in %d ms (%d ms per object)",
    			count, delta, delta / count);
		pw.close();
	}
	
	public Map<Long, RootObject> load() throws IOException
	{
		FileReader fr = new FileReader(_filename);
		BufferedReader br = new BufferedReader(fr);
		
		HashMap<Long, RootObject> db = new HashMap<Long, RootObject>(); 
		HashMap<String, String> map = new HashMap<String, String>();

		Log.log("begin database load");
		long startTime = System.currentTimeMillis();
		int count = 0;
		
		for (;;)
		{
			String s = br.readLine();
			if (s == null)
				break;
			
			map.clear();
			int i = Integer.parseInt(s);
			while (i > 0)
			{
				i--;
				
				String key = br.readLine();
				String value = br.readLine();
				map.put(key, value);
			}
			
			RootObject o = (RootObject) DataMangler.deserialise(map);
			db.put(o.getId(), o);
			
			count++;
		}
		
    	long delta = System.currentTimeMillis() - startTime;
    	Log.log("loaded %d objects in %d ms (%d ms per object)",
    			count, delta, delta / count);

		fr.close();
		return db;
	}
	
	@Override
	public void lock()
	{
		for (;;)
		{
			try
			{
				_semaphore.acquire();
				return;
			}
			catch (InterruptedException e)
			{
			}
		}
	}

	@Override
	public void unlock()
	{
		_semaphore.release();
	}
}
