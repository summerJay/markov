package cn.edu.hdu.random;

import org.junit.Test;

import cn.edu.hdu.entity.Markov;
import cn.edu.hdu.entity.State;
import cn.edu.hdu.entity.Transition;

/**
 * ר����������markov��ƽ�ȷֲ��Ĺ�����
 * @author YJ
 * @version 1.0
 * */

public class CalculateDistribution {

	/**
	 * ���ô˾�̬��������ָ��markov����ƽ�ȷֲ�
	 * @param markov ����һ��markov������
	 * @return ����ָ��markov�������ƽ�ȷֲ�����
	 * */
	public static double[] stationaryDistribution(Markov markov){
		
		double[][]	p = new double[markov.getNumberOfStates()+1][markov.getNumberOfStates()+1];
		for (State state : markov.getStates()) {
			for (Transition t : state.getOutTransitions()) {
				p[state.getStateNum()][t.getNextState().getStateNum()] += t.getProbability();
			}
		}
		
		p[markov.getNumberOfStates()-1][0] = 0;
		p[markov.getNumberOfStates()-1][markov.getNumberOfStates()] = 1;
		p[markov.getNumberOfStates()][0] = 0.5;
		p[markov.getNumberOfStates()][markov.getNumberOfStates()] = 0.5;
		
		double[] y = new double[markov.getNumberOfStates()+1];
		for (int i = 0; i < y.length; i++) {
			y[i] = 1.0/(markov.getNumberOfStates()+1);
		}
		
		double[] result = new double[markov.getNumberOfStates()+1];
		double d = 0.0;
		
		do{
			d = 0.0;
			
			for (int j = 0; j < markov.getNumberOfStates()+1; j++) {
				
				double s = 0.0;
				for (int i = 0; i < markov.getNumberOfStates()+1; i++) {
					
					s+=  y[i] * p[i][j];
				}
				result[j] = s;
				d += Math.pow(result[j]-y[j], 2);
			}
			System.arraycopy(result, 0, y, 0, markov.getNumberOfStates()+1);//��result�����ֵȫ����������y
			
		}while(Math.sqrt(d) >= 1e-10 );
		
		//ȥ��result���һ��Ԫ�ز���һ����
		result[markov.getNumberOfStates()] = 0;
		double sum = 0.0;
		double[] PI = new double[markov.getNumberOfStates()];
		for (double e : result) {
			sum += e;
		}
		for (int i = 0; i < result.length-1; i++) {
			PI[i]  = result[i] / sum;
		}
		
		return PI;
	}
	
}
