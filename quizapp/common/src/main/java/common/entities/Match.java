package common.entities;

import java.util.ArrayList;
import java.util.List;

import common.entities.annotations.*;

/**
 * This class represents a finished match between two users.
 * 
 * @author Tim Wiechers
 */
@TableAlias(table = "MATCH__")
public class Match {
	/**
	 * ID for unique identification.
	 */
	private int id;
	/**
	 * First player.
	 */
	private User user1;
	/**
	 * Second player.
	 */
	private User user2;
	/**
	 * First player's id.
	 */
	private int userId1;
	/**
	 * Second player's id.
	 */
	private int userId2;

	private List<Round> rounds;

	/**
	 * Constructor for JSON deserialization and persistence framework.
	 */
	@Deprecated
	public Match() {
	}

	/**
	 * Creates an instance.
	 * 
	 * @param user1
	 *            first player
	 * @param user2
	 *            second player
	 */
	public Match(User user1, User user2, List<Question> questions, int perRound) {
		setUser1(user1);
		setUser2(user2);

		List<Answer> answers = new ArrayList<Answer>();
		for (int i = 0; i < questions.size();) {
			for (int j = 0; j < perRound; j++, i++) {
				answers.add(new Answer(questions.get(i).getId()));
			}
		}
		rounds.add(new Round(answers));
	}

	public void addAnswer(User user, int index) {
		for (Round r : rounds) {
			if (r.addAnswer(user == user1, index)) {
				break;
			}
		}
	}

	public boolean isFinished() {
		for (Round r : rounds) {
			if (!r.isFinished()) {
				return false;
			}
		}
		
		return true;
	}

	// === getters & setters ===

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;

		for (Round r : rounds) {
			r.setMatchId(id);
		}
	}

	public User getUser1() {
		return user1;
	}

	public void setUser1(User user1) {
		this.user1 = user1;

		if (user1 != null) {
			this.userId1 = user1.getId();
		}
	}

	public User getUser2() {
		return user2;
	}

	public void setUser2(User user2) {
		this.user2 = user2;

		if (user2 != null) {
			this.userId2 = user2.getId();
		}
	}

	public int getPoints1() {
		return 0; // TODO
	}

	public int getPoints2() {
		return 0; // TODO
	}

	// === special methods ===
}