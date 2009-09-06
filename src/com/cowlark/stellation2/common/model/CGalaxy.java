/* Client-side galaxy.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/model/CGalaxy.java,v $
 * $Date: 2009/09/06 17:59:15 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common.model;

import com.cowlark.stellation2.common.db.ListOfClientObjects;
import com.cowlark.stellation2.server.db.Property;
import com.google.gwt.user.client.rpc.IsSerializable;

public class CGalaxy extends CObject implements IsSerializable
{
	@Property
    private ListOfClientObjects _visibleStars = new ListOfClientObjects();

    public CGalaxy()
    {
    }
}
