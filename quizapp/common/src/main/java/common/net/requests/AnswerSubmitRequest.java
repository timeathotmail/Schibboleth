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
	
	private int matchId;
	
	/**
	 * The answer's index.
	 */
	private int index;
	
	private boolean inTime;
	
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
	public AnswerSubmitRequest(int matchId, int index, boolean inTime) {
		this.matchId = matchId;
		this.index = index;
		this.inTime = inTime;
	}

	// === getters ===
	
	public int getMatchId() {
		return matchId;
	}
	
	public int getIndex() {
		return index;
	}
	
	public boolean answeredInTime() {
		return inTime;
	}
}