/* This object can be serialised.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/db/HasDBRepresentation.java,v $
 * $Date: 2009/09/06 17:59:16 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common.db;

public interface HasDBRepresentation
{
	public Iterable<String> toDBRepresentation();
	public void fromDBRepresentation(Iterable<String> value);
	
	public Object getClient();
}
