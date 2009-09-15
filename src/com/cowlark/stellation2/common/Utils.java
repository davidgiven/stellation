/* Generic utilities.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/Utils.java,v $
 * $Date: 2009/09/15 23:14:36 $
 * $Author: dtrg $
 * $Revision: 1.4 $
 */

package com.cowlark.stellation2.common;

import java.util.LinkedList;
import java.util.List;
import com.cowlark.stellation2.client.ui.DataGroup;
import com.cowlark.stellation2.client.ui.DataRow;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class Utils
{
	public static int random(int top)
    {
    	return (int) (Math.abs(Math.random()) * top);
    }

	public static String random(String[] array)
    {
		int element = random(array.length);
    	return array[element];
    }
	
	public static double round(double factor, double i)
	{
		return Math.floor(factor * i) / factor;
	}
	
	public static String renderTime(long t)
	{
    	long t1 = t / 3600; /* to thousands of hours */
    	long t3 = t1 % 1000;
    	t1 /= 1000; /* to hours */
    	long t2 = t1 % 1000;
    	t1 /= 1000; /* to thousands of hours */
    	
    	StringBuilder sb = new StringBuilder();
    	sb.append(t1);
    	sb.append('.');
    	if (t2 < 100)
    		sb.append('0');
    	if (t2 < 10)
    		sb.append('0');
    	sb.append(t2);
    	sb.append('.');
    	if (t3 < 100)
    		sb.append('0');
    	if (t3 < 10)
    		sb.append('0');
    	sb.append(t3);
    	return sb.toString();
	}

	public static Widget renderResources(Resources r)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("A: ");
		sb.append(r.getAntimatter());
		sb.append(" M: ");
		sb.append(r.getMetal());
		sb.append(" O: ");
		sb.append(r.getOrganics());

		return new Label(sb.toString());
	}
	
	public static Element findElementByClass(Element element, String className)
	{
		String s = element.getClassName();
		if (s.equals(className))
			return element;
		
		Element e = element.getFirstChildElement();
		while (e != null)
		{
			Element found = findElementByClass(e, className);
			if (found != null)
				return found;
			
			e = e.getNextSiblingElement();
		}
		
		return null;
	}

	public static String toEncoding(Iterable<String> list)
    {
    	StringBuilder sb = new StringBuilder();
    	boolean separator = false;
    	for (String s : list)
    	{
    		if (separator)
    			sb.append('|');
    		separator = true;
    		
    		int len = s.length();
    		for (int i = 0; i < len; i++)
    		{
    			char c = s.charAt(i);
    			switch (c)
    			{
    				case '|':
    					sb.append("\\|");
    					break;
    					
    				case '\\':
    					sb.append("\\\\");
    					break;
    					
    				case '\n':
    					sb.append("\\n");
    					break;
    					
    				default:
    					sb.append(c);
    			}
    		}
    	}
    	
    	return sb.toString();
    }

	public static List<String> fromEncoding(String src)
    {
    	LinkedList<String> list = new LinkedList<String>();
    	StringBuilder sb = new StringBuilder();
    	
    	boolean escape = false;
    	int len = src.length();
    	for (int i = 0; i < len; i++)
    	{
    		char c = src.charAt(i);
    		if (escape)
    		{
    			switch (c)
    			{
    				case 'n':
    					sb.append('\n');
    					break;
    					
    				default:
    					sb.append(c);
    			}
    			escape = false;
    		}
    		else
    		{
    			switch (c)
    			{
    				case '|':
    					list.addLast(sb.toString());
    					sb.setLength(0);
    					break;
    				
    				case '\\':
    					escape = true;
    					continue;
    				
    				default:
    					sb.append(c);
    					break;
    			}
    		}
    	}
    	
    	list.addLast(sb.toString());
    	return list;
    }
	
	public static void fillGrid(Grid grid, Object[][] objects)
	{
		for (int y = 0; y < objects.length; y++)
		{
			Object[] row = objects[y];
			for (int x = 0; x < row.length; x++)
			{
				Object o = row[x];
				
				if (o == null)
					continue;
				else if (o instanceof String)
					grid.setText(y, x, (String) o);
				else
					grid.setWidget(y, x, (Widget) o);
			}
		}
	}
	
	public static void fillGroup(DataGroup group, Object[][] objects)
	{
		for (Object[] row : objects)
		{
			final Widget[] widgets = new Widget[row.length];
			for (int x = 0; x < row.length; x++)
			{
				Object o = row[x];
				if (o instanceof String)
					o = new Label((String) o);
				widgets[x] = (Widget) o;
			}
			
			group.add(new DataRow()
				{
					public Widget[] getData()
					{
						return widgets;
					}
				}
			);	
		}
	}
}
