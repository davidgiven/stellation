/* A word-wrapped label.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/ui/WrappedLabel.java,v $
 * $Date: 2009/09/07 21:48:10 $
 * $Author: dtrg $
 * $Revision: 1.3 $
 */

package com.cowlark.stellation2.client.ui;

import com.google.gwt.user.client.ui.InlineLabel;

public class WrappedLabel extends InlineLabel
{
	public WrappedLabel(String s)
    {
		super(s);
		setStylePrimaryName("Text");
    }
	
	public WrappedLabel()
	{
		this("");
	}
}
