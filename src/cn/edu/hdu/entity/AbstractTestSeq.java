package cn.edu.hdu.entity;

/**
 * 通过单例设计模式构造的测试用例各信息的组合bean 方便每条测试用例各信息的存取
 * 
 * @author 夏沐
 *
 */
public class AbstractTestSeq {
	private int id;// 唯一标识
	private String testSequence;// 测试序列

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
