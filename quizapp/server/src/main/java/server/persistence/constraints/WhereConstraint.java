package server.persistence.constraints;

public class WhereConstraint extends Constraint {
	private final OperatorConstraint[] constraints;
	
	
	public WhereConstraint(OperatorConstraint ... constraints) {
		this.constraints = constraints;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(" WHERE ");
		
		for(OperatorConstraint c : constraints) {
			sb.append(c);
		}
		
		sb.append(" ");
		return sb.toString();
	}
}