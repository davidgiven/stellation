/* The overall game screen.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/data/PropertyStore.java,v $
 * $Date: 2009/09/16 23:11:51 $
 * $Author: dtrg $
 * $Revision: 1.4 $
 */

package com.cowlark.stellation2.common.data;

import com.cowlark.stellation2.common.Resources;

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
	                "supply vessels in operation.")
	        .setMass(1000.0)
	        .setMaxDamage(300.0)
	        .setBuildCost(new Resources(10000.0, 1000.0, 5000.0))
	        .setBuildTime(3.0)
	        .setMaintenanceCost(new Resources(2.0, 1.0, 0.0));
	
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
					"exact resonant frequency of the flaw is known.")
		    .setMass(5000.0)
		    .setMaxDamage(1000.0)
		    .setBuildCost(new Resources(20000.0, 1000.0, 10000.0))
		    .setBuildTime(5.0)
		    .setMaintenanceCost(new Resources(5.0, 2.0, 0.0));
	
	public static Properties Tug =
		new Properties()
			.setName("Tug")
			.setDescription(
					"Tugs are small, powerful craft used to tow otherwise " +
			    	"unpowered vessels and other artifacts.")
		    .setMass(1000.0)
		    .setMaxDamage(100.0)
		    .setBuildCost(new Resources(8000.0, 1000.0, 3000.0))
		    .setBuildTime(2.0)
		    .setMaintenanceCost(new Resources(4.0, 1.0, 0.0));
	
	public static Properties BasicFactory =
		new Properties()
			.setName("Basic Factory")
			.setDescription(
					"The basic factory is a semi-automated device that, " +
					"given the appropriate quantities of basic resources, " +
					"can produce (one at a time) any of a considerable " +
					"number of units.")
			.setMass(10000.0)
			.setMaxDamage(5000.0)
			.setBuildCost(new Resources(30000.0, 2000.0, 20000.0))
			.setBuildTime(10.0);
			
}
