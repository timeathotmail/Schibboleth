package common.net.requests;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Request to send a section of the user ranking.
 * 
 * @author Tim Wiechers
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY)
public class GetRankingsRequest implements Serializable {
	/**
	 * Version number for serialization.
	 */
	private static final long serialVersionUID = -9202879758595015205L;
	/**
	 * The ranking offset.
	 */
	private int offset;
	/**
	 * The amount of ranked users to send
	 */
	private int length;
	
	/**
	 * Constructor for JSON deserialization.
	 */
	@Deprecated
	public GetRankingsRequest() {
	}

	/**
	 * Creates an instance.
	 * @param offset the ranking offset
	 * @param the amount of ranked users to send
	 */
	public GetRankingsRequest(int offset, int length) {
		this.offset = offset;
		this.length = length;
	}

	// === getters ===
	
	public int getOffset() {
		return offset;
	}

	public int getLength() {
		return length;
	}
}