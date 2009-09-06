/* Name generator helper.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/server/NameGenerator.java,v $
 * $Date: 2009/09/06 17:59:16 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.server;

import com.cowlark.stellation2.common.Utils;

public class NameGenerator
{
	private static String[] _syllables1 = new String[] {
	        "An", "Ca", "Jo", "Ka", "Kri", "Da", "Re", "De", "Ed", "Ma", "Ni",
	        "Qua", "Qa", "Li", "La", "In", "On", "An", "Un", "Ci", "Cu", "Ce",
	        "Co", "Xa", "Xef", "Xii", "Xo'o", "Xu", "Ram", "Noq", "Mome", "Pawa",
	        "Limi", "Ney"
	};
	
	private static String[] _syllables2 = new String[] {
	        "the", "ru", "shu", "be", "po", "fol", "boo", "qwa", "xi", "lo", "fi"
	};
	
	private static String[] _syllables3 = new String[] {
	        "drew", "rine", "vid", "a", "na", "sten", "niel", "cca", "vin", "ven",
	        "cor", "rion", "rath", "tong", "lar", "bol", "ting", "narg", "aq", "blan",
	        "sim", "pil", "rib", "org", "lig", "zim", "frob", "cha", "poo", "tang"
	};

	public static String generateName()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(Utils.random(_syllables1));
		if (Utils.random(2) == 0)
			sb.append(Utils.random(_syllables2));
		sb.append(Utils.random(_syllables3));
		
		return sb.toString();
	}
}
