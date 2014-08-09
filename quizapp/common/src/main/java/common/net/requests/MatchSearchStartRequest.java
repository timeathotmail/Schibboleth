package common.net.requests;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Request to signalize the server that the user wants to be matched with an
 * opponent.
 * 
 * @author Tim Wiechers
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY)
public class MatchSearchStartRequest implements Serializable {
	/**
	 * Version number for serialization.
	 */
	private static final long serialVersionUID = 4894237704843669846L;
}
