package common.net.responses;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY)
public class OpponentAnswerResponse {
	/**
	 * The answer's index.
	 */
	private int index;
	/**
	 * Constructor for JSON deserialization.
	 */
	@Deprecated
	public OpponentAnswerResponse() {
	}

	/**
	 * Creates an instance.
	 * @param index the answer's index
	 */
	public OpponentAnswerResponse(int index) {
		this.index = index;
	}

	// === getters ===
	
	public int getIndex() {
		return index;
	}
}