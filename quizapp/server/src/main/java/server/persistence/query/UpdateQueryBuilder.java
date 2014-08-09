package server.persistence.query;

/**
 * QueryBuilder to ease creating update queries.
 * 
 * @author Tim Wiechers
 */
public class UpdateQueryBuilder extends QueryBuilder {
	/**
	 * Id of the row to be updated.
	 */
	private final int rowid;

	/**
	 * Creates an instance.
	 * 
	 * @param table
	 *            the table for the query
	 * @param rowid
	 */
	public UpdateQueryBuilder(String table, int rowid) {
		super(" SET ", table);
		this.rowid = rowid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.persistence.query.QueryBuilder#appendField(java.lang.String)
	 */
	@Override
	public void appendField(String field) {
		values.append(field + "=");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.persistence.query.QueryBuilder#getQuery()
	 */
	@Override
	public String getQuery() {
		values.deleteCharAt(values.length() - 1);
		values.append(" WHERE rowid=" + rowid);
		return "UPDATE " + table + values.toString();
	}
}