/* Globals.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/S.java,v $
 * $Date: 2009/09/23 21:26:31 $
 * $Author: dtrg $
 * $Revision: 1.4 $
 */

package com.cowlark.stellation2.common;

import com.google.gwt.i18n.client.NumberFormat;

public class S
{
	/* Special object IDs. */
	
	public final static long NULL = 0;
	public final static long UNIVERSE = -1;
	
	/* Server settings */
	
	public final static int NUMBER_OF_STARS = 400;
	public final static int GALAXY_RADIUS = 20;
	public final static long TICK = 30 * 1000;
	public final static int UPDATE_TIME = 60*1000; /* one minute */
	
	/* Number formats. */
	
	public final static NumberFormat COORD_FORMAT = NumberFormat.getFormat("0.0");
	public final static NumberFormat BRIGHTNESS_FORMAT = NumberFormat.getFormat("0.0");
	public final static NumberFormat MASS_FORMAT = NumberFormat.getFormat("0.0");
	public final static NumberFormat DURATION_FORMAT = NumberFormat.getFormat("0.0");
	
	/* Timer magic. */
	
	public final static long CANCELTIMER = -1;
	
	/* Scope types. */
	
	public final static int PRIVATE = 0;
	public final static int OWNER = 1;
	public final static int LOCAL = 2;
	public final static int GLOBAL = 3;
}
