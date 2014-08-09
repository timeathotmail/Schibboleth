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
	 * Version number for serialization.
	 */
	private static final long serialVersionUID = -827424907619406814L;
	/**
	 * The rowId of the match.
	 */
	private int matchRowId;
	/**
	 * The answer's index.
	 */
	private int index;
	/**
	 * Indicates whether the opponent answered in the time limit.
	 */
	private boolean inTime;

	/**
	 * Constructor for JSON deserialization.
	 */
	@Deprecated
	public OpponentAnswerResponse() {
	}

	/**
	 * Creates an instance.
	 * 
	 * @param matchRowId
	 *            the rowId of the match
	 * @param index
	 *            the answer's index
	 * @param inTime
	 *            true if the opponent answered in the time limit
	 */
	public OpponentAnswerResponse(int matchRowId, int index, boolean inTime) {
		this.matchRowId = matchRowId;
		this.index = index;
		this.inTime = inTime;
	}

	// === getters ===

	public int getMatchId() {
		return matchRowId;
	}

	public int getIndex() {
		return index;
	}

	public boolean answeredInTime() {
		return inTime;
	}
}