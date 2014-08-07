package common.net.responses;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Request to signalize the server that the user left the match.
 * 
 * @author Tim Wiechers
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY)
public class MatchEnterResponse implements Serializable {

	/**
	 * Version number for serialization.
	 */
	private static final long serialVersionUID = 6079160427949683161L;
	
	private int timeLimit;
	
	@Deprecated
	public MatchEnterResponse() {
		
	}
	
	public MatchEnterResponse(int timeLimit) {
		this.timeLimit = timeLimit;
	}
	
	public int getTimeLimit() {
		return timeLimit;
	}
}
