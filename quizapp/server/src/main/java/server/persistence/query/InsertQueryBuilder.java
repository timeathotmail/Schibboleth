package server.persistence.query;

/**
 * QueryBuilder to ease creating insert queries.
 * 
 * @author Tim Wiechers
 */
public class InsertQueryBuilder extends QueryBuilder {
	/**
	 * StringBuilder for the fields.
	 */
	private final StringBuilder fields;

	/**
	 * Creates an instance.
	 * 
	 * @param table
	 *            the table for the query
	 */
	public InsertQueryBuilder(String table) {
		super(" VALUES (", table);
		fields = new StringBuilder(String.format("INSERT INTO %s (", table));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.persistence.query.QueryBuilder#appendField(java.lang.String)
	 */
	@Override
	public void appendField(String field) {
		fields.append(field + ",");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.persistence.query.QueryBuilder#getQuery()
	 */
	@Override
	public String getQuery() {
		fields.deleteCharAt(fields.length() - 1);
		fields.append(")");
		values.deleteCharAt(values.length() - 1);
		values.append(")");
		return fields.toString() + values.toString();
	}
}