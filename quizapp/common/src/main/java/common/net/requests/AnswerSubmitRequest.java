package common.net.requests;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Request to send a chosen answer to the server and an opponent in-game.
 * 
 * @author Tim Wiechers
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY)
public class AnswerSubmitRequest implements Serializable {
	/**
	 * Version number for serialization.
	 */
	private static final long serialVersionUID = -6889887916985945155L;
	/**
	 * The answer's index.
	 */
	private int index;
	
	/**
	 * Constructor for JSON deserialization.
	 */
	@Deprecated
	public AnswerSubmitRequest() {
	}

	/**
	 * Creates an instance.
	 * @param index the answer's index
	 */
	public AnswerSubmitRequest(int index) {
		this.index = index;
	}

	// === getters ===
	
	public int getIndex() {
		return index;
	}
}