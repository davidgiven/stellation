/* Server-side player.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/server/model/SPlayer.java,v $
 * $Date: 2009/09/15 23:15:49 $
 * $Author: dtrg $
 * $Revision: 1.3 $
 */

package com.cowlark.stellation2.server.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import com.cowlark.stellation2.common.Resources;
import com.cowlark.stellation2.common.S;
import com.cowlark.stellation2.common.UpdateBatch;
import com.cowlark.stellation2.common.db.ListOfClientObjects;
import com.cowlark.stellation2.common.db.LogMessageStore;
import com.cowlark.stellation2.common.exceptions.FleetAlreadyHasThisNameException;
import com.cowlark.stellation2.common.exceptions.NotAdministratorException;
import com.cowlark.stellation2.common.exceptions.StellationException;
import com.cowlark.stellation2.common.model.CObject;
import com.cowlark.stellation2.common.model.CPlayer;
import com.cowlark.stellation2.server.Log;
import com.cowlark.stellation2.server.db.CClass;
import com.cowlark.stellation2.server.db.DataMangler;
import com.cowlark.stellation2.server.db.MapOfServerObjects;
import com.cowlark.stellation2.server.db.Property;
import com.cowlark.stellation2.server.db.ServerDB;

@CClass(name = CPlayer.class)
public class SPlayer extends SObject
{
    private static final long serialVersionUID = -954295233192947332L;

	@Property(scope = S.GLOBAL)
    private String _uid;

    @Property
    private String _password;
    
    @Property(scope = S.GLOBAL)
    private String _name;

    @Property(scope = S.GLOBAL)
    private String _empire;

    @Property(scope = S.OWNER)
    private boolean _administrator;
    
    @Property(scope = S.OWNER)
    private MapOfServerObjects<SFleet> _fleets = new MapOfServerObjects<SFleet>();
    
    @Property
    private LogMessageStore _log = new LogMessageStore();
    
    private Map<Long, Integer> _visibleObjects = new TreeMap<Long, Integer>();

    public SPlayer initialise(String uid, String password, String empire, String name)
    {
    	super.initialise();
    	
    	_uid = uid;
    	_password = password;
    	_name = name;
    	_empire = empire;
    	_administrator = true;
    	
    	setOwner(this);
    	
    	SStar star = getGalaxy().getRandomStar();
    	
    	try
    	{
	    	SFleet fleet = createFleet(star, _empire+"'s starter fleet");
	    	
	    	fleet.createJumpship()
	    		.makeAlive();
	    	
	    	fleet.createTug()
	    		.makeAlive()
	    		.createBasicFactory();
	    	
	    	fleet.createCargoship()
	    		.setCargo(new Resources(100000.0, 100000.0, 100000.0))
	    		.makeAlive();
	    	
	    	log("Intergalactic transit bubble arrival. " + _empire +
	    			" of " + _name + " has arrived in the galaxy.");
    	}
    	catch (StellationException e)
    	{
    	}
    	
    	return this;
    }
    
	public String getUid()
    {
    	return _uid;
    }

	public SPlayer setUid(String uid)
    {
    	_uid = uid;
    	dirty();
    	return this;
    }

	public String getPassword()
	{
		return _password;
	}
	
	public void setPassword(String password)
	{
		_password = password;
	}
	
    public String getName()
    {
    	return _name;
    }

	public SPlayer setName(String name)
    {
    	_name = name;
    	dirty();
    	return this;
    }

	public String getEmpire()
    {
    	return _empire;
    }

	public SPlayer setEmpire(String empire)
    {
    	_empire = empire;
    	dirty();
    	return this;
    }

	public MapOfServerObjects<SFleet> getFleets()
    {
    	return _fleets;
    }

	public SPlayer setFleets(MapOfServerObjects<SFleet> fleets)
    {
    	_fleets = fleets;
    	dirty();
    	return this;
    }

    public Map<Long, Integer> getVisibleObjects()
    {
	    return _visibleObjects;
    }
    
    public void checkAdministrator() throws NotAdministratorException
    {
    	if (!_administrator)
    		throw new NotAdministratorException();
    }
    
	public SFleet createFleet(SStar star, String name)
			throws FleetAlreadyHasThisNameException
    {
    	if (_fleets.containsKey(name))
    		throw new FleetAlreadyHasThisNameException(name);
    	
    	SFleet fleet = new SFleet();
    	fleet.initialise(star, this, name);
    	_fleets.put(name, fleet);
    	dirty();
    	
    	return fleet;
    }
    
    public void renameFleet(SFleet fleet, String name)
    		throws FleetAlreadyHasThisNameException
    {
    	if (fleet.getName().equals(name))
    		return;
    	
    	if (_fleets.containsKey(name))
    		throw new FleetAlreadyHasThisNameException(name);
    	
    	_fleets.remove(fleet.getName());
    	_fleets.put(name, fleet);
    	
    	dirty();
    }
    
    private void addWithContents(SObject object)
    {
		_visibleObjects.put(object.getId(), S.OWNER);
		for (SObject o : object)
			addWithContents(o);
    }
    
    public void updateVisibility()
    {
    	if (_visibleObjects == null)
    	    _visibleObjects = new HashMap<Long, Integer>();
    	else
    		_visibleObjects.clear();
    	
    	/* The player and the universe itself are visible! */
    	
    	_visibleObjects.put(getId(), S.OWNER);
    	_visibleObjects.put(S.UNIVERSE, S.GLOBAL);
    	
    	/* The galaxy and all (glowing) stars are visible. */
    	
    	SGalaxy galaxy = getGalaxy();
    	_visibleObjects.put(galaxy.getId(), S.GLOBAL);
    	for (Long id : galaxy.getVisibleStars().getIds())
    	{
   			_visibleObjects.put(id, S.GLOBAL);
    	}

    	/* We can see where each fleet that has a jumpship is. */
    	
    	for (SObject o : _fleets.values())
    	{
    		SFleet fleet = o.toFleet();
    		if (fleet.getJumpshipCount() > 0)
    		{
	    		/* We can see both the fleet and its star. */
	    		
	    		_visibleObjects.put(fleet.getId(), S.OWNER);
	    		_visibleObjects.put(fleet.getLocation().getId(), S.LOCAL);
	    		
	    		/* We can also see the contents of the fleet. */
	    		
	    		for (SObject oo : fleet)
	    			addWithContents(oo);
    		}
    	}
    	
    	dirty();
    }
    
    public void addChangedItemsToBatch(UpdateBatch batch, long since)
    {
    	long now = System.currentTimeMillis();
    	ServerDB db = ServerDB.Instance;
    	for (long id : _visibleObjects.keySet())
    	{
    		SObject object = (SObject) db.getImpl(id);
    		if (object.getChangedTime() > since)
    		{
    			int scope = _visibleObjects.get(id);
    			CObject cobject = DataMangler.export(object, scope);
    			cobject.setId(id);
    			cobject.setScope(scope);
    			//cobject = object.createPublic(scope);
    			
    			switch (scope)
    			{
    				case S.OWNER:
    				case S.LOCAL:
    					cobject.setContents(
    							(ListOfClientObjects) object.getContents().getClient());
    					break;
    					
    				case S.GLOBAL:
    				{
    					ListOfClientObjects cset = new ListOfClientObjects();
    					
    					HashSet<Long> childids = new HashSet<Long>(object.getContents().getIds());
    					childids.retainAll(_visibleObjects.keySet());

    					for (long childid : childids)
    						cset.add(childid);
		    			
		    			cobject.setContents(cset);
    				}
    			}
    	    	
    			batch.add(cobject);
    		}
    	}
    	
    	batch.getVisibleObjects().addAll(_visibleObjects.keySet());
    	long delta = System.currentTimeMillis() - now;
    	Log.log("batched %d objects out of %d in %d ms (%d ms per object)",
    			batch.getUpdatedObjects().size(),
    			_visibleObjects.size(),
    			delta,
    			delta / batch.getUpdatedObjects().size());

    	now = SUniverse.getStaticUniverse().getCurrentTime();
    	batch.setLogUpdates(_log.getSubset(since, now));
    	batch.setTime(now);
    }
    
    public SPlayer log(String message)
    {
    	long t = SUniverse.getStaticUniverse().getCurrentTime();
    	_log.add(t, message);
    	
//    	JID jid = new JID("david.given@gmail.com");
//    	Message msg = new MessageBuilder()
//    		.withRecipientJids(jid)
//    		.withFromJid(new JID("stellation2@appspot.com"))
//    		.withBody(Utils.renderTime(t) + ": " + message)
//    		.build();
//    	
//    	XMPPService xmpp = XMPPServiceFactory.getXMPPService();
//    	xmpp.sendMessage(msg);
    	
    	return this;
    }
}
