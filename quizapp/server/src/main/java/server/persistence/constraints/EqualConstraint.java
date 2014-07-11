package server.persistence.constraints;

public class EqualConstraint extends Constraint {
	private final String field;
	private final Object obj;
	
	public EqualConstraint(String field, Object obj) {
		this.field = field;
		this.obj = obj;
	}
	
	@Override
	public String toString() {
		return String.format(" %s='%s' ", field, obj);
	}
}
