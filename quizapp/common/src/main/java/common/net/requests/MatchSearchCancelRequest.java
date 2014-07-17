package common.net.requests;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Request to signalize the server that the user doesn't want to be matched with
 * an opponent anymore.
 * 
 * @author Tim Wiechers
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY)
public class MatchSearchCancelRequest implements Serializable {
	/**
	 * Version number for serialization.
	 */
	private static final long serialVersionUID = -3271397197872558344L;
}
