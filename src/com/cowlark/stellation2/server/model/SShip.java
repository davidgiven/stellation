/* Server-side generic ship.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/server/model/SShip.java,v $
 * $Date: 2009/09/06 17:59:15 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.server.model;

import com.cowlark.stellation2.common.model.CShip;
import com.cowlark.stellation2.server.db.CClass;

@CClass(name = CShip.class)
public abstract class SShip extends SUnit
{
    private static final long serialVersionUID = 2808034436235627328L;
}
