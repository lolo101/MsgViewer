package at.redeye.FrameWork.base.bindtypes;

import at.redeye.SqlDBInterface.SqlDBIO.impl.ColumnAttribute;

import java.util.*;
import java.util.Map.Entry;

public abstract class DBStrukt {

	protected final String strukt_name;
	protected final String strukt_name_lower_case;
	protected String title;
	protected final Map<String, DBValue> element_by_name = new HashMap<>();
	protected final ArrayList<DBStrukt> sub_strukts = new ArrayList<>();
	protected Integer version = null;
	protected final Map<String, List<Integer>> elements_with_version = new HashMap<>();

	public DBStrukt(String name) {
		this(name, "");
	}

	public DBStrukt(String name, String title) {
		this.strukt_name = name;
		this.strukt_name_lower_case = strukt_name.toLowerCase();
		this.title = title;
	}

	public void add(DBValue value) {
		add(value, 1);
	}

	/**
	 * removes an element. The DBValue has to be from the same instance
	 * as this object
	 */
	public void remove(DBValue value) {
		element_by_name.remove(value.getName());
		elements_with_version.remove(value.getName());
	}

	public void add(DBValue value, Integer version) {
		element_by_name.put(value.getName(), value);
		elements_with_version.computeIfAbsent(value.getName(), name -> new ArrayList<>()).add(version);

		if (this.version == null || version > this.version)
			this.version = version;
	}

	public void add(DBStrukt substrukt) {
		sub_strukts.add(substrukt);
	}

	public void consume(HashMap<String, Object> map) {
		consume(map, null);
	}

	public void consume(HashMap<String, Object> map, String prefix) {
        Set<Entry<String, Object>> entries = map.entrySet();

		for (Entry<String, Object> entry : entries) {
			if (prefix != null && entry.getKey().length() <= prefix.length())
				continue;

			String k = removePrefix(prefix, entry.getKey());

			DBValue val = getValueByName(k);

			if (val != null) {
				val.loadFromDB(entry.getValue());
				continue;
			}

			for (DBStrukt strukt : sub_strukts) {
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
	 */
	public void consumeFast(HashMap<String, Object> map) {
		consumeFast(map, null);
	}

	private static String removePrefix(String prefix, String key) {
		return prefix == null ? key : key.substring(prefix.length());
	}

	/**
	 * Same as consume(), but all column names and the prefix has to be lower case
	 */
	public void consumeFast(HashMap<String, Object> map, String prefix) {

		FastArray consumed = null;

		if (!sub_strukts.isEmpty())
			consumed = new FastArray(map.size());

		consumeFast(map, prefix, consumed);
	}

	/**
	 * Simple array wrapper of an initial fixed size
	 * This is much more simplier than ArrayList but its
	 * compare method is faster, because does not call the euqal method
	 */
	private static class FastArray {
		Object[] data;
		int idx;

		FastArray(int capacity) {
			data = new Object[capacity];
			idx = 0;
		}

		void add(Object o) {
			data[idx] = o;
			idx++;
		}

		/**
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


	private void consumeFast(Map<String, Object> map, String prefix, FastArray consumed) {

		for (Entry<String, Object> entry : map.entrySet()) {

			// bereits geladene Einträge überspringen
			if (consumed != null && consumed.contains(entry.getKey())) {
				continue;
			}

			if (prefix != null && entry.getKey().length() <= prefix.length()) {
				continue;
			}

			String k = removePrefix(prefix, entry.getKey());

			DBValue val = getValueByName(k);

			if (val != null) {
				val.loadFromDB(entry.getValue());

				if (consumed != null)
					consumed.add(entry.getKey());
				continue;
			}

			for (DBStrukt strukt : sub_strukts) {
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
		return strukt_name_lower_case;
	}

	/**
	 * retuns the DBValue by searching the element by its name by using DBValue.getName() function
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

	public DBStrukt getSubStrukt(int idx) {
		return sub_strukts.get(idx);
	}

	public Map<String, ColumnAttribute> getColumns() {
		return getColumns("");
	}

	protected Map<String, ColumnAttribute> getColumns(String prefix) {
		return getColumns(prefix, null);
	}

	protected boolean VersionExists(DBValue val, Integer Version) {
		return elements_with_version.getOrDefault(val.getName(), List.of())
				.contains(Version);
	}

	protected Map<String, ColumnAttribute> getColumns(String prefix,
													  Integer Version) {
		Map<String, ColumnAttribute> colls = new HashMap<>();

		for (DBValue val : element_by_name.values()) {

			if (Version == null || VersionExists(val, Version)) {
				ColumnAttribute attr = new ColumnAttribute(val.getDBType());

				attr.setPrimaryKey(val.isPrimaryKey());
				attr.setHasIndex(val.shouldHaveIndex());

				if (val instanceof DBString) {
					attr.setWidth(((DBString) val).getMaxLen());
				} else if (val instanceof DBEnum) {
					attr.setWidth(((DBEnum<?>) val).getMaxLen());
				}

				colls.put(prefix + val.getName(), attr);
			}
		}

		for (DBStrukt strukt : sub_strukts) {
			Map<String, ColumnAttribute> sub_colls = strukt.getColumns(
					prefix + strukt.getName() + "_", Version);

			Set<String> keys = sub_colls.keySet();

			for (String s : keys) {
				colls.put(s, sub_colls.get(s));
			}
		}

		return colls;
	}

	public Map<String, Object> getValueByNames() {
		return getValueByNames("");
	}

	protected Map<String, Object> getValueByNames(String prefix) {
		HashMap<String, Object> colls = new HashMap<>();

		for (DBValue val : element_by_name.values()) {
			colls.put(prefix + val.getName(), val.getValue());
		}

		for (DBStrukt strukt : sub_strukts) {
			Map<String, Object> sub_colls = strukt.getValueByNames(prefix
					+ strukt.getName() + "_");

			Set<String> keys = sub_colls.keySet();

			for (String s : keys) {
				colls.put(s, sub_colls.get(s));
			}
		}

		return colls;
	}

	public List<DBValue> getAllValues() {
		List<DBValue> values = new ArrayList<>(element_by_name.values());

		for (DBStrukt strukt : sub_strukts) {
			values.addAll(strukt.getAllValues());
		}

		return values;
	}

	public ArrayList<String> getAllNames() {
		return getAllNames("");
	}

	protected ArrayList<String> getAllNames(String prefix) {
		ArrayList<String> values = new ArrayList<>();

		for (DBValue val : element_by_name.values()) {

			if (val.getTitle().isEmpty())
				values.add(prefix + val.getName());
			else
				values.add(prefix + val.getTitle());
		}

		for (DBStrukt strukt : sub_strukts) {
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
	}

	public void loadFromCopy(DBStrukt s) {
		for (DBValue val : element_by_name.values()) {
			val.loadFromCopy(val.getValue());
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
		return version == null ? 1 : version;
	}

	public void setTitle(String title) {
		this.title = title;
        }

}
