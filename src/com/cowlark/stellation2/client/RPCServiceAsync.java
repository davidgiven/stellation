/* RPC interface (async version).
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/RPCServiceAsync.java,v $
 * $Date: 2009/09/09 23:17:34 $
 * $Author: dtrg $
 * $Revision: 1.3 $
 */

package com.cowlark.stellation2.client;

import java.util.Map;
import com.cowlark.stellation2.common.Authentication;
import com.cowlark.stellation2.common.UpdateBatch;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RPCServiceAsync
{
	void createUser(String uid, String password, String empire, String name,
			AsyncCallback<Void> callback);

	void ping(Authentication auth, long since,
			AsyncCallback<UpdateBatch> callback);
	
	void adminLoadObject(Authentication auth, long id,
			AsyncCallback<Map<String, String>> callback);
	
	void adminSaveObject(Authentication auth, long id,
			Map<String, String> map,
			AsyncCallback<Void> callback);
	
	/* Cargoship */
	
	void cargoshipLoadUnload(Authentication auth, long since,
			long id, double antimatter, double metal, double organics,
			AsyncCallback<UpdateBatch> callback);
}
