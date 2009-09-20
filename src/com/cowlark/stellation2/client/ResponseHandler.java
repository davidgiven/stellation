/* Handler callback for database updates.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/ResponseHandler.java,v $
 * $Date: 2009/09/20 21:50:35 $
 * $Author: dtrg $
 * $Revision: 1.2 $
 */

package com.cowlark.stellation2.client;

import com.cowlark.stellation2.common.UpdateBatch;
import com.cowlark.stellation2.common.exceptions.AuthenticationException;
import com.cowlark.stellation2.common.exceptions.ResourcesNotAvailableException;
import com.google.gwt.user.client.rpc.AsyncCallback;

class ResponseHandler implements AsyncCallback<UpdateBatch>
{
    public void onFailure(Throwable caught)
	{
    	if (caught instanceof AuthenticationException)
    		Stellation2.authenticationFailure();
    	else if (caught instanceof ResourcesNotAvailableException)
    		Stellation2.alert("Resources not available (did you remember to unload some from your cargoship?");
	}
    
    public void onSuccess(UpdateBatch result)
	{
    	ClientDB.processBatch(result);
	}
}