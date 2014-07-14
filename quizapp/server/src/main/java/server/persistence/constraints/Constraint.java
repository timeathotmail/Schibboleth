package server.persistence.constraints;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Constraint {
	public abstract String toString();

	public enum AppendAt {
		FRONT, BACK
	}

	public static Constraint[] append(Constraint[] arr, Constraint add,
			AppendAt at) {
		ArrayList<Constraint> temp = new ArrayList<Constraint>(
				Arrays.asList(arr));

		if (at == AppendAt.FRONT) {
			temp.add(0, add);
		} else {
			temp.add(add);
		}

		return temp.toArray(new Constraint[arr.length + 1]);
	}
	
	public static <T extends Constraint> String toString(
			T... constraints) {
		if (constraints.length == 0)
			return "";

		StringBuilder sb = new StringBuilder();

		for (T constraint : constraints) {
			sb.append(constraint);
		}

		return sb.toString();
	}
}
