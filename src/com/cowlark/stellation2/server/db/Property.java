/* Annotation describing a database property.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/server/db/Property.java,v $
 * $Date: 2009/09/06 17:59:15 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.server.db;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import com.cowlark.stellation2.common.S;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Property
{
	public int scope() default S.PRIVATE;
}
