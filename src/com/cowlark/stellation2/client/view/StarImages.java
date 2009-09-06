/* Bundle containing all the star artwork.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/view/StarImages.java,v $
 * $Date: 2009/09/06 17:58:31 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.view;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

public interface StarImages extends ImageBundle
{
	@Resource(value = "star.png")
	public AbstractImagePrototype star();
}
