/* Server-side universe.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/server/model/SUniverse.java,v $
 * $Date: 2009/09/06 17:59:15 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.server.model;

import java.util.Comparator;
import java.util.PriorityQueue;
import com.cowlark.stellation2.common.Authentication;
import com.cowlark.stellation2.common.S;
import com.cowlark.stellation2.common.db.DBRef;
import com.cowlark.stellation2.common.db.Database;
import com.cowlark.stellation2.common.exceptions.AuthenticationException;
import com.cowlark.stellation2.common.exceptions.StellationException;
import com.cowlark.stellation2.common.exceptions.UsernameInUseException;
import com.cowlark.stellation2.common.model.CUniverse;
import com.cowlark.stellation2.server.db.CClass;
import com.cowlark.stellation2.server.db.ListOfServerObjects;
import com.cowlark.stellation2.server.db.MapOfServerObjects;
import com.cowlark.stellation2.server.db.Property;

@CClass(name = CUniverse.class)
public class SUniverse extends SObject
{
    private static final long serialVersionUID = 6082770378108473372L;

	@Property
	private long _nextId;
    
    @Property(scope = S.GLOBAL)
	private DBRef<SGalaxy> _galaxy = new DBRef<SGalaxy>();
    
    @Property(scope = S.GLOBAL)
	private MapOfServerObjects<SPlayer> _players =
		new MapOfServerObjects<SPlayer>();

    @Property
    private ListOfServerObjects<STimer> _timers =
    	new ListOfServerObjects<STimer>();
    private transient PriorityQueue<Long> _sortedTimers;
    
    private transient long _currentTime;
    
	public static SUniverse getStaticUniverse()
	{
		SUniverse universe = Database.get(S.UNIVERSE);
		if (universe == null)
		{
			universe = new SUniverse();
			universe.initialise();
		}
		return universe;
	}
	
	public SUniverse initialise()
    {
		super.initialise(S.UNIVERSE);
		_nextId = 1;
		return this;
    }
	
	public long getNextId()
	{
		return _nextId++;
	}

	public SGalaxy getGalaxy()
    {
		SGalaxy galaxy;
		if (_galaxy.isNull())
	    {
			galaxy = new SGalaxy()
				.initialise();
			_galaxy = new DBRef<SGalaxy>(galaxy);
	    }
		else
			galaxy = _galaxy.get();

	    return galaxy;
    }
	
	public SUniverse setGalaxy(DBRef<SGalaxy> galaxy)
    {
	    _galaxy = galaxy;
	    dirty();
	    return this;
    }
	
	public SPlayer getPlayer(String uid)
	{
		return (SPlayer) _players.get(uid);
	}
	
	public SPlayer authenticatePlayer(Authentication auth)
			throws AuthenticationException
	{
		SPlayer player = getPlayer(auth.getUid());
		if ((player != null) && (player.getPassword().equals(auth.getPassword())))
			return player;
		throw new AuthenticationException(auth.getUid());
	}
		
	public SPlayer createPlayer(String uid, String password, String empire, String name)
		throws StellationException
	{
		if (_players.containsKey(uid))
			throw new UsernameInUseException(uid);
		
		SPlayer player = new SPlayer()
			.initialise(uid, password, empire, name);
		_players.put(uid, player);
		dirty();
		return player;
	}

	private void initSortedTimerList()
	{
		if (_sortedTimers == null)
		{
			_sortedTimers = new PriorityQueue<Long>(
					10,
					new Comparator<Long>()
					{
						public int compare(Long o1, Long o2)
						{
							STimer t1 = (STimer) Database.get(o1);
							STimer t2 = (STimer) Database.get(o2);
							long l1 = 0;
							if (t1 != null)
								l1 = t1.getExpiry();
							long l2 = 0;
							if (t2 != null)
								l2 = t2.getExpiry();
							
						    return ((Long) l1).compareTo(l2);
						}
					}
			);
			
			_sortedTimers.addAll(_timers.getData());
		}
	}
	
	public void addTimer(STimer timer)
	{
		initSortedTimerList();
		_timers.add(timer);
		_sortedTimers.add(timer.getId());
	}
	
	public void removeTimer(STimer timer)
	{
		initSortedTimerList();
		_timers.remove(timer);
		_sortedTimers.remove(timer.getId());
	}
	
	public void timePassing()
	{
		initSortedTimerList();
		
		for (;;)
		{
			if (_sortedTimers.isEmpty())
				break;
			
			long id = _sortedTimers.peek();
			STimer t = Database.get(id);
			if (!t.pending())
				break;
		
			_currentTime = t.getExpiry();
			t.fire();
		}
		
		_currentTime = System.currentTimeMillis();
	}
	
	public long getCurrentTime()
    {
	    return _currentTime;
    }
}
