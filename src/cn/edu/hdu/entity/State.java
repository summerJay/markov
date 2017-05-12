package cn.edu.hdu.entity;

import java.util.List;

/**
 * ������ʾmarkov����״̬�ڵ����ṹ������һϵ��get��set������
 * @author YJ
 * @version 1.0
 * */

public class State {

	private String stateName; 							 //״̬�ڵ�����
	private int stateNum;									//״̬�ڵ�ı��
	private String label = "null"; 						//״̬���
	private String notation = "null"; 				//״̬������Ϣ
	private List<Transition> outTransitions;		//״̬�ڵ�ĳ�Ǩ�ƣ�����Ǩ�ƣ�����
	private int stateAccessTimes = 0;				//״̬�ڵ�ķ��ʴ���
	
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public int getStateNum() {
		return stateNum;
	}
	public void setStateNum(int stateNum) {
		this.stateNum = stateNum;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getNotation() {
		return notation;
	}
	public void setNotation(String notation) {
		this.notation = notation;
	}
	public List<Transition> getOutTransitions() {
		return outTransitions;
	}
	public void setOutTransitions(List<Transition> outTransitions) {
		this.outTransitions = outTransitions;
	}
	public int getStateAccessTimes() {
		return stateAccessTimes;
	}
	public void setStateAccessTimes(int stateAccessTimes) {
		this.stateAccessTimes = stateAccessTimes;
	}
	
}
