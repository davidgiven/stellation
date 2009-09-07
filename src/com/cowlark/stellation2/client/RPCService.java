/* RPC interface (client-side).
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/RPCService.java,v $
 * $Date: 2009/09/07 21:49:14 $
 * $Author: dtrg $
 * $Revision: 1.2 $
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
}
