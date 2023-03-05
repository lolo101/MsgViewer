package at.redeye.FrameWork.base.bindtypes;

import java.util.*;

public abstract class DBStrukt {

	private final String strukt_name;
	protected final String title;
	private final Map<String, DBValue> element_by_name = new HashMap<>();
	private final Collection<DBStrukt> sub_strukts = new ArrayList<>();
	protected Integer version;
	private final Map<String, List<Integer>> elements_with_version = new HashMap<>();

	public DBStrukt(String name) {
		this(name, "");
	}

	public DBStrukt(String name, String title) {
		this.strukt_name = name;
		this.title = title;
	}

	public final void add(DBValue value) {
		add(value, 1);
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


	public String getName() {
		return strukt_name;
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

	private ArrayList<String> getAllNames(String prefix) {
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

	public String getTitle() {
		return title;
	}

	private DBValue getValueByName(String key) {
		return element_by_name.get(key.toLowerCase());
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getVersion() {
		return version == null ? 1 : version;
	}
}
