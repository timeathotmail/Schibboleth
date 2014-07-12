package server.persistence.constraints;

public class GroupByConstraint extends Constraint {
	private final String groupByField;

	public GroupByConstraint(String groupByField) {
		this.groupByField = groupByField;
	}

	@Override
	public String toString() {
		return String.format(" GROUP BY %s", groupByField);
	}
}
