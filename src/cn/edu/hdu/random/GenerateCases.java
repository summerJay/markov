package cn.edu.hdu.random;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dom4j.Element;

import cn.edu.hdu.entity.Markov;
import cn.edu.hdu.entity.State;
import cn.edu.hdu.entity.Stimulate;
import cn.edu.hdu.entity.Transition;

/**
 * ����һ��ר�������������������Ͳ���·�����࣬���а������ɲ���������·���ķ����� ѡ����һ��Ǩ�Ƶķ��������������������·���ķ����ȡ�
 * 
 * @author YJ
 * @version 1.0
 * */

public class GenerateCases {

	private static final int COEFFICIENT = 8; // ����һ������ϵ��
	private int oneBatchSize = 1000; // ���ɵ�ÿһ���Ĳ�����������
	private List<List<Integer>> testPaths = new ArrayList<List<Integer>>(); // ����·������
	public List<List<String>> testCases = new ArrayList<List<String>>(); // ������������
	public List<List<Stimulate>> testCasesExtend = new ArrayList<List<Stimulate>>();
	public List<String> abstractTS = new ArrayList<String>();
	// �����һ�����ɷ�ʽ�����ݿ⸲����//////////////////////////
	Set<String> set = new HashSet<String>();

	// ///////////////////////////////////////////////////////////
	/**
	 * ��������������·������ӡ������̨��ͬʱ������д���ļ���
	 * 
	 * @param markov
	 *            ����һ��markov������
	 * @param bufw
	 *            ����һ���ļ����������
	 * @return ���ص�ǰ�����Ĳ��������Ͳ���·���ĸ���
	 * */
	public int generate(Markov markov, Element root) throws IOException {

		// oneBatchSize = (int) (markov.getStates().size()
		// * markov.getStates().size() * 0.5); // ����ÿһ������N*50����������
		// System.out.println(oneBatchSize + "------------------------");
		int startSize = testCases.size();

		while (testCases.size() - startSize < oneBatchSize) {

			List<Integer> onePath = new ArrayList<Integer>(); // �洢��ǰ���ɵ�һ������·��
			List<String> oneCase = new ArrayList<String>(); // �洢��ǰ���ɵ�һ����������
			List<Stimulate> oneCaseExtend = new ArrayList<Stimulate>();// �洢��ǰ���ɵ�һ������������չ��
			// ////// ��¼ÿ��·���ĸ���///////////////////////////////////
			double routeProb = 1.0;
			String oneRoute = "";
			// ///////////////////////////////////////////////////////////
			onePath.add(0);
			oneCase.add(markov.getStates().get(0).getStateName());

			State currentState = markov.getInitialState();
			// System.out.println(currentState.getStateName()+currentState.getLabel());
			currentState
					.setStateAccessTimes(currentState.getStateAccessTimes() + 1);

			while (!currentState.getLabel().equals("final")) {
				// ��ӡ��ǰ״̬�ڵ��������Ǩ�Ƹ���

				// System.out.println("currentStateName:"
				// + currentState.getStateName() + "#"
				// + currentState.getOutTransitions().size());

				Transition nextTransition = rouletteWheels(currentState
						.getOutTransitions()); // ͨ�������㷨��ȡ��ǰ״̬�ڵ����һ����Ǩ��

				// �������һ���ݿ⸲����///////////////////////////////
				routeProb *= nextTransition.getProbability();
				oneRoute += nextTransition.getStimulate().getName();
				// //////////////////////////////////////////////////////

				nextTransition
						.setAccessTimes(nextTransition.getAccessTimes() + 1);

				currentState = nextTransition.getNextState();
				onePath.add(currentState.getStateNum());
				oneCase.add(nextTransition.getName());
				// �ѵ�ǰǨ������ļ�������洢������
				oneCaseExtend.add(nextTransition.getStimulate());

				currentState.setStateAccessTimes(currentState
						.getStateAccessTimes() + 1);

			}

			testPaths.add(onePath);
			testCases.add(oneCase);
			testCasesExtend.add(oneCaseExtend);
			// ���ݿ⸲����////////////////////////////////////////////
			boolean result = set.add(oneRoute);
			if (result) {
				markov.setDbCoverage(markov.getDbCoverage() + routeProb);
			}
			// ///////////////////////////////////////////////////////
			// ��װ�ɲ���������ϸjavabean

			// bufw.write(oneCase.toString());
			// bufw.newLine();
			// bufw.flush();

			// printCaseAndPath(oneCase, onePath);
			printCaseAndPath(oneCase, oneCaseExtend, onePath, root);
		}

		return testCases.size();
	}

	private void printCaseAndPath(List<String> oneCase,
			List<Stimulate> oneCaseExtend, List<Integer> onePath, Element root) {

		// System.out.print("��������:");
		String testSequence = "";
		for (int i = 0; i < oneCase.size(); i++) {
			if (i != oneCase.size() - 1) {
				testSequence = testSequence + oneCase.get(i) + "-->>";
				// System.out.print(oneCase.get(i) + "-->>");
			} else {
				testSequence = testSequence + oneCase.get(i);
				// System.out.println(oneCase.get(i));
			}
		}
		// System.out.println(testSequence);
		abstractTS.add(testSequence);
		/*
		 * TCDetail.getInstance().setTestSequence(testSequence);
		 * 
		 * System.out.print("��������:"); String stimulateSequence = ""; for (int i
		 * = 0; i < oneCaseExtend.size(); i++) { if (i != oneCaseExtend.size() -
		 * 1) { stimulateSequence = stimulateSequence +
		 * oneCaseExtend.get(i).toString() + "-->>"; //
		 * System.out.print(oneCaseExtend.get(i).toString() + "-->>"); } else {
		 * stimulateSequence = stimulateSequence +
		 * oneCaseExtend.get(i).toString(); //
		 * System.out.println(oneCaseExtend.get(i).toString()); } }
		 * System.out.println(stimulateSequence);
		 * TCDetail.getInstance().setStimulateSequence(stimulateSequence);
		 * 
		 * System.out.println("��������:"); // Evaluation.getValue(oneCaseExtend);
		 * 
		 * // RandomCase.getCase(oneCaseExtend, root);
		 * 
		 * System.out.print("����·��:"); for (int i = 0; i < onePath.size(); i++) {
		 * if (i != onePath.size() - 1) { System.out.print(onePath.get(i) +
		 * "-->>"); } else { System.out.println(onePath.get(i));
		 * System.out.println(); } }
		 */

	}

	/**
	 * �������Ĳ��������Ͳ���·�����������̨
	 * 
	 * @param oneCase
	 *            ����һ����������
	 * @param onePath
	 *            ����һ������·��
	 * */
	private void printCaseAndPath(List<String> oneCase, List<Integer> onePath) {

		System.out.print("��������:");
		for (int i = 0; i < oneCase.size(); i++) {
			if (i != oneCase.size() - 1) {
				System.out.print(oneCase.get(i) + "-->>");
			} else {
				System.out.println(oneCase.get(i));
			}
		}

		System.out.print("����·��:");
		for (int i = 0; i < onePath.size(); i++) {
			if (i != onePath.size() - 1) {
				System.out.print(onePath.get(i) + "-->>");
			} else {
				System.out.println(onePath.get(i));
				System.out.println();
			}
		}
	}

	/**
	 * �����㷨: ����ѡ�����Ǩ��
	 * 
	 * @param outTransitions
	 *            ����һ��״̬�ڵ�ĳ�Ǩ�Ƽ���
	 * @return �������ö����㷨�ӳ�Ǩ�Ƽ�����ѡ�����һ��Ǩ��
	 * */
	private Transition rouletteWheels(List<Transition> outTransitions) {

		int i = 0;
		double[] probs = new double[outTransitions.size()];
		for (Transition t : outTransitions) {
			probs[i++] = t.getProbability();
		}

		double sum = 0.0;
		for (int j = 0; j < probs.length; j++) {
			sum += probs[j];
			probs[j] = sum;
		}
		for (int j = 0; j < probs.length; j++) {
			probs[j] /= sum;
		}

		double random = Math.random();
		for (int j = 0; j < probs.length; j++) {
			if (random <= probs[j]) {
				return outTransitions.get(j);
			}
		}

		System.out.println("ѡ�����Ǩ��ʧ��!");
		return null;
	}

	public int getOneBatchSize() {
		return oneBatchSize;
	}

	public void setOneBatchSize(int oneBatchSize) {
		this.oneBatchSize = oneBatchSize;
	}

	public List<List<Integer>> getTestPaths() {
		return testPaths;
	}

	public void setTestPaths(List<List<Integer>> testPaths) {
		this.testPaths = testPaths;
	}

	public List<List<String>> getTestCases() {
		return testCases;
	}

	public void setTestCases(List<List<String>> testCases) {
		this.testCases = testCases;
	}

	public static int getCoefficient() {
		return COEFFICIENT;
	}

}
