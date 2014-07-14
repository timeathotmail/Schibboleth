package server.persistence.constraints;

public class GroupByConstraint extends Constraint {
	private final String[] groupByFields;

	public GroupByConstraint(String ... groupByFields) {
		this.groupByFields = groupByFields;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(" GROUP BY ");
		
		for(String field : groupByFields) {
			sb.append(field);
			sb.append(",");
		}
		
		return sb.substring(0, sb.length()-1) + " ";
	}
}
