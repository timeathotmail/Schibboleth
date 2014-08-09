package server.persistence.constraints;

/**
 * Constraint used to assert that a value is less or equal to another.
 * 
 * @author Tim Wiechers
 */
public class LessEqualConstraint extends OperatorConstraint {
	/**
	 * Creates an instance.
	 * 
	 * @param objects
	 *            list of field-value pairs
	 */
	public LessEqualConstraint(Object... objects) {
		super(objects);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.persistence.constraints.OperatorConstraint#getOperator()
	 */
	@Override
	protected String getOperator() {
		return "<=";
	}
}