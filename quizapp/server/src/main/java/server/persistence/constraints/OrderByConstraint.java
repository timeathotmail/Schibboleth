package server.persistence.constraints;

/**
 * Constraint used to order results.
 * 
 * @author Tim Wiechers
 */
public class OrderByConstraint extends Constraint {
	/**
	 * Sorting orders.
	 */
	public enum Order {
		ASC, DESC
	}
	
	/**
	 * The fields to sort by.
	 */
	private final String[] fields;
	/**
	 * The order to sort by.
	 */
	private final Order order;

	/**
	 * Creates an instance.
	 * @param order the order to sort by
	 * @param fields the fields to sort by
	 */
	public OrderByConstraint(Order order, String ... fields) {
		this.fields = fields;
		this.order = order;
	}

	/* (non-Javadoc)
	 * @see server.persistence.constraints.Constraint#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(" ORDER BY ");
		
		for(String field : fields) {
			sb.append(field);
			sb.append(",");
		}
		
		sb.deleteCharAt(sb.length()-1);
		sb.append(" ");
		sb.append(order);
		return sb.toString();
	}
}
