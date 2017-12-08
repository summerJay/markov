package cn.edu.hdu.entity;

import java.util.List;

/**
 * ������ʾmarkov���������ṹ������һϵ��get��set������
 * 
 * @author YJ
 * @version 1.0
 * */

public class Markov {

	private List<State> states; // ��ͷ��㼯��
	private int numberOfStates; // ����markov����״̬�ڵ����
	private List<Transition> transitions; // ����markov����Ǩ�Ƽ���
	private State initialState; // ��ʼ״̬�ڵ�
	private State finalState; // ��ֹ״̬�ڵ�
	// �������Markov���ϵ�����·�����ϣ�ÿ��·����һ��Ǩ�����м���
	private List<Route> routeList;
	private double dbCoverage;// ָ�꣺���ݿ⸲����
	private double deviation;// ָ�꣺��������ʹ������ƫ��
	private int tcNumber;
	private int actualNum;
	// ��ŷֶ�ѹ��·��
	private List<OnePiece> classRoute;

	public List<State> getStates() {
		return states;
	}

	public void setStates(List<State> states) {
		this.states = states;
	}

	public List<Transition> getTransitions() {
		return transitions;
	}

	public void setTransitions(List<Transition> transitions) {
		this.transitions = transitions;
	}

	public State getInitialState() {
		return initialState;
	}

	public void setInitialState(State initialState) {
		this.initialState = initialState;
	}

	public State getFinalState() {
		return finalState;
	}

	public void setFinalState(State finalState) {
		this.finalState = finalState;
	}

	public int getNumberOfStates() {
		return numberOfStates;
	}

	public void setNumberOfStates(int numberOfStates) {
		this.numberOfStates = numberOfStates;
	}

	public List<Route> getRouteList() {
		return routeList;
	}

	public void setRouteList(List<Route> routeList) {
		this.routeList = routeList;
	}

	public int getTcNumber() {
		return tcNumber;
	}

	public void setTcNumber(int tcNumber) {
		this.tcNumber = tcNumber;
	}

	public double getDbCoverage() {
		return dbCoverage;
	}

	public void setDbCoverage(double dbCoverage) {
		this.dbCoverage = dbCoverage;
	}

	public double getDeviation() {
		return deviation;
	}

	public void setDeviation(double deviation) {
		this.deviation = deviation;
	}

	public int getActualNum() {
		return actualNum;
	}

	public void setActualNum(int actualNum) {
		this.actualNum = actualNum;
	}

	public List<OnePiece> getClassRoute() {
		return classRoute;
	}

	public void setClassRoute(List<OnePiece> classRoute) {
		this.classRoute = classRoute;
	}

}
