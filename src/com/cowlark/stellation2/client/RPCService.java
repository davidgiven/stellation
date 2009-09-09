/* RPC interface (client-side).
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/RPCService.java,v $
 * $Date: 2009/09/09 23:17:34 $
 * $Author: dtrg $
 * $Revision: 1.3 $
 */

package com.cowlark.stellation2.client;

import java.util.Map;
import com.cowlark.stellation2.common.Authentication;
import com.cowlark.stellation2.common.UpdateBatch;
import com.cowlark.stellation2.common.exceptions.StellationException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("greet")
public interface RPCService extends RemoteService
{
	void createUser(String uid, String password, String empire, String name)
		throws StellationException;

	UpdateBatch ping(Authentication auth, long since)
		throws StellationException;
	
	Map<String, String> adminLoadObject(Authentication auth, long id)
		throws StellationException;
	
	void adminSaveObject(Authentication auth, long id,
			Map<String, String> map)
		throws StellationException;
	
	/* Cargoship */
	
	UpdateBatch cargoshipLoadUnload(Authentication auth, long since,
			long id, double antimatter, double metal, double organics)
		throws StellationException;
}
