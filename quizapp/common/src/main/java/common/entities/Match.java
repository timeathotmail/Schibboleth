package common.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import common.entities.annotations.*;

/**
 * This class represents a (running) match between two users.
 * 
 * @author Tim Wiechers
 */
@TableAlias(table = "MATCH__")
public class Match implements Serializable {
	/**
	 * Version number for serialization.
	 */
	private static final long serialVersionUID = -8635993418414020408L;
	/**
	 * ID for unique identification.
	 */
	private int rowid;
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
	/**
	 * List of rounds to be played in this match.
	 */
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

	/**
	 * Sets the questions to be posed in the match.
	 * 
	 * @param questions
	 *            list of questions
	 * @param perRound
	 *            number of questions per round
	 */
	public void setQuestions(List<Question> questions, int perRound) {
		List<Answer> answers = new ArrayList<Answer>();
		for (int i = 0; i < questions.size();) {
			for (int j = 0; j < perRound; j++, i++) {
				answers.add(new Answer(questions.get(i).getRowId()));
			}
		}
		rounds = new ArrayList<Round>();
		rounds.add(new Round(answers));
	}

	/**
	 * Saves an answer of one of the players.
	 * 
	 * @param user
	 *            the answering user
	 * @param index
	 *            the answer's index
	 */
	public synchronized void addAnswer(User user, int index) {
		for (Round r : rounds) {
			if (r.addAnswer(user == user1, index)) {
				break;
			}
		}
	}

	// === getters & setters ===

	public synchronized Round getNextRound(User user) {
		boolean isFirstPlayer = user.equals(user1);

		for (Round r : rounds) {
			if ((isFirstPlayer && !r.hasPlayed1())
					|| (!isFirstPlayer && !r.hasPlayed2())) {
				return r;
			}
		}
		
		return null;
	}

	/**
	 * @return true if both players have played all rounds
	 */
	public synchronized boolean isFinished() {
		return hasPlayed1() && hasPlayed2();
	}

	/**
	 * @return true if player one has played all rounds
	 */
	public synchronized boolean hasPlayed1() {
		for (Round r : rounds) {
			if (!r.hasPlayed1()) {
				return false;
			}
		}

		return true;
	}

	/**
	 * @return true if player two has played all rounds
	 */
	public synchronized boolean hasPlayed2() {
		for (Round r : rounds) {
			if (!r.hasPlayed2()) {
				return false;
			}
		}

		return true;
	}

	public synchronized int getPoints1() {
		int points = 0;

		for (Round r : rounds) {
			int w = r.getResult();
			if (w > 0) {
				points++;
			}
		}

		return points;
	}

	public synchronized int getPoints2() {
		int points = 0;

		for (Round r : rounds) {
			int w = r.getResult();
			if (w < 0) {
				points++;
			}
		}

		return points;
	}

	public int getRowId() {
		return rowid;
	}

	public void setRowId(int id) {
		this.rowid = id;

		for (Round r : rounds) {
			r.setMatchId(id);
		}
	}

	public int getUserId1() {
		return userId1;
	}

	public int getUserId2() {
		return userId2;
	}

	public User getUser1() {
		return user1;
	}

	public void setUser1(User user1) {
		this.user1 = user1;

		if (user1 != null) {
			this.userId1 = user1.getRowId();
		}
	}

	public User getUser2() {
		return user2;
	}

	public void setUser2(User user2) {
		this.user2 = user2;

		if (user2 != null) {
			this.userId2 = user2.getRowId();
		}
	}

	public List<Round> getRounds() {
		return rounds;
	}

	public void setRounds(List<Round> rounds) {
		this.rounds = rounds;
	}

	// === special methods ===

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Match [rowid=" + rowid + ", user1=" + user1 + ", user2="
				+ user2 + ", userId1=" + userId1 + ", userId2=" + userId2
				+ ", rounds=" + rounds + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rounds == null) ? 0 : rounds.hashCode());
		result = prime * result + rowid;
		result = prime * result + ((user1 == null) ? 0 : user1.hashCode());
		result = prime * result + ((user2 == null) ? 0 : user2.hashCode());
		result = prime * result + userId1;
		result = prime * result + userId2;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Match other = (Match) obj;
		if (rounds == null) {
			if (other.rounds != null)
				return false;
		} else if (!rounds.equals(other.rounds))
			return false;
		if (rowid != other.rowid)
			return false;
		if (user1 == null) {
			if (other.user1 != null)
				return false;
		} else if (!user1.equals(other.user1))
			return false;
		if (user2 == null) {
			if (other.user2 != null)
				return false;
		} else if (!user2.equals(other.user2))
			return false;
		if (userId1 != other.userId1)
			return false;
		if (userId2 != other.userId2)
			return false;
		return true;
	}

	// === special methods ===
}