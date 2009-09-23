/* Main RPC entrypoint.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/server/RPCServiceImpl.java,v $
 * $Date: 2009/09/23 08:27:49 $
 * $Author: dtrg $
 * $Revision: 1.7 $
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
import com.cowlark.stellation2.server.model.SFactory;
import com.cowlark.stellation2.server.model.SFleet;
import com.cowlark.stellation2.server.model.SObject;
import com.cowlark.stellation2.server.model.SPlayer;
import com.cowlark.stellation2.server.model.STug;
import com.cowlark.stellation2.server.model.SUnit;
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

	private <T extends SObject> T getVisibleOwnedObject(SPlayer player, long id,
			Class<T> classobj)
		throws StellationException
	{
		try
		{
			T object = classobj.cast(Database.get(id));
			object.checkObjectOwnedBy(player);
			object.checkObjectVisibleTo(player);
			return object;
		}
		catch (ClassCastException e)
		{
			throw new InvalidObjectException(id);
		}
	}
	
	/* Admin */
	
	public UpdateBatch ping(Authentication auth, long since)
		throws StellationException
	{
		return new UpdatingCommand()
				{
					public void run(SPlayer player)
							throws StellationException
					{
					}
				}
				.execute(auth, since);
	}

	public Map<String, String> adminLoadObject(
			final Authentication auth, final long id)
	        throws StellationException
	{
		return new RawCommand<Map<String, String>>()
				{
					@Override
					public Map<String, String> run() throws StellationException
					{
						SPlayer player = getPlayer(auth);
						player.checkAdministrator();
			
						RootObject o = Database.get(id);
						return DataMangler.serialise(o);
					}
				}
				.execute();
	}
	
	public void adminSaveObject(final Authentication auth, final long id,
				final Map<String, String> map)
	        throws StellationException
	{
		new RawCommand<Void>()
				{
					@Override
					public Void run() throws StellationException
					{
						SPlayer player = getPlayer(auth);
						player.checkAdministrator();
			
						RootObject o = DataMangler.deserialise(map);
						o.setId(id);
						if (o instanceof SObject)
							((SObject) o).dirty();
							
						Database.put(o);
						return null;
					}
				}
				.execute();
	}

	/* Cargoship */
	
	public UpdateBatch cargoshipLoadUnload(Authentication auth, long since,
			final long id,
			final double antimatter, final double metal, final double organics)
		throws StellationException
	{
		return new UpdatingCommand()
				{
					public void run(SPlayer player)
							throws StellationException
					{
								SCargoship cargoship = getVisibleOwnedObject(player, id,
										SCargoship.class);
								cargoship.loadUnloadCmd(antimatter, metal, organics);
					}
				}
				.execute(auth, since);
	}
	
	/* Tug */
	
	public UpdateBatch tugUnload(Authentication auth, long since,
			final long id)
	        throws StellationException
	{
		return new UpdatingCommand()
				{
					public void run(SPlayer player)
							throws StellationException
					{
						STug tug = getVisibleOwnedObject(player, id,
								STug.class);
						tug.unloadCmd();
					}
				}
				.execute(auth, since);
	}
	
	public UpdateBatch tugLoad(Authentication auth, long since,
			final long id, final long cargoid)
	        throws StellationException
	{
		return new UpdatingCommand()
				{
					public void run(SPlayer player)
							throws StellationException
					{
						STug tug = getVisibleOwnedObject(player, id,
								STug.class);
						SUnit cargo = getVisibleOwnedObject(player, cargoid,
								SUnit.class);
						tug.loadCmd(cargo);
					}
				}
				.execute(auth, since);
	}
	
	public UpdateBatch factoryBuild(Authentication auth, long since,
			final long id, final int type)
	        throws StellationException
	{
		return new UpdatingCommand()
				{
					public void run(SPlayer player)
							throws StellationException
					{
						SFactory factory = getVisibleOwnedObject(player, id,
								SFactory.class);
						factory.buildCmd(type);
					}
				}
				.execute(auth, since);
	}
	
	public UpdateBatch factoryAbort(Authentication auth, long since,
			final long id)
	        throws StellationException
	{
		return new UpdatingCommand()
				{
					public void run(SPlayer player)
							throws StellationException
					{
						SFactory factory = getVisibleOwnedObject(player, id,
								SFactory.class);
						factory.abortCmd();
					}
				}
				.execute(auth, since);
	}
	
	public UpdateBatch factoryDeploy(Authentication auth, long since,
			final long id, final long fleetid)
	        throws StellationException
	{
		return new UpdatingCommand()
				{
					public void run(SPlayer player)
							throws StellationException
					{
						SFactory factory = getVisibleOwnedObject(player, id,
								SFactory.class);
						SFleet fleet = getVisibleOwnedObject(player, fleetid,
								SFleet.class);
						factory.deployCmd(fleet);
					}
				}
				.execute(auth, since);
	}
}
