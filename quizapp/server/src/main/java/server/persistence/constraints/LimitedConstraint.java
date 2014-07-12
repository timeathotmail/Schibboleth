package server.persistence.constraints;

public class LimitedConstraint extends Constraint {
	private final int limit;
	private final int offset;

	public LimitedConstraint(int limit, int offset) {
		this.limit = limit;
		this.offset = offset;
	}

	@Override
	public String toString() {
		return String.format(" LIMIT %d OFFSET %d ", limit, offset);
	}
}
