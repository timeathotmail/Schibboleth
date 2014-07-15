package server.persistence.query;

public abstract class QueryBuilder {
	protected boolean hasValues;
	protected final StringBuilder values;

	protected QueryBuilder(String values) {
		this.values = new StringBuilder(values);
	}

	public void appendValue(Object value) {
		hasValues = true;
		
		if (value.getClass().equals(String.class)) {
			values.append(String.format("'%s'", value));
		} else {
			values.append(value);
		}
		
		values.append(",");
	}
	
	public boolean hasValues() {
		return hasValues;
	}

	public abstract void appendField(String field);

	public abstract String getQuery();
}
