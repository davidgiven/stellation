/* Data serialisation/deserialisation/conversion methods.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/server/db/DataMangler.java,v $
 * $Date: 2009/09/06 17:59:15 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.server.db;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.cowlark.stellation2.common.Utils;
import com.cowlark.stellation2.common.db.HasDBRepresentation;
import com.cowlark.stellation2.common.model.CObject;
import com.cowlark.stellation2.server.model.SObject;

public class DataMangler
{
    private static Map<Class<?>, Map<String, Field>> _fieldCaches =
    	new HashMap<Class<?>, Map<String,Field>>();
	
	private static Map<String, Field> getFieldCache(Object o)
	{
		synchronized (_fieldCaches)
		{
			Class<?> c = o.getClass();
			Map<String, Field> fieldCache = _fieldCaches.get(c);
			
			if (fieldCache == null)
			{
				fieldCache = new HashMap<String, Field>();
				_fieldCaches.put(c, fieldCache);
				
				while (c != null)
				{
					for (Field field : c.getDeclaredFields())
					{
						if (field.getAnnotation(Property.class) != null)
						{
							field.setAccessible(true);
							String name = field.getName().intern();
							
							assert (!fieldCache.containsKey(name));
							
							fieldCache.put(name, field);
						}
					}
					
					c = c.getSuperclass();
				}
			}
			
			return fieldCache;
		}
	}

	public static Map<String, String> serialise(Object object)
	{
		HashMap<String, String> map = new HashMap<String, String>();
		
		Class<?> c = object.getClass();
		Map<String, Field> fieldCache = getFieldCache(object);
		
		map.put("class", c.getName());
		for (Map.Entry<String, Field> entry : fieldCache.entrySet())
		{
			try
			{
				String name = entry.getKey();
				Field field = entry.getValue();
				Object value = field.get(object);
				
				if ((value instanceof Number) ||
					(value instanceof String) ||
					(value instanceof Boolean))
					map.put(name, value.toString());
				else if (value instanceof Date)
					map.put(name, Long.toString(((Date) value).getTime()));
				else if (value instanceof HasDBRepresentation)
				{
					HasDBRepresentation srep = (HasDBRepresentation) value;
					Iterable<String> svalue = srep.toDBRepresentation();
					
					if (svalue.iterator().hasNext())
					{
						String encoding = Utils.toEncoding(svalue);
						map.put(name, encoding);
					}
				}
				else
					assert false;
			}
			catch (IllegalAccessException e)
			{
			}
		}
		return map;
	}
	
	private static Object instantiate(String classname)
	{
		try
		{
			return Class.forName(classname).newInstance();
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	private static <T> T instantiate(Class<T> classobj)
	{
		try
		{
			return classobj.newInstance();
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	public static RootObject deserialise(Map<String, String> map)
	{
		/* Create the object itself. */
		
		String classname = (String) map.get("class");
		Object object = (RootObject) instantiate(classname);
		if (object == null)
			return null;

		Map<String, Field> fieldCache = getFieldCache(object);
		for (Map.Entry<String, Field> entry : fieldCache.entrySet())
		{
			String name = entry.getKey();
			Field field = entry.getValue();
			
			try
			{
				String src = map.get(name);
				Class<?> fieldclass = field.getType();

				if (HasDBRepresentation.class.isAssignableFrom(fieldclass))
				{
					List<String> list;
					
					if (src == null)
						list = Collections.emptyList();
					else
						list = Utils.fromEncoding(src);
					
					Object dest = field.get(object);
					assert(dest != null);
					
					HasDBRepresentation srep = (HasDBRepresentation) dest;
					srep.fromDBRepresentation(list);
				}
				else if (fieldclass == Long.TYPE)
				{
					field.setLong(object, Long.parseLong(src));
				}
				else if (fieldclass == Double.TYPE)
				{
					field.setDouble(object, Double.parseDouble(src));
				}
				else if (fieldclass == String.class)
				{
					field.set(object, src);
				}
				else if (fieldclass == Integer.TYPE)
				{
					field.setInt(object, Integer.parseInt(src));
				}
				else if (fieldclass == Boolean.TYPE)
				{
					field.setBoolean(object, Boolean.parseBoolean(src));
				}
				else if (fieldclass == Date.class)
				{
					long t = Long.parseLong(src);
					field.set(object, new Date(t));
				}
				else
					assert false;
			}
			catch (IllegalAccessException e)
			{
			}
		}
		
		return (RootObject) object;
	}

	private static boolean matchscope(int desired, int got)
	{
		return desired <= got;
	}
	
	public static CObject export(SObject sobject, int scope)
	{
		Class<?> sc = sobject.getClass();
		CClass cclass = sc.getAnnotation(CClass.class);
		Class<?> cc = cclass.name(); 
		CObject cobject = (CObject) instantiate(cc);

		Map<String, Field> sfields = getFieldCache(sobject);
		Map<String, Field> cfields = getFieldCache(cobject);

		try
		{
			for (Map.Entry<String, Field> sentry : sfields.entrySet())
			{
				Field sfield = sentry.getValue();
				Field cfield = cfields.get(sentry.getKey());
				assert(cfield != null);
				
				Property prop = sfield.getAnnotation(Property.class);
				if ((prop != null) && matchscope(scope, prop.scope()))
				{
					Object value = sfield.get(sobject);
					if (value instanceof HasDBRepresentation)
						value = ((HasDBRepresentation) value).getClient();
					
					cfield.set(cobject, value);
				}
			}
			
			return cobject;
		}
		catch (IllegalAccessException e)
		{
			return null;
		}
		catch (IllegalArgumentException e)
		{
			return null;
		}
	}
}
