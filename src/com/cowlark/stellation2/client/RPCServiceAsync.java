/* RPC interface (async version).
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/RPCServiceAsync.java,v $
 * $Date: 2009/09/06 17:58:31 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client;

import java.util.Map;
import com.cowlark.stellation2.common.Authentication;
import com.cowlark.stellation2.common.UpdateBatch;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RPCServiceAsync
{
	void createUser(String uid, String password, String name, String empire,
			AsyncCallback<Void> callback);

	void ping(Authentication auth, long since,
			AsyncCallback<UpdateBatch> callback);
	
	void adminLoadObject(Authentication auth, long id,
			AsyncCallback<Map<String, String>> callback);
	
	void adminSaveObject(Authentication auth, long id,
			Map<String, String> map,
			AsyncCallback<Void> callback);
}
