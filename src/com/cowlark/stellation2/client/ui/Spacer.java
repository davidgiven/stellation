/* An empy space.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/ui/Spacer.java,v $
 * $Date: 2009/09/06 17:58:31 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.ui;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class Spacer extends Composite
{
	public Spacer()
    {
		Widget w = new Label();
		w.setWidth("0.5em");
		w.setHeight("0.5em");
		initWidget(w);
    }
}
