package server.persistence.constraints;

public class EqualConstraint extends Constraint {
	private final Object obj1;
	private final Object obj2;
	
	public EqualConstraint(Object obj1, Object obj2) {
		this.obj1 = obj1;
		this.obj2 = obj2;
	}
	
	@Override
	public String toString() {
		return String.format(" %s='%s' ", obj1, obj2);
	}
}
