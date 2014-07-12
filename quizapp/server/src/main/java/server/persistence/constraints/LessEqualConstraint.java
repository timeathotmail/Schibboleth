package server.persistence.constraints;

public class LessEqualConstraint extends OperatorConstraint {

	public LessEqualConstraint(Object ... objects) {
		super(objects);
	}
	
	@Override
	protected String getOperator() {
		return "<=";
	}
}