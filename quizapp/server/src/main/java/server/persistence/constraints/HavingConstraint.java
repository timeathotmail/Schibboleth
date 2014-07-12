package server.persistence.constraints;

public class HavingConstraint extends Constraint {
	private final OperatorConstraint[] constraints;
	
	
	public HavingConstraint(OperatorConstraint ... constraints) {
		this.constraints = constraints;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(" HAVING ");
		
		for(OperatorConstraint c : constraints) {
			sb.append(c);
		}
		
		sb.append(" ");
		return sb.toString();
	}
}