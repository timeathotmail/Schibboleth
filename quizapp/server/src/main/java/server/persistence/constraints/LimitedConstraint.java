package server.persistence.constraints;

/**
 * Constraint used to limit and offset the result.
 * 
 * @author Tim Wiechers
 */
public class LimitedConstraint extends Constraint {
	/**
	 * The number of maximum results.
	 */
	private final int limit;
	/**
	 * The number of results to be skipped.
	 */
	private final int offset;

	/**
	 * Creates an instance.
	 * 
	 * @param limit
	 *            the number of maximum results
	 * @param offset
	 *            the number of results to be skipped
	 */
	public LimitedConstraint(int limit, int offset) {
		this.limit = limit;
		this.offset = offset;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.persistence.constraints.Constraint#toString()
	 */
	@Override
	public String toString() {
		return String.format(" LIMIT %d OFFSET %d ", limit, offset);
	}
}
