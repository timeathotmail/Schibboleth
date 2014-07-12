package server.persistence.constraints;


public class OrderByConstraint extends Constraint {
	public enum Order {
		ASC, DESC
	}
	
	private final String[] fields;
	private final Order order;

	public OrderByConstraint(Order order, String ... fields) {
		this.fields = fields;
		this.order = order;
	}

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
