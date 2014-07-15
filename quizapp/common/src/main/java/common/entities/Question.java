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
	 * States in which app revision the question was introduced.
	 */
	private int revision;
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
	 * 
	 * @param text
	 *            the question
	 * @param answer1
	 *            first answer
	 * @param answer2
	 *            second answer
	 * @param answer3
	 *            third answer
	 * @param answer4
	 *            fourth answer
	 * @param correctIndex
	 *            the question's category
	 * @param category
	 *            the question's category
	 */
	public Question(String text, String answer1, String answer2,
			String answer3, String answer4, int correctIndex,
			Category category, int revision) {
		this.text = text;
		this.answer1 = answer1;
		this.answer2 = answer2;
		this.answer3 = answer3;
		this.answer4 = answer4;
		this.correctIndex = correctIndex;
		this.category = category;
		this.revision = revision;
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

	public int getRevision() {
		return revision;
	}

	public void setRevision(int revision) {
		this.revision = revision;
	}

	// === special methods ===

	@Override
	public String toString() {
		return "Question [id=" + id + ", revision=" + revision + ", text="
				+ text + ", answer1=" + answer1 + ", answer2=" + answer2
				+ ", answer3=" + answer3 + ", answer4=" + answer4
				+ ", correctIndex=" + correctIndex + ", category=" + category
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((answer1 == null) ? 0 : answer1.hashCode());
		result = prime * result + ((answer2 == null) ? 0 : answer2.hashCode());
		result = prime * result + ((answer3 == null) ? 0 : answer3.hashCode());
		result = prime * result + ((answer4 == null) ? 0 : answer4.hashCode());
		result = prime * result
				+ ((category == null) ? 0 : category.hashCode());
		result = prime * result + correctIndex;
		result = prime * result + id;
		result = prime * result + revision;
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Question other = (Question) obj;
		if (answer1 == null) {
			if (other.answer1 != null)
				return false;
		} else if (!answer1.equals(other.answer1))
			return false;
		if (answer2 == null) {
			if (other.answer2 != null)
				return false;
		} else if (!answer2.equals(other.answer2))
			return false;
		if (answer3 == null) {
			if (other.answer3 != null)
				return false;
		} else if (!answer3.equals(other.answer3))
			return false;
		if (answer4 == null) {
			if (other.answer4 != null)
				return false;
		} else if (!answer4.equals(other.answer4))
			return false;
		if (category != other.category)
			return false;
		if (correctIndex != other.correctIndex)
			return false;
		if (id != other.id)
			return false;
		if (revision != other.revision)
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}
}
