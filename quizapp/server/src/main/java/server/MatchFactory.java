package server;

import java.util.List;

import javax.naming.ConfigurationException;

import common.entities.Match;
import common.entities.Question;
import common.entities.User;
import common.utils.Config;

/**
 * This class represents a running match.
 * 
 * @author Tim Wiechers
 */
public class MatchFactory {
	/**
	 * Amount of rounds per match.
	 */
	public static int ROUND_COUNT;
	/**
	 * Amount of questions per round.
	 */
	public static int QUESTIONS_PER_ROUND;
	/**
	 * The resulting match instance.
	 */
	private final Match match;
	/**
	 * The questions posed during the match.
	 */
	private final List<Question> questions;
	/**
	 * Current question index.
	 */
	private int q_index;
	/**
	 * Used to increase {@link #q_index} after every second answer.
	 */
	private boolean _continue = true;
	/**
	 * Number of points the first player scored in the current round.
	 */
	private int roundPoints1;
	/**
	 * Number of points the second player scored in the current round.
	 */
	private int roundPoints2;

	/**
	 * Get properties.
	 */
	static {
		try {
			ROUND_COUNT = Config.get().getInt("ROUND_COUNT");
			QUESTIONS_PER_ROUND = Config.get().getInt("QUESTIONS_PER_ROUND");
		} catch (ConfigurationException e) {
			// TODO 
		}
	}

	/**
	 * Creates an instance.
	 * 
	 * @param user1
	 *            first player
	 * @param user2
	 *            second player
	 * @param questions
	 *            the questions posed during the match
	 * @throws IllegalArgumentException
	 *             when there are not enough questions provided
	 */
	public MatchFactory(User user1, User user2, List<Question> questions)
			throws IllegalArgumentException {
		if (questions.size() < ROUND_COUNT * QUESTIONS_PER_ROUND) {
			throw new IllegalArgumentException("Not enough questions");
		}

		this.match = new Match(user1, user2);
		this.questions = questions;
	}

	/**
	 * Saves an answer for a user.
	 * 
	 * @param user
	 *            answering user
	 * @param answerIndex
	 *            chosen answer's index
	 */
	public synchronized void addAnswer(User user, int answerIndex) {
		if (!isFinished()) {
			// add roundPoint if answer is correct
			if (answerIndex == questions.get(q_index).getCorrectIndex()) {
				if (user == match.getUser1()) {
					roundPoints1++;
				} else {
					roundPoints2++;
				}
			}

			// next question after two answers
			if ((_continue = !_continue)) {
				q_index++;
				
				// set scores
				if(roundPoints1 >= roundPoints2) {
					match.setPoints1(match.getPoints1() + 1);
				}
				
				if(roundPoints2 >= roundPoints1) {
					match.setPoints1(match.getPoints2() + 1);
				}
			}
		}
	}

	/**
	 * Ends a match and declares the opponent the winner.
	 * 
	 * @param user
	 *            the forfeiting player
	 */
	public synchronized void forfeit(User user) {
		if (!isFinished()) {
			// make the other user win
			if (user == match.getUser1()) {
				match.setPoints1(0);
				match.setPoints2(Math.max(1, match.getPoints2()));
			} else {
				match.setPoints1(Math.max(1, match.getPoints2()));
				match.setPoints2(0);
			}

			// isFinished() = true
			q_index = ROUND_COUNT * QUESTIONS_PER_ROUND;
		}
	}

	/**
	 * @return true if all questions have been answered
	 */
	public synchronized boolean isFinished() {
		return q_index == ROUND_COUNT * QUESTIONS_PER_ROUND;
	}

	/**
	 * @return the resulting match instance
	 */
	public Match getMatch() {
		return match;
	}
}