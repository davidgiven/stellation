/* Annotation describing which client-side class corresponds to a server-side one.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/server/db/CClass.java,v $
 * $Date: 2009/09/06 17:59:15 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.server.db;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import com.cowlark.stellation2.common.model.CObject;

@Retention(RetentionPolicy.RUNTIME)
public @interface CClass
{
	public Class<? extends CObject> name();
}
