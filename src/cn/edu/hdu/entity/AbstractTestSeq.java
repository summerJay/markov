package cn.edu.hdu.entity;

/**
 * ͨ���������ģʽ����Ĳ�����������Ϣ�����bean ����ÿ��������������Ϣ�Ĵ�ȡ
 * 
 * @author ����
 *
 */
public class AbstractTestSeq {
	private int id;// Ψһ��ʶ
	private String testSequence;// ��������

	public AbstractTestSeq(String testSequence) {
		this.testSequence = testSequence;
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

}
