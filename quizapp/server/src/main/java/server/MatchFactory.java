package server;

import java.util.List;

import common.entities.Match;
import common.entities.Question;
import common.entities.User;

public class MatchFactory {
	public static final int ROUND_COUNT = 10;

	private final Match match;
	private final List<Question> questions;
	private int q_index;
	private boolean _continue = true;

	public MatchFactory(User user1, User user2, List<Question> questions) {
		this.match = new Match(user1, user2);
		this.questions = questions;
	}

	public synchronized void addAnswer(User user, int answerIndex) {
		if (!isFinished()) {
			// add point if answer is correct
			if (answerIndex == questions.get(q_index).getCorrectIndex()) {
				if (user == match.getUser1()) {
					match.setPoints1(match.getPoints1() + 1);
				} else {
					match.setPoints2(match.getPoints2() + 1);
				}
			}

			// next question after two answers
			if ((_continue = !_continue)) {
				q_index++;
			}
		}
	}

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

			q_index = ROUND_COUNT;
		}
	}

	public synchronized boolean isFinished() {
		return q_index == ROUND_COUNT;
	}

	public Match getMatch() {
		return match;
	}
}