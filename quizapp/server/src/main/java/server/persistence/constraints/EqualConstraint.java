package server.persistence.constraints;

public class EqualConstraint extends OperatorConstraint {

	public EqualConstraint(Object ... objects) {
		super(objects);
	}
	
	@Override
	protected String getOperator() {
		return "=";
	}
}