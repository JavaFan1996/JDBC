package selfTest;

public class Student {

	// ��ˮ��
	private int flowId;
	// ���Ե�����
	private int type;
	// ���֤��
	private String idCard;
	// ׼��֤��
	private String examCard;
	// ѧ����
	private String studentName;
	// ѧ����ַ
	private String location;
	// ���Է���.
	private int grade;


	public int getFlowId() {
		return flowId;
	}

	public void setFlowId(int flowId) {
		this.flowId = flowId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getExamCard() {
		return examCard;
	}

	public void setExamCard(String examCard) {
		this.examCard = examCard;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public Student(int flowId, int type, String idCard, String examCard,
                   String studentName, String location, int grade) {
		super();
		this.flowId = flowId;
		this.type = type;
		this.idCard = idCard;
		this.examCard = examCard;
		this.studentName = studentName;
		this.location = location;
		this.grade = grade;
	}

	public Student() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Student [flowId=" + flowId + ", type=" + type + ", idCard="
				+ idCard + ", examCard=" + examCard + ", studentName="
				+ studentName + ", location=" + location + ", grade=" + grade
				+ "]";
	}

}
