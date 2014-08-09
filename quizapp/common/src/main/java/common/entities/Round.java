package common.entities;

import java.io.Serializable;
import java.util.List;

/**
 * This class represents a round of a match.
 * 
 * @author Tim Wiechers
 */
public class Round implements Serializable {
	/**
	 * Version number for serialization.
	 */
	private static final long serialVersionUID = -1914320813276870786L;
	/**
	 * ID for unique identification.
	 */
	private int rowid;
	/**
	 * The rowid of the match the round is played in.
	 */
	private int matchId;
	/**
	 * List of questions & player answers.
	 */
	private List<Answer> answers;

	/**
	 * Constructor for JSON deserialization and persistence framework.
	 */
	@Deprecated
	public Round() {
	}

	/**
	 * Creates an instance.
	 * 
	 * @param answers
	 *            list of questions & player answers
	 */
	public Round(List<Answer> answers) {
		this.answers = answers;
	}

	/**
	 * Saves an answer of a player.
	 * 
	 * @param isFirstUser
	 *            true if the first player answered
	 * @param index
	 *            the answer's index
	 * @return
	 */
	public synchronized boolean addAnswer(boolean isFirstUser, int index) {
		for (Answer a : answers) {
			if (isFirstUser) {
				if (a.getAnswerIndex1() == 0) {
					a.setAnswerIndex1(index);
					return true;
				}
			} else {
				if (a.getAnswerIndex2() == 0) {
					a.setAnswerIndex2(index);
					return true;
				}
			}
		}

		return false;
	}

	// === getters & setters ===

	/**
	 * @return true if both players finished this round
	 */
	public synchronized boolean isFinished() {
		return hasPlayed1() && hasPlayed2();
	}

	/**
	 * @return true if player one finished this round
	 */
	public synchronized boolean hasPlayed1() {
		for (Answer a : answers) {
			if (!a.isAnswered1()) {
				return false;
			}
		}

		return true;
	}

	/**
	 * @return true if player two finished this round
	 */
	public synchronized boolean hasPlayed2() {
		for (Answer a : answers) {
			if (!a.isAnswered2()) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Indicates the result of the round.
	 * 
	 * @return 1 if player one won, -1 of player two won, 0 in case of draw
	 */
	public int getResult() {
		int p1 = 0;
		int p2 = 0;

		for (Answer a : answers) {
			if (a.getAnswerIndex1() == a.getQuestion().getCorrectIndex()) {
				p1++;
			}
			if (a.getAnswerIndex2() == a.getQuestion().getCorrectIndex()) {
				p2++;
			}
		}

		if (p1 == p2) {
			return 0;
		}

		return p1 > p2 ? 1 : -1;
	}

	public int getRowId() {
		return rowid;
	}

	public void setRowId(int id) {
		this.rowid = id;

		for (Answer a : answers) {
			a.setRoundId(id);
		}
	}

	public int getMatchId() {
		return matchId;
	}

	public void setMatchId(int matchId) {
		this.matchId = matchId;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

	// === special methods ===

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Round [rowid=" + rowid + ", matchId=" + matchId + ", answers="
				+ answers + "]";
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
		result = prime * result + ((answers == null) ? 0 : answers.hashCode());
		result = prime * result + matchId;
		result = prime * result + rowid;
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
		Round other = (Round) obj;
		if (answers == null) {
			if (other.answers != null)
				return false;
		} else if (!answers.equals(other.answers))
			return false;
		if (matchId != other.matchId)
			return false;
		if (rowid != other.rowid)
			return false;
		return true;
	}
}
