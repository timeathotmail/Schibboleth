package common.entities;

import java.io.Serializable;

/**
 * Objects of this class represent the answers of two users to a given question.
 * 
 * @author Tim Wiechers
 */
public class Answer implements Serializable {
	/**
	 * Version number for serialization.
	 */
	private static final long serialVersionUID = -1141900858713375550L;
	/**
	 * Answer index indicating that the user didn't answer in time.
	 */
	public final static int NOT_ANSWERED_INDEX = 5;
	/**
	 * ID for unique identification.
	 */
	private int rowid;
	/**
	 * The round's rowid.
	 */
	private int roundId;
	/**
	 * The posed question's rowid.
	 */
	private int questionId;
	/**
	 * The posed question.
	 */
	private Question question;
	/**
	 * Answer index of player one.
	 */
	private int answerIndex1;
	/**
	 * Answer index of player two.
	 */
	private int answerIndex2;

	/**
	 * Constructor for JSON deserialization and persistence framework.
	 */
	@Deprecated
	public Answer() {
	}

	/**
	 * Creates an instance.
	 * 
	 * @param questionId
	 *            the posed question's rowid
	 */
	public Answer(int questionId) {
		this.questionId = questionId;
	}

	// === getters & setters ===
	
	/**
	 * @return true if both players have answered
	 */
	public boolean isAnswered() {
		return isAnswered1() && isAnswered2();
	}

	/**
	 * @return true if player one has answered
	 */
	public boolean isAnswered1() {
		return answerIndex1 > 0;
	}

	/**
	 * @return true if player two has answered
	 */
	public boolean isAnswered2() {
		return answerIndex2 > 0;
	}

	public int getRowId() {
		return rowid;
	}

	public void setRowId(int id) {
		this.rowid = id;
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public int getRoundId() {
		return roundId;
	}

	public void setRoundId(int roundId) {
		this.roundId = roundId;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;

		if (question != null) {
			this.questionId = question.getRowId();
		}
	}

	public int getAnswerIndex1() {
		return answerIndex1;
	}

	public void setAnswerIndex1(int answerIndex1) {
		this.answerIndex1 = answerIndex1;
	}

	public int getAnswerIndex2() {
		return answerIndex2;
	}

	public void setAnswerIndex2(int answerIndex2) {
		this.answerIndex2 = answerIndex2;
	}

	// === special methods ===

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Answer [rowid=" + rowid + ", roundId=" + roundId
				+ ", questionId=" + questionId + ", question=" + question
				+ ", answerIndex1=" + answerIndex1 + ", answerIndex2="
				+ answerIndex2 + "]";
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
		result = prime * result + answerIndex1;
		result = prime * result + answerIndex2;
		result = prime * result
				+ ((question == null) ? 0 : question.hashCode());
		result = prime * result + questionId;
		result = prime * result + roundId;
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
		Answer other = (Answer) obj;
		if (answerIndex1 != other.answerIndex1)
			return false;
		if (answerIndex2 != other.answerIndex2)
			return false;
		if (question == null) {
			if (other.question != null)
				return false;
		} else if (!question.equals(other.question))
			return false;
		if (questionId != other.questionId)
			return false;
		if (roundId != other.roundId)
			return false;
		if (rowid != other.rowid)
			return false;
		return true;
	}
}
