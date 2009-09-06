/* Handler callback for database updates.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/ResponseHandler.java,v $
 * $Date: 2009/09/06 17:58:31 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client;

import com.cowlark.stellation2.common.UpdateBatch;
import com.cowlark.stellation2.common.exceptions.AuthenticationException;
import com.google.gwt.user.client.rpc.AsyncCallback;

class ResponseHandler implements AsyncCallback<UpdateBatch>
{
    public void onFailure(Throwable caught)
	{
    	if (caught instanceof AuthenticationException)
    		Stellation2.authenticationFailure();
	}
    
    public void onSuccess(UpdateBatch result)
	{
    	ClientDB.processBatch(result);
	}
}