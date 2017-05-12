package cn.edu.hdu.random;

import cn.edu.hdu.entity.Markov;
import cn.edu.hdu.entity.State;
import cn.edu.hdu.entity.Transition;

/**
 * ר����������ʹ�����Ͳ����������ƶȵĹ����࣬��������ƽ�ȷֲ���ŷ�Ͼ���������󷨡�
 * @author YJ
 * @version 1.0
 * */

public class CalculateSimilarity {
	
	/**
	 * ����ƽ�ȷֲ���ʹ�����Ͳ����������ƶ�
	 * @param markov ����һ��markov������
	 * @param PI ����ָ��markov�������ƽ�ȷֲ�����
	 * @return ��������ƽ�ȷֲ����������ָ��markov����ʹ�����Ͳ����������ƶ�
	 * */
	public static double statistic(Markov markov, double[] PI) {

		double dis = 0.0;
		
		for (State state : markov.getStates()) {
			
			int totalTimes = 0;
			for (Transition t : state.getOutTransitions()) {
				
				totalTimes += t.getAccessTimes();
			}
			for (Transition t : state.getOutTransitions()) {
				
				dis += PI[state.getStateNum()] * t.getProbability() * (Math.log10(t.getProbability()/(t.getAccessTimes()*1.0/totalTimes)) / Math.log10(2));
			}
		}
		
		return dis;
	}
	
	/**
	 * ����ŷ�Ͼ�����ʹ�����Ͳ����������ƶ�
	 * @param markov ����һ��markov������
	 * @return ��������ŷ�Ͼ��뷨�����ָ��markov����ʹ�����Ͳ����������ƶ�
	 * */
	public static double statistic_1(Markov markov) {
		
		double distance = 0.0;
		for (State state : markov.getStates()) {
			
			int totalTimes = 0;
			for (Transition t : state.getOutTransitions()) {
				
				totalTimes += t.getAccessTimes();
			}
			for (Transition t : state.getOutTransitions()) {
				
				distance += Math.pow(t.getAccessTimes()*1.0/totalTimes-t.getProbability(), 2);
			}
		}
		return Math.sqrt(distance);
	}
	
}
