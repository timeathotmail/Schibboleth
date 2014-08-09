package server.persistence.constraints;

/**
 * Base class for query constraints.
 * 
 * @author Tim Wiechers
 */
public abstract class Constraint {
	public abstract String toString();

	/**
	 * Creates a String from multiple constraints.
	 * 
	 * @param constraints
	 *            the constraints to create a String for
	 * @return the constraint String
	 */
	public static <T extends Constraint> String toString(T... constraints) {
		if (constraints.length == 0)
			return "";

		StringBuilder sb = new StringBuilder();

		for (T constraint : constraints) {
			sb.append(constraint);
		}

		return sb.toString();
	}
}
