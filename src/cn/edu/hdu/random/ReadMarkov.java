package cn.edu.hdu.random;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cn.edu.hdu.entity.Markov;
import cn.edu.hdu.entity.Parameter;
import cn.edu.hdu.entity.State;
import cn.edu.hdu.entity.Stimulate;
import cn.edu.hdu.entity.Transition;

/**
 * ����һ�����ڶ�ȡ���markov����XML�ļ�,���ڴ��й���markov�����ڽӱ��ʾ�ṹ
 * ���а����˻�ȡXML�ļ���dom����ĺ�����ͨ��dom�����ȡxml�ļ�����markov���� �ڽӱ��ʾ�ṹ
 * 
 * @author YJ
 * @version V1.0
 * */

public class ReadMarkov {

	private Markov markov = new Markov();

	/**
	 * ��ȡָ����xml�ļ���dom������
	 * 
	 * @return ����ָ��xml�ļ���dom����
	 * 
	 * */
	public Document getDom() throws Exception {
		SAXReader reader = new SAXReader();
		Document dom = reader.read("telephone_extend_1.xml");
		return dom;
	}

	/**
	 * ����dom���������ȡxml�ļ����ڴ����γ�markov�����ڽӱ��ʾ�ṹ
	 * 
	 * @return �����ڽӱ�ṹ��markov������
	 * */
	public Markov readMarkov() throws Exception {
		Document dom = getDom();
		Element root = dom.getRootElement();
		List stateList = root.selectNodes("//state"); // xpath�﷨
														// ��ȡ��xml�����е�state��ǩ
														// ���������ǵļ���

		List<Transition> transitions = new ArrayList<Transition>(); // �����洢����Ǩ�Ƶļ��ϣ��洢��ɸ�ֵ��markov��Ӧ����
		List<State> states = new ArrayList<State>(); // �����洢����״̬�ڵ�ļ��ϣ��洢��ɸ�ֵ��markov��Ӧ����

		int i = 0; // ��¼ÿ��״̬�ı��
		for (Object stateNode : stateList) {

			List<Transition> outTransitions = new ArrayList<Transition>(); // �����洢��������ÿ��״̬�ڵ�ĳ�Ǩ�Ƽ���

			Element state = (Element) stateNode;
			Element name = state.element("name");

			State headState = new State(); // ÿ������һ��״̬�ͽ����װ��һ����ͷ�ڵ�
			// ����ͷ�����Ӧ���Ը�ֵ
			headState.setStateName(name.getText());
			headState.setStateNum(i);
			headState.setStateAccessTimes(0);
			// ������ǰ״̬�ĺ���Ǩ��(��Ǩ��)����
			List arcs = state.element("arcs").elements("arc");
			for (Object arcNode : arcs) {
				Element arc = (Element) arcNode;
				Element arcName = arc.element("stimulate").element("name");// �������ƽڵ�
				Element nextState = arc.element("to");
				Element probability = arc.element("probability");
				// ÿ������һ����Ǩ�ƣ��ʹ���һ��Ǩ�ƶ��󣬲�����xml�ж�����ֵ��ֵ������Ӧ����
				Transition t = new Transition();
				t.setName(arcName.getText());
				t.setProbability(Double.parseDouble(probability.getText()));
				t.setAccessTimes(0);
				t.setNextStateName(nextState.getText());

				// ��װtransition�ϵļ���stimulate������ֵ��transition�����stimulate����
				Stimulate stimulate = new Stimulate();
				stimulate.setName(arcName.getText());
				Element parametersNode = arc.element("stimulate").element(
						"parameters");
				// �ȷ�װstimulate����Ĳ�����������
				List paramList = parametersNode.elements("parameter");
				List<Parameter> parameters = new ArrayList<Parameter>();// �洢ÿ��Ǩ���ϵļ����еĲ���
				for (Object parameterNode : paramList) {
					// ��װÿ������ʵ��
					Parameter parameter = new Parameter();
					Element parameterElement = (Element) parameterNode;
					Element paramName = parameterElement.element("paramName");
					Element paramType = parameterElement.element("paramType");
					Element domain = parameterElement.element("domain");
					parameter.setName(paramName.getText());
					parameter.setParamType(paramType.getText());
					parameter.setDomainType(domain.attributeValue("type"));
					parameter.setDomain(domain.getText());

					parameters.add(parameter);

				}
				stimulate.setParameters(parameters);// ���õ�ǰǨ���ϵļ����еĲ�����������

				// �ٷ�װstimulate�ϵĲ�����Լ�����ʽ����
				List cons = parametersNode.element("constraints").elements(
						"constraint");
				List<String> constraints = new ArrayList<String>();
				for (Object constraintNode : cons) {
					Element constraint = (Element) constraintNode;
					constraints.add(constraint.getText());
				}
				stimulate.setConstraints(constraints);

				t.setStimulate(stimulate);// ����ǰǨ�������stimulate���Ը�ֵ

				outTransitions.add(t); // ����ǰ��װ�ĳ�Ǩ�ƴ����Ǩ�Ƽ���
				transitions.add(t); // ����ǰ��װ��Ǩ�ƴ�����Ǩ�Ƽ���
			}
			headState.setOutTransitions(outTransitions); // ����Ǩ�Ƽ��ϸ�ֵ����ǰ��״̬�ڵ�ĳ�Ǩ�Ƽ��϶���
			// System.out.println("��"+i+"��ͷ�����"+outTransitions.size());
			// outTransitions.clear();������clear �Ѿ���ֵ��״̬�ڵ�ĳ�Ǩ�Ƽ����ֻ���null
			states.add(headState); // ����ǰ��״̬�ڵ����״̬�ڵ㼯����
			++i;
		}

		markov.setStates(states); // ��ȡ��xml֮�󣬽�״̬�ڵ㼯�ϸ�ֵ��markov��������Ӧ��״̬�ڵ㼯��
		markov.setNumberOfStates(markov.getStates().size()); // ��markov�������״̬�������Ը�ֵ
		// System.out.println("�ܹ���"+markov.getStates().size()+"��״̬�ڵ�"+markov.getStates().get(2).getOutTransitions().size());
		markov.setTransitions(transitions); // ��ȡ��xml֮�󣬽���Ǩ�Ƽ��ϸ�ֵ��markov��������Ӧ����Ǩ�Ƽ���
		// System.out.println("�ܹ���"+markov.getTransitions().size()+"��Ǩ��");

		// ��ÿ��״̬�ڵ��ÿ����Ǩ�Ƶ�nextState���Ը�ֵ����������markov�����������״̬�ڵ��ÿ����Ǩ�ƣ�
		// ͨ��ÿ����Ǩ�Ƶ�Ŀ��״̬�ڵ����Ʋ�ѯ��������״̬�ڵ㼯�ϣ�
		// �ҵ�ӵ����ͬ���Ƶ�״̬�ڵ�󣬾ͽ���ǰ��״̬�ڵ㸳ֵ����ǰ��Ǩ�Ƶ�nextState���ԡ�
		for (State state : markov.getStates()) {
			// System.out.println(state.getOutTransitions().size());
			for (Transition t : state.getOutTransitions()) {

				for (State state2 : markov.getStates()) {

					if (t.getNextStateName().equals(state2.getStateName())) {

						t.setNextState(state2);
						break;
					}
				}
			}
		}

		// ����markov���ĳ�ʼ״̬�ڵ�
		markov.getStates().get(0).setLabel("initial");
		markov.setInitialState(markov.getStates().get(0));
		// ����markov������ֹ״̬�ڵ�
		markov.getStates().get(markov.getStates().size() - 1).setLabel("final");
		markov.setFinalState(markov.getStates().get(
				markov.getStates().size() - 1));

		// System.out.println(markov.getTransitions().size());
		return markov; // ������ɹ����ڽӱ�ṹ��markov�����󷵻�
	}
}
