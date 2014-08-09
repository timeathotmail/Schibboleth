package common.entities;

import java.io.Serializable;
import java.util.List;

public class Round implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1914320813276870786L;
	private int rowid;
	private int matchId;
	private List<Answer> answers;

	@Deprecated
	public Round() {
	}

	public Round(List<Answer> answers) {
		this.answers = answers;
	}

	public boolean addAnswer(boolean isFirstUser, int index) {
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

	public boolean isFinished() {
		return hasPlayed1() && hasPlayed2();
	}
	
	public boolean hasPlayed1() {
		for (Answer a : answers) {
			if (!a.isAnswered1()) {
				return false;
			}
		}

		return true;
	}
	
	public boolean hasPlayed2() {
		for (Answer a : answers) {
			if (!a.isAnswered2()) {
				return false;
			}
		}

		return true;
	}

	public int getWinner() {
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
		
		if(p1 == p2) {
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
}
