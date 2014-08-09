package server.persistence.constraints;

/**
 * Base class for constraints using operators.
 * 
 * @author Tim Wiechers
 */
public abstract class OperatorConstraint extends Constraint {
	/**
	 * Fields.
	 */
	private final Object[] fields;
	/**
	 * Values.
	 */
	private final Object[] values;

	/**
	 * Creates an instance.
	 * 
	 * @param objects
	 *            list of field-value pairs
	 */
	protected OperatorConstraint(Object... objects) {
		fields = new Object[objects.length / 2];
		values = new Object[objects.length / 2];

		if (objects.length % 2 != 0) {
			throw new IllegalArgumentException(
					"'objects' has to be a collection of field-value pairs!");
		}

		for (int i = 0, j = 0; j < objects.length / 2; i += 2, j++) {
			fields[j] = objects[i];
			values[j] = objects[i + 1];
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.persistence.constraints.Constraint#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < fields.length; i++) {
			sb.append(String.format("%s%s'%s' AND ", fields[i], getOperator(),
					values[i]));
		}

		return sb.substring(0, sb.length() - 5);
	}

	abstract protected String getOperator();
}
