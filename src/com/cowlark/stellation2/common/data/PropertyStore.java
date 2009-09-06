/* The overall game screen.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/data/PropertyStore.java,v $
 * $Date: 2009/09/06 22:15:12 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common.data;

public class PropertyStore
{
	public static Properties Generic = new Properties();
	
	public static Properties Cargoship =
		new Properties()
			.setName("Cargoship")
			.setDescription(
	                "Cargo ships can carry large quantities of the three " +
	                "main commodities. They can be used to transfer resources " +
	                "from one star system to another, and are also used to " +
	                "supply vessels in operation.");
	
	public static Properties Fleet =
		new Properties()
			.setName("Fleet");
	
	public static Properties Jumpship =
		new Properties()
			.setName("Jumpship")
			.setDescription(
					"A jumpship is used to carry the spatial flaw used to " +
					"bootstrap wormholes. Its primary function is to carry fleets " +
					"of other ships through interstellar distances. The spatial " +
					"flaw can also be used to send instantaneous communications " +
					"from one jumpship to another, but only ones for which the " +
					"exact resonant frequency of the flaw is known.");
	
	public static Properties Tug =
		new Properties()
			.setName("Tug")
			.setDescription(
					"Tugs are small, powerful craft used to tow otherwise " +
			    	"unpowered vessels and other artifacts.");
}
