package cn.edu.hdu.assign;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hdu.entity.Markov;
import cn.edu.hdu.entity.Route;
import cn.edu.hdu.entity.State;
import cn.edu.hdu.entity.Transition;
import cn.edu.hdu.utils.Constant;

/**
 * ����������װ�ռ�Markov��·���ȵ���ع���
 * 
 * @author ����
 * 
 */
public class CollectRoute {

	private List<Route> routeList;// ���Markov���ϵ�·������
	private List<Transition> transitionList;// �����ϵ���ջ���� �ռ�ÿһ��·��
	private double routeProbability = 1.0;
	private int tcNumber;

	public void collect(Markov markov) {
		routeList = new ArrayList<Route>();
		transitionList = new ArrayList<Transition>();
		tcNumber = markov.getTcNumber();
		State initialState = markov.getInitialState();
		initialState.setStateAccessTimes(1);
		dfs(initialState);
		markov.setRouteList(routeList);
		int actualTcNum = getActualTcNum(routeList);
		// ����������������೬��100�������ƻ�����û����10��
		if (tcNumber - actualTcNum > 10 && Constant.maxCircle < 11) {
			Constant.maxCircle++;
			collect(markov);
		}
		double dbCoverage = 0;
		for (Route route : routeList) {
			dbCoverage += route.getRouteProbability();
		}
		markov.setDbCoverage(dbCoverage);
	}

	private int getActualTcNum(List<Route> rl) {
		int actualTcNum = 0;
		for (Route route : rl) {
			actualTcNum += route.getNumber();
		}
		return actualTcNum;
	}

	/**
	 * ��Markov������һ������������������ռ�����·��
	 * 
	 * @param initialState
	 *            ��ʼ״̬
	 */
	private void dfs(State initialState) {
		List<Transition> outTransitions = initialState.getOutTransitions();
		if (outTransitions != null && outTransitions.size() != 0) {
			for (Transition transition : outTransitions) {
				// if (transition.isVisited() == false) {
				// transition.setVisited(true);
				// �����ڵ��Ѿ����ʹ����ξͻ�Ǩ�ƣ��Դ˱�֤��·ֻ��һ��
				if (transition.getNextState().getStateAccessTimes() == Constant.maxCircle) {
					continue;
				}
				// ÿ������һ��״̬�ڵ㣬ʹ��ڵ���ʴ���+1
				transition.getNextState().setStateAccessTimes(
						transition.getNextState().getStateAccessTimes() + 1);
				transitionList.add(transition);
				routeProbability = routeProbability
						* transition.getProbability();
				State nextState = transition.getNextState();
				if (nextState.getLabel().equals("final")) {
					List<Transition> oneRoute = new ArrayList<Transition>();
					oneRoute.addAll(transitionList);
					Route route = new Route();
					route.setTransitionList(oneRoute);
					route.setRouteProbability(routeProbability);

					route.setNumber((int) Math.round(tcNumber
							* routeProbability));

					// route.setNumber((int) (tcNumber * routeProbability));
					routeList.add(route);
				}
				dfs(nextState);
				transitionList.remove(transitionList.size() - 1);
				routeProbability = routeProbability
						/ transition.getProbability();
				// ���ݵ�ʱ��ǵ�Ҫ�ѽڵ�ķ��ʴ�����һ
				nextState
						.setStateAccessTimes(nextState.getStateAccessTimes() - 1);
				// }
			}
		}
	}
}
