/* Logger helper class.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/server/Log.java,v $
 * $Date: 2009/09/06 17:59:16 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.server;

import java.util.Date;

public class Log
{
	public static void log(String s, Object... o)
	{
		long ms = new Date().getTime() % 1000000;
		double t = (double)ms / 1000.0;
		
		System.out.print(t + ": ");
		System.out.println(String.format(s, o));
	}
}
