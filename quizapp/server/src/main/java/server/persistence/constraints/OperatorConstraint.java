package server.persistence.constraints;

public abstract class OperatorConstraint {
	private final Object[] fields;
	private final Object[] values;
	
	protected OperatorConstraint(Object ... objects) {
		fields = new Object[objects.length/2];
		values = new Object[objects.length/2];
		
		if(objects.length % 2 != 0) {
			return;
		}
		
		for(int i = 0, j = 0; j < objects.length / 2; i += 2, j++) {
			fields[j] = objects[i];
			values[j] = objects[i+1];
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < fields.length; i++) {
			sb.append(String.format("%s%s'%s' AND ", fields[i], getOperator(), values[i]));
		}
		
		return sb.substring(0, sb.length()-5);
	}
	
	abstract protected String getOperator();
}
