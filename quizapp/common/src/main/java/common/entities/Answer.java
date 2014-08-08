package common.entities;

import java.io.Serializable;

public class Answer implements Serializable {
	public final static int NOT_ANSWERED_INDEX = 5;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1141900858713375550L;
	private int id;
	private int roundId;
	private int questionId;
	private Question question;
	
	private int answerIndex1;
	private int answerIndex2;
	
	@Deprecated
	public Answer() {
	}
	
	public Answer(int questionId) {
		this.questionId = questionId;
	}
	
	public boolean isAnswered() {
		return isAnswered1() && isAnswered2();
	}
	
	public boolean isAnswered1() {
		return answerIndex1 > 0;
	}
	
	public boolean isAnswered2() {
		return answerIndex2 > 0;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
		
		if(question != null) {
			this.questionId = question.getId();
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
}
