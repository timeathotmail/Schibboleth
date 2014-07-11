package server.persistence.utils;

public abstract class QueryBuilder {
	protected boolean hasValues;
	protected final StringBuilder values;

	protected QueryBuilder(String values) {
		this.values = new StringBuilder(values);
	}

	public void appendValue(String value) {
		hasValues = true;
		values.append(value + ",");
	}

	public boolean hasValues() {
		return hasValues;
	}

	public abstract void appendField(String field);

	public abstract String getQuery();
}
