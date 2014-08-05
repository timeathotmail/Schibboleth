package common.entities;

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
	@NotPersisted
	private User user1;
	/**
	 * Second player.
	 */
	@NotPersisted
	private User user2;
	/**
	 * First player's id.
	 */
	private int userId1;
	/**
	 * Second player's id.
	 */
	private int userId2;

	@NotPersisted
	private int points1;

	@NotPersisted
	private int points2;

	@NotPersisted
	private List<Question> questions;

	@NotPersisted
	private List<Integer> answers1;

	@NotPersisted
	private List<Integer> answers2;

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
	public Match(User user1, User user2, List<Question> questions) {
		setUser1(user1);
		setUser2(user2);
		this.questions = questions;
	}

	public Match(User user1, User user2, List<Question> questions,
			List<Integer> answers1, List<Integer> answers2) {
		super();
		setUser1(user1);
		setUser2(user2);
		this.questions = questions;
		this.answers1 = answers1;
		this.answers2 = answers2;
	}

	public void addAnswer(User user, int index) {
		if (!isFinished()) {
			if (user == user1) {
				answers1.add(index);
			} else if (user == user2) {
				answers2.add(index);
			}
		}
	}

	public boolean isFinished() {
		return answers1.size() == questions.size()
				&& answers2.size() == questions.size();
	}

	// === getters & setters ===

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
		return points1;
	}

	public int getPoints2() {
		return points2;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public List<Integer> getAnswers1() {
		return answers1;
	}

	public void setAnswers1(List<Integer> answers1) {
		this.answers1 = answers1;
	}

	public List<Integer> getAnswers2() {
		return answers2;
	}

	public void setAnswers2(List<Integer> answers2) {
		this.answers2 = answers2;
	}

	// === special methods ===
}