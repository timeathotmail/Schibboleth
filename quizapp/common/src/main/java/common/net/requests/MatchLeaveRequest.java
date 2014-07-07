package common.net.requests;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Request to signalize the server that the user left the match.
 * 
 * @author Tim Wiechers
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY)
public class MatchLeaveRequest {
}
