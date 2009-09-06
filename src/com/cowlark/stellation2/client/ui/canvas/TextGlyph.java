/* A glyph containing text.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/ui/canvas/TextGlyph.java,v $
 * $Date: 2009/09/06 17:58:31 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.ui.canvas;

import com.google.gwt.user.client.DOM;

public class TextGlyph extends Glyph
{
	public TextGlyph(String label)
    {
		super(DOM.createLabel());
		setText(label);
    }
	
	public TextGlyph setText(String label)
	{
		getElement().setInnerText(label);
		return this;
	}
}
