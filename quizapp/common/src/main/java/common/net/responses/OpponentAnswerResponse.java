package common.net.responses;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Response to notify a client about the opponent's answer.
 * 
 * @author Tim Wiechers
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY)
public class OpponentAnswerResponse implements Serializable {
	/**
	 *  Version number for serialization.
	 */
	private static final long serialVersionUID = -827424907619406814L;
	
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
	public OpponentAnswerResponse() {
	}

	/**
	 * Creates an instance.
	 * @param index the answer's index
	 */
	public OpponentAnswerResponse(int matchId, int index, boolean inTime) {
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