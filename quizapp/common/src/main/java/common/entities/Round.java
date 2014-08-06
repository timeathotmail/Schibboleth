package common.entities;

import java.io.Serializable;
import java.util.List;

public class Round implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1914320813276870786L;
	private int id;
	private int matchId;
	private List<Answer> answers;

	public Round(List<Answer> answers) {
		this.answers = answers;
	}

	public boolean addAnswer(boolean isFirstUser, int index) {
		for (Answer a : answers) {
			if(isFirstUser) {
				if(a.getAnswerIndex1() == 0) {
					a.setAnswerIndex1(index);
					return true;
				} 
			} else {
				if(a.getAnswerIndex2() == 0) {
					a.setAnswerIndex2(index);
					return true;
				} 
			}
		}
		
		return false;
	}
	
	public boolean isFinished() {
		for (Answer a : answers) {
			if (!a.isAnswered()) {
				return false;
			}
		}
		
		return true;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;

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
}
