package common.net.requests;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Request to signalize the server that the user disconnected and the respective
 * socket can be closed.
 * 
 * @author Tim Wiechers
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY)
public class LogoutRequest {
}
