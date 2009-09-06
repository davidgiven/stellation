/* The overall game screen.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/data/Properties.java,v $
 * $Date: 2009/09/06 22:15:12 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common.data;

public class Properties
{
	private String _name = "Unnamed Object";
	private String _description = "This object has no description yet.";
	
	public Properties()
    {
    }
	
	public String getName()
    {
	    return _name;
    }
	
	public Properties setName(String name)
    {
	    _name = name;
	    return this;
    }
	
	public String getDescription()
    {
	    return _description;
    }
	
	public Properties setDescription(String description)
    {
	    _description = description;
	    return this;
    }
}
