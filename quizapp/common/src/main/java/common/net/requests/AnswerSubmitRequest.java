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
	 * The rowId of the match.
	 */
	private int matchRowId;
	/**
	 * The answer's index.
	 */
	private int index;
	/**
	 * Indicates whether the user selected an answer in the time limit.
	 */
	private boolean inTime;

	/**
	 * Constructor for JSON deserialization.
	 */
	@Deprecated
	public AnswerSubmitRequest() {
	}

	/**
	 * Creates an instance.
	 * 
	 * @param matchRowId
	 *            the rowId of the match
	 * @param index
	 *            index the answer's index
	 * @param inTime
	 *            true if the user selected an answer in the time limit
	 */
	public AnswerSubmitRequest(int matchRowId, int index, boolean inTime) {
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