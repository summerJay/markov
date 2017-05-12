package cn.edu.hdu.entity;

/**
 * ������ʾmarkov���е�Ǩ�Ƶ���ṹ������һϵ��get��set������
 * 
 * @author YJ
 * @version 1.0
 * */

public class Transition {

	private String transitionType; // Ǩ������
	private String name; // Ǩ�����ƣ�������
	private Stimulate stimulate;
	private double probability; // Ǩ�Ƹ���
	private String sender; // Ǩ�ƶ�Ӧ��Ϣ�ķ��Ͷ���
	private String receiver; // Ǩ�ƶ�Ӧ��Ϣ�Ľ��ն���
	private int accessTimes = 0; // Ǩ�Ƶķ��ʴ���
	private String nextStateName; // Ǩ�Ƶ�Ŀ��״̬����
	private State nextState; // Ǩ�Ƶ�Ŀ��״̬
	private boolean visited = false;// ����Ǩ�Ʒ��ʱ��
	private String assignValue;// Ǩ�Ʒ���ֵ
	private String assignType;// Ǩ�Ʒ���ֵ����
	private String conditions;// ��Ÿ��ֲ�����������

	@Override
	public String toString() {
		return "Transition [name=" + name + ", probability=" + probability
				+ ", nextStateName=" + nextStateName + "]";
	}

	public String getTransitionType() {
		return transitionType;
	}

	public void setTransitionType(String transitionType) {
		this.transitionType = transitionType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public int getAccessTimes() {
		return accessTimes;
	}

	public void setAccessTimes(int accessTimes) {
		this.accessTimes = accessTimes;
	}

	public String getNextStateName() {
		return nextStateName;
	}

	public void setNextStateName(String nextStateName) {
		this.nextStateName = nextStateName;
	}

	public State getNextState() {
		return nextState;
	}

	public void setNextState(State nextState) {
		this.nextState = nextState;
	}

	public Stimulate getStimulate() {
		return stimulate;
	}

	public void setStimulate(Stimulate stimulate) {
		this.stimulate = stimulate;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public String getAssignValue() {
		return assignValue;
	}

	public void setAssignValue(String assignValue) {
		this.assignValue = assignValue;
	}

	public String getAssignType() {
		return assignType;
	}

	public void setAssignType(String assignType) {
		this.assignType = assignType;
	}

	public String getConditions() {
		return conditions;
	}

	public void setConditions(String conditions) {
		this.conditions = conditions;
	}

}
