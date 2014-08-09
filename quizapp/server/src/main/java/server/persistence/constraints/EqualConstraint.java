package server.persistence.constraints;

/**
 * Constraint used to assert equal values.
 * 
 * @author Tim Wiechers
 */
public class EqualConstraint extends OperatorConstraint {

	/**
	 * Creates an instance.
	 * 
	 * @param objects
	 *            list of field-value pairs
	 */
	public EqualConstraint(Object... objects) {
		super(objects);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.persistence.constraints.OperatorConstraint#getOperator()
	 */
	@Override
	protected String getOperator() {
		return "=";
	}
}