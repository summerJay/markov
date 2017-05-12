package cn.edu.hdu.entity;

/**
 * ͨ���������ģʽ����Ĳ�����������Ϣ�����bean ����ÿ��������������Ϣ�Ĵ�ȡ
 * 
 * @author ����
 *
 */
public class TCDetail {
	private int id;// Ψһ��ʶ
	private String testSequence;// ��������
	private String stimulateSequence;// ��������
	private String testCase;// ��������

	private static TCDetail tcDetail = null;

	private TCDetail() {
		// super();
	}

	public static TCDetail getInstance() {
		if (tcDetail == null) {
			tcDetail = new TCDetail();
		}
		return tcDetail;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTestSequence() {
		return testSequence;
	}

	public void setTestSequence(String testSequence) {
		this.testSequence = testSequence;
	}

	public String getStimulateSequence() {
		return stimulateSequence;
	}

	public void setStimulateSequence(String stimulateSequence) {
		this.stimulateSequence = stimulateSequence;
	}

	public String getTestCase() {
		return testCase;
	}

	public void setTestCase(String testCase) {
		this.testCase = testCase;
	}

}
