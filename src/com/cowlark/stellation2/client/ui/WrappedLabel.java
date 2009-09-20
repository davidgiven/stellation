/* A word-wrapped label.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/ui/WrappedLabel.java,v $
 * $Date: 2009/09/20 22:12:52 $
 * $Author: dtrg $
 * $Revision: 1.4 $
 */

package com.cowlark.stellation2.client.ui;

import com.google.gwt.user.client.ui.Label;

public class WrappedLabel extends Label
{
	public WrappedLabel(String s)
    {
		super(s, true);
		addStyleName("Text");
    }
	
	public WrappedLabel()
	{
		this("");
	}
}
