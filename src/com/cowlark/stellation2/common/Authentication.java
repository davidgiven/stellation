/* A set of user authentication data.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/Authentication.java,v $
 * $Date: 2009/09/06 17:59:15 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Authentication implements IsSerializable
{
	private String _uid;
	private String _password;
	
	public Authentication()
	{
		_uid = _password = null;
	}
	
	public Authentication(String uid, String password)
    {
		_uid = uid;
		_password = password;
    }
	
	public String getUid()
    {
	    return _uid;
    }
	
	public String getPassword()
    {
	    return _password;
    }
}
