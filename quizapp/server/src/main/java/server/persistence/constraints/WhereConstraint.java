package server.persistence.constraints;

/**
 * Constraint used to group OperatorConstraints.
 * 
 * @author Tim Wiechers
 */
public class WhereConstraint extends Constraint {
	/**
	 * List of constraints.
	 */
	private final OperatorConstraint[] constraints;

	/**
	 * Creates an instance.
	 * 
	 * @param constraints
	 *            list of constraints
	 */
	public WhereConstraint(OperatorConstraint... constraints) {
		this.constraints = constraints;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.persistence.constraints.Constraint#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(" WHERE ");

		for (OperatorConstraint c : constraints) {
			sb.append(c);
		}

		sb.append(" ");
		return sb.toString();
	}
}