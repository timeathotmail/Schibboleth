package common.net.requests;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Request to signalize the server that the user left the match.
 * 
 * @author Tim Wiechers
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY)
public class MatchEnterRequest implements Serializable {

	/**
	 * Version number for serialization.
	 */
	private static final long serialVersionUID = -4533507640979437987L;

	private int matchId;
	
	@Deprecated
	public MatchEnterRequest() {
		
	}
	
	public MatchEnterRequest(int matchId) {
		this.matchId = matchId;
	}
	
	public int getMatchId() {
		return matchId;
	}
}
