package server.persistence.constraints;

/**
 * Constraint used to group results.
 * 
 * @author Tim Wiechers
 */
public class GroupByConstraint extends Constraint {
	/**
	 * The fields to group by.
	 */
	private final String[] fields;

	/**
	 * Creates an instance.
	 * 
	 * @param fields
	 *            the fields to group by
	 */
	public GroupByConstraint(String... fields) {
		this.fields = fields;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.persistence.constraints.Constraint#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(" GROUP BY ");

		for (String field : fields) {
			sb.append(field);
			sb.append(",");
		}

		return sb.substring(0, sb.length() - 1) + " ";
	}
}
