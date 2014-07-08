package common.entities;

/**
 * This class represents a question posed in the game.
 * 
 * @author Tim Wiechers
 */
public class Question {
	/**
	 * Enum for category distinction.
	 */
	public enum Category {
		science
	}

	/**
	 * ID for unique identification.
	 */
	private int id;
	/**
	 * The question.
	 */
	private String text;
	/**
	 * First answer.
	 */
	private String answer1;
	/**
	 * Second answer.
	 */
	private String answer2;
	/**
	 * Third answer.
	 */
	private String answer3;
	/**
	 * Fourth answer.
	 */
	private String answer4;
	/**
	 * Correct answer's index.
	 */
	private int correctIndex;
	/**
	 * The question's category.
	 */
	private Category category;

	/**
	 * Constructor for JSON deserialization and persistence framework.
	 */
	@Deprecated
	public Question() {
	}

	/**
	 * Creates an instance.
	 * @param text the question
	 * @param answer1 first answer
	 * @param answer2 second answer
	 * @param answer3 third answer
	 * @param answer4 fourth answer
	 * @param correctIndex the question's category
	 * @param category the question's category
	 */
	public Question(String text, String answer1, String answer2,
			String answer3, String answer4, int correctIndex, Category category) {
		this.text = text;
		this.answer1 = answer1;
		this.answer2 = answer2;
		this.answer3 = answer3;
		this.answer4 = answer4;
		this.correctIndex = correctIndex;
		this.category = category;
	}

	// === getters & setters ===
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getAnswer1() {
		return answer1;
	}

	public void setAnswer1(String answer1) {
		this.answer1 = answer1;
	}

	public String getAnswer2() {
		return answer2;
	}

	public void setAnswer2(String answer2) {
		this.answer2 = answer2;
	}

	public String getAnswer3() {
		return answer3;
	}

	public void setAnswer3(String answer3) {
		this.answer3 = answer3;
	}

	public String getAnswer4() {
		return answer4;
	}

	public void setAnswer4(String answer4) {
		this.answer4 = answer4;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public int getCorrectIndex() {
		return correctIndex;
	}

	public void setCorrectIndex(int correctIndex) {
		this.correctIndex = correctIndex;
	}

}
