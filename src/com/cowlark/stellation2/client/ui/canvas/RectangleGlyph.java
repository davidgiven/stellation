/* A filled rectangle.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/ui/canvas/RectangleGlyph.java,v $
 * $Date: 2009/09/06 17:58:31 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.ui.canvas;

import com.google.gwt.user.client.DOM;

public class RectangleGlyph extends Glyph
{
	public RectangleGlyph(int width, int height)
    {
		super(DOM.createDiv());
		setSize(width, height);
    }
}
