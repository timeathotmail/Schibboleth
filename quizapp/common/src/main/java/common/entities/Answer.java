package common.entities;

public class Answer {
	private int roundId;
	private int questionId;
	private Question question;
	
	private int answerIndex1;
	private int answerIndex2;
	
	public Answer(int questionId) {
		this.questionId = questionId;
	}
	
	public boolean isAnswered() {
		return answerIndex1 > 0 && answerIndex2 > 0;
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
