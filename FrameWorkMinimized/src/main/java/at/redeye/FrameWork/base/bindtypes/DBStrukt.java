/*

 * To change this template, choose Tools | Templates

 * and open the template in the editor.

 */

package at.redeye.FrameWork.base.bindtypes;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import at.redeye.SqlDBInterface.SqlDBIO.impl.ColumnAttribute;
import java.util.ArrayList;

/**
 * @author martin
 */

public abstract class DBStrukt {

	protected String strukt_name;
        protected String strukt_name_lower_case;
	protected String title;
	protected HashMap<String, DBValue> element_by_name = new HashMap<String, DBValue>();
	protected ArrayList<DBStrukt> sub_strukts = new ArrayList<DBStrukt>();
	protected Integer version = null;
	protected ArrayList<Entry<Integer, DBValue>> elements_with_version = new ArrayList<Entry<Integer, DBValue>>();

	public DBStrukt(String name) {
		this.strukt_name = name;                
		title = new String();
	}

	public DBStrukt(String name, String title) {
		this.strukt_name = name;                
		this.title = title;
	}

	public void add(DBValue value) {
		add(value,1);		
	}

        /**
         * removes an element. The DBValue has to be from the same instance
         * as this object
         * @param value 
         */
	public void remove(DBValue value) {		
		element_by_name.remove(value.getName());
                
                for( Entry<Integer, DBValue> entry : elements_with_version ) 
                {
                    if( entry.getValue().getName().equals(value.getName()) )
                    {
                        elements_with_version.remove(entry);
                        break;
                    }
                }
	}

	public void add(DBValue value, Integer version) {		
		element_by_name.put(value.getName(), value);
		elements_with_version.add(new SimpleEntry<Integer, DBValue>(version,
				value));

		if (this.version == null)
			this.version = version;
		else if (version > this.version)
			this.version = version;
	}

	public void add(DBStrukt substrukt) {
		sub_strukts.add(substrukt);
	}

	public void consume(HashMap<String, Object> map) {
		consume(map, null);
	}

	public void consume(HashMap<String, Object> map, String prefix) {
		// Set<String> keys = map.keySet();
        
        Set<Entry<String, Object>> entries = map.entrySet();
        String k;
        
		for (Entry<String, Object> entry : entries) {
			if (prefix != null && entry.getKey().length() <= prefix.length())
				continue;			

			if (prefix != null)
				k = entry.getKey().substring(prefix.length());
			else
				k = entry.getKey();

			DBValue val = getValueByName(k);

			if (val != null) {
				val.loadFromDB(entry.getValue());
				continue;
			}

			for (int i = 0; i < sub_strukts.size(); i++) {
				DBStrukt strukt = sub_strukts.get(i);

				if (k.startsWith(strukt.getName()) && (k.charAt(strukt.getName().length()) == '_')) {                    
					if (prefix != null)
						strukt.consume(map, prefix + strukt.getName() + "_");
					else
						strukt.consume(map, strukt.getName() + "_");

					break;
				}
			}
		}
	}
        
        /**
         * Same as consume(), but all column names has to be lower case 
         * @param map
         */
	public void consumeFast(HashMap<String, Object> map) {
		consumeFast(map, null);
	}        

        /**
         * Same as consume(), but all column names and the prefix has to be lower case 
         * @param map
         * @param prefix 
         */
	public void consumeFast(HashMap<String, Object> map, String prefix) {

            FastArray consumed = null;
            
             if( !sub_strukts.isEmpty() )
                consumed = new FastArray(map.size());
            
            consumeFast(map, prefix, consumed);
        }        
        
    /**
     * Simple array wrapper of an initial fixed size
     * This is much more simplier than ArrayList but it's
     * compare method is faster, because does not call the euqal method
     */
    private static class FastArray
    {
        Object[] data;
        int idx;
        
        FastArray( int capacity )
        {
            data = new Object[capacity];
            idx = 0;
        }
        
        void add( Object o )
        {
            data[idx] = o;
            idx++;
        }
        
        /**         
         * @param o
         * @return true if the same object was found in the array
         */
        boolean contains( Object o )
        {
            for( int i = 0; i < idx; i++ )
            {
                if( data[i] == o )
                    return true;
            }
            
            return false;
        }
    }
    

	private void consumeFast(HashMap<String, Object> map, String prefix, FastArray consumed) {        

        Set<Entry<String, Object>> entries = map.entrySet();
        String k;

        for (Entry<String, Object> entry : entries) {
            
            // bereits geladene Einträge überspringen
            if( consumed != null && consumed.contains(entry.getKey()) ) {
                    continue;
             }
            
            if (prefix != null && entry.getKey().length() <= prefix.length()) {
                continue;
            }

            if (prefix != null) {
                k = entry.getKey().substring(prefix.length());
            } else {
                k = entry.getKey();
            }

            DBValue val = getValueByNameLowerCase(k);

            if (val != null) {
                val.loadFromDB(entry.getValue()); 
                
                if( consumed != null )
                    consumed.add(entry.getKey());
                continue;
            }

            for (int i = 0; i < sub_strukts.size(); i++) {
                DBStrukt strukt = sub_strukts.get(i);

                if (k.startsWith(strukt.getName()) && (k.charAt(strukt.getName().length()) == '_')) {
                    if (prefix != null) {
                        strukt.consumeFast(map, prefix + strukt.getNameLowerCase() + "_", consumed);
                    } else {
                        strukt.consumeFast(map, strukt.getNameLowerCase() + "_", consumed);
                    }

                    break;
                }
            }
        }
    }
    
	public String getName() {
		return strukt_name;
	}
        
	public String getNameLowerCase() {
            if( strukt_name_lower_case == null )
                strukt_name_lower_case = strukt_name.toLowerCase();
            
            return strukt_name_lower_case;
	}        

        /**
         * Get DBValue by its index. Each member that is added to DBStrukt by using the add()
         * method is stored in a vector. So the elements can also be accessed by the idx of this
         * vector.
         * @param idx
         * @return DBValue 
         */
	public DBValue getValue(int idx) {
		return elements_with_version.get(idx).getValue();
	}

        /**
         * retuns the DBValue by searching the element by its name by using DBValue.getName() function
         * @param val
         * @return
         */
	public DBValue getValue(DBValue val) {
		return getValue(val.getName());
	}

	public DBValue getValue(String name) {
		return getValueByName(name);       
	}

	public int countValues() {
		return elements_with_version.size();
	}

	public int countSubStrukts() {
		return sub_strukts.size();
	}

        public ArrayList<DBStrukt> getSubStrukts() {
		return sub_strukts;
	}
        
	public DBStrukt getSubStrukt(int idx) {
		return sub_strukts.get(idx);
	}

	public HashMap<String, ColumnAttribute> getHashMap() {
		return getHashMap("");
	}

	protected HashMap<String, ColumnAttribute> getHashMap(String prefix) {
		return getHashMap(prefix, null);
	}

	protected boolean VersionExists(DBValue val, Integer Version) {
		for (Entry<Integer, DBValue> pair : elements_with_version) {
			if ((int) pair.getKey() == (int) Version) {
				if (pair.getValue() == val)
					return true;
			}
		}

		return false;
	}

	public HashMap<String, ColumnAttribute> getHashMapForVersion(Integer Version) {
		return getHashMap("", Version);
	}

	protected HashMap<String, ColumnAttribute> getHashMap(String prefix,
			Integer Version) {
		HashMap<String, ColumnAttribute> colls = new HashMap<String, ColumnAttribute>();

		for (int i = 0; i < elements_with_version.size(); i++) {
			DBValue val = elements_with_version.get(i).getValue();

			if (Version != null) {
				if (!VersionExists(val, Version))
					continue;
			}

			ColumnAttribute attr = new ColumnAttribute(val.getDBType());

			attr.setPrimaryKey(val.isPrimaryKey());
			attr.setHasIndex(val.shouldHaveIndex());

			if (DBString.class.isInstance(val)) {
				attr.setWidth(((DBString) val).getMaxLen());
			} else if (val instanceof DBEnum) {
				attr.setWidth(((DBEnum) val).getMaxLen());
			}

			colls.put(prefix + val.getName(), attr);
		}

		for (int i = 0; i < sub_strukts.size(); i++) {
			DBStrukt strukt = sub_strukts.get(i);

			HashMap<String, ColumnAttribute> sub_colls = strukt.getHashMap(
					prefix + strukt.getName() + "_", Version);

			Set<String> keys = sub_colls.keySet();

			for (String s : keys) {
				colls.put(s, sub_colls.get(s));
			}
		}

		return colls;
	}

	public HashMap<String, Object> getHashMapAndData() {
		return getHashMapAndData("");
	}

	protected HashMap<String, Object> getHashMapAndData(String prefix) {
		HashMap<String, Object> colls = new HashMap<String, Object>();

		for (int i = 0; i < elements_with_version.size(); i++) {
			DBValue val = elements_with_version.get(i).getValue();
			colls.put(prefix + val.getName(), val.getValue());
		}

		for (int i = 0; i < sub_strukts.size(); i++) {
			DBStrukt strukt = sub_strukts.get(i);

			HashMap<String, Object> sub_colls = strukt.getHashMapAndData(prefix
					+ strukt.getName() + "_");

			Set<String> keys = sub_colls.keySet();

			for (String s : keys) {
				colls.put(s, sub_colls.get(s));
			}
		}

		return colls;
	}

	public ArrayList<DBValue> getAllValues() {
		ArrayList<DBValue> values = new ArrayList<DBValue>();

		for (int i = 0; i < elements_with_version.size(); i++) {
			DBValue val = elements_with_version.get(i).getValue();

			values.add(val);
		}

		for (int i = 0; i < sub_strukts.size(); i++) {
			DBStrukt strukt = sub_strukts.get(i);

			values.addAll(strukt.getAllValues());
		}

		return values;
	}

	public ArrayList<String> getAllNames() {
		return getAllNames("");
	}

	protected ArrayList<String> getAllNames(String prefix) {
		ArrayList<String> values = new ArrayList<String>();

		for (int i = 0; i < elements_with_version.size(); i++) {
			DBValue val = elements_with_version.get(i).getValue();

			if (val.getTitle().isEmpty())
				values.add(prefix + val.getName());
			else
				values.add(prefix + val.getTitle());
		}

		for (int i = 0; i < sub_strukts.size(); i++) {
			DBStrukt strukt = sub_strukts.get(i);

			if (strukt.getTitle().isEmpty())
				values.addAll(strukt.getAllNames(strukt.getName() + " "));
			else
				values.addAll(strukt.getAllNames(strukt.getTitle() + " "));
		}

		return values;
	}

	public abstract DBStrukt getNewOne();

	public String getTitle() {
		return title;
	}

	private DBValue getValueByName(String key) {
		return element_by_name.get(key.toLowerCase());

		/*
		 * 
		 * for( int i = 0; i < elements.size(); i++ ) { if(
		 * key.equalsIgnoreCase(elements.get(i).getName()) ) return
		 * elements.get(i); }
		 * 
		 * return null;
		 */
	}
        
        /**
         * find Value by name without converting the name to lower case
         * @param key has to be lower case 
         * @return 
         */
        private DBValue getValueByNameLowerCase(String key) {
		return element_by_name.get(key);

		/*
		 * 
		 * for( int i = 0; i < elements.size(); i++ ) { if(
		 * key.equalsIgnoreCase(elements.get(i).getName()) ) return
		 * elements.get(i); }
		 * 
		 * return null;
		 */
	}

	public void loadFromCopy(DBStrukt s) {
		for (int i = 0; i < s.elements_with_version.size(); i++) {
			DBValue val = s.elements_with_version.get(i).getValue();

			elements_with_version.get(i).getValue().loadFromCopy(val.getValue());
		}

		for (int i = 0; i < s.sub_strukts.size(); i++) {
			sub_strukts.get(i).loadFromCopy(s.sub_strukts.get(i));
		}
	}

	public DBStrukt getCopy() {
		DBStrukt s = getNewOne();

		s.loadFromCopy(this);

		return s;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getVersion() {
		if (version == null)
			return 1;

		return version;
	}
        
        public void setTitle( String title )
        {
            this.title = title;
        }

}
