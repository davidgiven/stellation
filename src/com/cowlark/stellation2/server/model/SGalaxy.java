/* Server-side galaxy.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/server/model/SGalaxy.java,v $
 * $Date: 2009/09/06 17:59:15 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.server.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import com.cowlark.stellation2.common.Asteroids;
import com.cowlark.stellation2.common.Pair;
import com.cowlark.stellation2.common.Resources;
import com.cowlark.stellation2.common.S;
import com.cowlark.stellation2.common.Utils;
import com.cowlark.stellation2.common.model.CGalaxy;
import com.cowlark.stellation2.server.NameGenerator;
import com.cowlark.stellation2.server.db.CClass;
import com.cowlark.stellation2.server.db.ListOfServerObjects;
import com.cowlark.stellation2.server.db.Property;

@CClass(name = CGalaxy.class)
public class SGalaxy extends SObject
{
    private static final long serialVersionUID = -7850982306143615792L;
    
	@Property(scope = S.GLOBAL)
    private ListOfServerObjects<SStar> _visibleStars =
    	new ListOfServerObjects<SStar>();
    
    /* Create a shiny new galaxy. */
    
    public SGalaxy initialise()
    {
    	super.initialise();
    	
    	/* Determine the locations of all the stars in the galaxy. */
    	
    	Map<Integer, Pair<Double>> postab = new HashMap<Integer, Pair<Double>>();
    	while (postab.size() < S.NUMBER_OF_STARS)
    	{
    		double r = Math.random() * S.GALAXY_RADIUS;
    		double t = Math.random() * 2.0 * Math.PI;
    		
    		double x = Utils.round(10, r * Math.sin(t));
    		double y = Utils.round(10, r * Math.cos(t));
    		
    		int hash = ((int)x + S.GALAXY_RADIUS) * (S.GALAXY_RADIUS*2) +
    			((int)y + S.GALAXY_RADIUS);
    		
    		if (!postab.containsKey(hash))
    		{
    			Pair<Double> p = new Pair<Double>();
    			p.x = x;
    			p.y = y;
    			postab.put(hash, p);
    		}
    	}
    	
    	/* Now generate their names. */
    	
    	Set<String> nametab = new HashSet<String>();
    	while (nametab.size() < S.NUMBER_OF_STARS)
    	{
    		String n = NameGenerator.generateName();
    		if (!nametab.contains(n))
    			nametab.add(n);
    	}
    	
    	/* And generate the stars themselves. */

    	Iterator<Pair<Double>> posi = postab.values().iterator();
    	Iterator<String> namei = nametab.iterator();
    	for (int i = 0; i < S.NUMBER_OF_STARS; i++)
    	{
    		Pair<Double> pos = posi.next();
    		String name = namei.next();
    		
    		createStar(name, pos.x, pos.y,
    				1.0 + (Math.random() * 9.0),
    				null,
    				new Asteroids(Utils.random(10) + 10, Utils.random(10) + 10));
    	}
    	
    	return this;
    }
    
    @Override
    public void onAdditionOf(SObject object)
    {    	
        super.onAdditionOf(object);
        
        SStar star = (SStar) object;
        if (star.getBrightness() > 0.0)
        	_visibleStars.add(star);
    }
    
    @Override
    public void onRemovalOf(SObject object)
    {
    	_visibleStars.remove(object);
        super.onRemovalOf(object);
    }

    public SStar getRandomStar()
    {
    	return _visibleStars.getRandomElement().toStar();
    }
    
    public ListOfServerObjects<SStar> getVisibleStars()
    {
    	return _visibleStars;
    }
    
    public SStar createStar(String name, double x, double y, double brightness,
    		Resources resources, Asteroids asteroids)
    {
    	SStar star = new SStar();
    	star.initialise(name, x, y, brightness, resources, asteroids);
    	add(star);
    	dirty();
    	return star;
    }
    
}
