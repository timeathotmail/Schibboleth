package server.persistence.query;

public class InsertQueryBuilder extends QueryBuilder {
	private final StringBuilder fields;

	public InsertQueryBuilder(String table) {
		super(" VALUES (");
		fields = new StringBuilder(String.format("INSERT INTO %s (", table));
	}

	@Override
	public void appendField(String field) {
		fields.append(field + ",");
	}

	@Override
	public String getQuery() {
		fields.deleteCharAt(fields.length() - 1);
		fields.append(")");
		values.deleteCharAt(values.length() - 1);
		values.append(")");
		return fields.toString() + values.toString();
	}
}