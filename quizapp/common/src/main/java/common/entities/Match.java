package common.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import common.entities.annotations.*;

/**
 * This class represents a finished match between two users.
 * 
 * @author Tim Wiechers
 */
@TableAlias(table = "MATCH__")
public class Match implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8635993418414020408L;
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
	public int getUserId1() {
		return userId1;
	}

	public int getUserId2() {
		return userId2;
	}

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
	public Match(User user1, User user2) {
		setUser1(user1);
		setUser2(user2);
	}

	public void addAnswer(User user, int index) {
		for (Round r : rounds) {
			if (r.addAnswer(user == user1, index)) {
				break;
			}
		}
	}

	public boolean isFinished() {
		return hasPlayed1() && hasPlayed2();
	}
	
	public void setQuestions(List<Question> questions, int perRound) {
		List<Answer> answers = new ArrayList<Answer>();
		for (int i = 0; i < questions.size();) {
			for (int j = 0; j < perRound; j++, i++) {
				answers.add(new Answer(questions.get(i).getId()));
			}
		}
		rounds = new ArrayList<Round>();
		rounds.add(new Round(answers));
	}
	
	public boolean hasPlayed1() {
		for (Round r : rounds) {
			if (!r.hasPlayed1()) {
				return false;
			}
		}
		
		return true;
	}
	
	public boolean hasPlayed2() {
		for (Round r : rounds) {
			if (!r.hasPlayed2()) {
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
		int points = 0;
		
		for (Round r : rounds) {
			int w = r.getWinner();
			if(w > 0) {
				points++;
			}
		}
		
		return points;
	}

	public int getPoints2() {
		int points = 0;
		
		for (Round r : rounds) {
			int w = r.getWinner();
			if(w < 0) {
				points++;
			}
		}
		
		return points;
	}

	public List<Round> getRounds() {
		return rounds;
	}

	public void setRounds(List<Round> rounds) {
		this.rounds = rounds;
	}

	// === special methods ===
}