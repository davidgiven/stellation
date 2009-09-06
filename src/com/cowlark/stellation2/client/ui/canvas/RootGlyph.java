/* A glyph containing a subcanvas.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/ui/canvas/RootGlyph.java,v $
 * $Date: 2009/09/06 17:58:31 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.ui.canvas;

import com.google.gwt.user.client.DOM;

public class RootGlyph extends Glyph
{
	public RootGlyph()
    {
		super(DOM.createDiv());
    }
}
