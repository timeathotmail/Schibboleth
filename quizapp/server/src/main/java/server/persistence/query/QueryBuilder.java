package server.persistence.query;

/**
 * Base for query builders to ease query construction.
 * 
 * @author Tim Wiechers
 */
public abstract class QueryBuilder {
	/**
	 * Indicates whether values have been added.
	 */
	protected boolean hasValues;
	/**
	 * StringBuilder for the values.
	 */
	protected final StringBuilder values;
	/**
	 * The table for the query.
	 */
	protected final String table;

	/**
	 * Creates a new instance.
	 * 
	 * @param valuesPrefix
	 *            prefix for {@link QueryBuilder#values}
	 * @param table
	 *            the table for the query
	 */
	protected QueryBuilder(String valuesPrefix, String table) {
		this.values = new StringBuilder(valuesPrefix);
		this.table = table;
	}

	/**
	 * Adds a value of a field.
	 * 
	 * @param value
	 *            the value to add
	 */
	public void appendValue(Object value) {
		hasValues = true;

		// formatting
		if (value.getClass().equals(String.class)) {
			values.append(String.format("'%s'", value));
		} else if (value.getClass().equals(Boolean.class)) {
			values.append(((Boolean) value).booleanValue() ? 1 : 0);
		} else {
			values.append(value);
		}

		values.append(",");
	}

	/**
	 * @return returns true if values have been added
	 */
	public boolean hasValues() {
		return hasValues;
	}

	/**
	 * Adds a field.
	 * 
	 * @param field
	 *            the field to add
	 */
	public abstract void appendField(String field);

	/**
	 * @return the resulting query
	 */
	public abstract String getQuery();
}
