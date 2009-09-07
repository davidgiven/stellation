/* A word-wrapped label.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/ui/NonWrappedLabel.java,v $
 * $Date: 2009/09/07 21:48:10 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.ui;

import com.google.gwt.user.client.ui.Label;

public class NonWrappedLabel extends Label
{
	public NonWrappedLabel(String s)
    {
		super(s);
		setStylePrimaryName("Text");
    }
	
	public NonWrappedLabel()
	{
		this("");
	}
}
