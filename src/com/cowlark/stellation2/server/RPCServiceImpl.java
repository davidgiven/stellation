/* Main RPC entrypoint.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/server/RPCServiceImpl.java,v $
 * $Date: 2009/09/09 23:17:34 $
 * $Author: dtrg $
 * $Revision: 1.4 $
 */

package com.cowlark.stellation2.server;

import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import com.cowlark.stellation2.client.RPCService;
import com.cowlark.stellation2.common.Authentication;
import com.cowlark.stellation2.common.UpdateBatch;
import com.cowlark.stellation2.common.db.Database;
import com.cowlark.stellation2.common.exceptions.DatabaseCorruptException;
import com.cowlark.stellation2.common.exceptions.InvalidObjectException;
import com.cowlark.stellation2.common.exceptions.StellationException;
import com.cowlark.stellation2.server.db.DataMangler;
import com.cowlark.stellation2.server.db.GAEPersistenceInterface;
import com.cowlark.stellation2.server.db.PersistenceInterface;
import com.cowlark.stellation2.server.db.RootObject;
import com.cowlark.stellation2.server.db.ServerDB;
import com.cowlark.stellation2.server.model.SCargoship;
import com.cowlark.stellation2.server.model.SObject;
import com.cowlark.stellation2.server.model.SPlayer;
import com.cowlark.stellation2.server.model.SUniverse;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class RPCServiceImpl extends RemoteServiceServlet implements
        RPCService
{
    private static final long serialVersionUID = 4022080474337461584L;

	@Override
	public void init(ServletConfig config) throws ServletException
	{
	    super.init(config);
	    Log.log("server init");
	    
	    PersistenceInterface persistence = new GAEPersistenceInterface(config);
	    Database.Instance = ServerDB.Instance = new ServerDB(persistence);
	}
	
	@Override
	public void destroy()
	{
		ServerDB.Instance.close();
		
		Log.log("server deinit");
	    super.destroy();
	}
	
	public void createUser(String uid, String password, String empire, String name)
		throws StellationException
	{
		try
		{
			ServerDB.Instance.lock();		
			try
			{
				SUniverse universe = SUniverse.getStaticUniverse();
				Log.log("running game");
				universe.timePassing();
				Log.log("end running game");
				universe.createPlayer(uid, password, empire, name);
	        }
			finally
			{
				ServerDB.Instance.unlock();
			}
		}
		catch (IOException e)
		{
			throw new DatabaseCorruptException(e);
		}
	}

	private SPlayer getPlayer(Authentication auth)
		throws StellationException
	{
		SUniverse universe = SUniverse.getStaticUniverse();
		Log.log("running game");
		universe.timePassing();
		Log.log("end running game");
		return universe.authenticatePlayer(auth);
	}
	
	private UpdateBatch returnBatch(SPlayer player, long since)
	{
		UpdateBatch batch = new UpdateBatch();
		Log.log("updating visibility");
		player.updateVisibility();
		Log.log("done visibility search");
		player.addChangedItemsToBatch(batch, since);
		return batch;
	}
	
	public UpdateBatch ping(Authentication auth, long since)
		throws StellationException
	{
		try
		{
			ServerDB.Instance.lock();		
			try
			{
				SPlayer player = getPlayer(auth);
				return returnBatch(player, since);
			}
			finally
			{
				ServerDB.Instance.unlock();
			}
		}
		catch (IOException e)
		{
			throw new DatabaseCorruptException(e);
		}
	}
	
	public Map<String, String> adminLoadObject(Authentication auth, long id)
	        throws StellationException
	{
		try
		{
			ServerDB.Instance.lock();		
			try
			{
				SPlayer player = getPlayer(auth);
				player.checkAdministrator();
	
				RootObject o = Database.get(id);
				return DataMangler.serialise(o);
			}
			finally
			{
				ServerDB.Instance.unlock();
			}
		}
		catch (IOException e)
		{
			throw new DatabaseCorruptException(e);
		}
	}
	
	public void adminSaveObject(Authentication auth, long id,
				Map<String, String> map)
	        throws StellationException
	{
		try
		{
			ServerDB.Instance.lock();		
			try
			{
				SPlayer player = getPlayer(auth);
				player.checkAdministrator();
	
				RootObject o = DataMangler.deserialise(map);
				o.setId(id);
				if (o instanceof SObject)
					((SObject) o).dirty();
					
				Database.put(o);
			}
			finally
			{
				ServerDB.Instance.unlock();
			}
		}
		catch (IOException e)
		{
			throw new DatabaseCorruptException(e);
		}
	}
	
	/* Cargoship */
	
	public UpdateBatch cargoshipLoadUnload(Authentication auth, long since,
			long id, double antimatter, double metal, double organics)
	        throws StellationException
	{
	    		try
	    		{
	    			ServerDB.Instance.lock();		
	    			try
	    			{
	    				SPlayer player = getPlayer(auth);
	    				SObject object = Database.get(id);
	    				
	    				if (!(object instanceof SCargoship))
	    					throw new InvalidObjectException(id);
	    				object.checkObjectOwnedBy(player);
	    				object.checkObjectVisibleTo(player);
	    				
	    				SCargoship cargoship = object.toCargoship();
	    				cargoship.loadUnloadCmd(antimatter, metal, organics);
	    				
	    				return returnBatch(player, since);
	    			}
	    			finally
	    			{
	    				ServerDB.Instance.unlock();
	    			}
	    		}
	    		catch (IOException e)
	    		{
	    			throw new DatabaseCorruptException(e);
	    		}
	}
}
