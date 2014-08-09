package server.persistence.query;

public class UpdateQueryBuilder extends QueryBuilder {
	private final int id;
	private final String updateString;

	public UpdateQueryBuilder(String table, int id) {
		super(" SET ");
		updateString = "UPDATE " + table;
		this.id = id;
	}

	@Override
	public void appendField(String field) {
		values.append(field + "=");
	}

	@Override
	public String getQuery() {
		values.deleteCharAt(values.length() - 1);
		values.append(" WHERE rowid=" + id);
		return updateString + values.toString();
	}
}