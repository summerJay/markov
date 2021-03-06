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
 * 建立一个用于读取存放markov链的XML文件,在内存中构成markov链的邻接表表示结构
 * 其中包含了获取XML文件的dom对象的函数和通过dom对象读取xml文件构成markov链的 邻接表表示结构
 * 
 * @author YJ
 * @version V1.0
 * */

public class ReadMarkov2 {

	private Markov markov = new Markov();

	/**
	 * 获取指定的xml文件的dom树对象。
	 * 
	 * @return 返回指定xml文件的dom对象
	 * 
	 * */
	public Document getDom() throws Exception {
		SAXReader reader = new SAXReader();
		// Document dom = reader.read("telephone_extend_2.xml");
		// Document dom = reader.read("EADemo.xml");
		Document dom = reader.read("EATimeDemo_TimeExtend.xml");

		// Document dom = reader.read("VioletTimeDemo_TimeExtend.xml");
		// Document dom = reader.read("VioletDemo.xml");
		// Document dom = reader.read("Software_MarkovChainModel1.xml");
		// Document dom = reader.read("su3.xml");
		return dom;
	}

	/**
	 * 利用dom对象解析读取xml文件在内存中形成markov链的邻接表表示结构
	 * 
	 * @return 返回邻接表结构的markov链对象
	 * */
	public Markov readMarkov() throws Exception {
		Document dom = getDom();
		Element root = dom.getRootElement();
		List stateList = root.selectNodes("//state"); // xpath语法
														// 获取到xml中所有的state标签
														// ，返回它们的集合

		List<Transition> transitions = new ArrayList<Transition>(); // 用来存储所有迁移的集合，存储完成赋值给markov相应变量
		List<State> states = new ArrayList<State>(); // 用来存储所有状态节点的集合，存储完成赋值给markov相应变量

		int i = 0; // 记录每个状态的编号
		for (Object stateNode : stateList) {

			List<Transition> outTransitions = new ArrayList<Transition>(); // 用来存储遍历到的每个状态节点的出迁移集合

			Element state = (Element) stateNode;
			Element name = state.element("name");

			State headState = new State(); // 每遍历到一个状态就将其封装成一个表头节点
			// 给表头结点相应属性赋值
			headState.setStateName(name.getText());
			if (name.getText().contains("extend")) {
				headState.setTime(true);
			}
			headState.setStateNum(i);
			headState.setStateAccessTimes(0);

			String stateLabel = state.attributeValue("label");
			if (stateLabel != null) {
				if (stateLabel.equals("final")) {
					headState.setLabel("final");
					markov.setFinalState(headState);
				} else if (stateLabel.equals("initial")) {
					headState.setLabel("initial");
					markov.setInitialState(headState);
				}
			}

			// 遍历当前状态的后续迁移(出迁移)集合
			List arcs = state.elements("arc");
			double outTransProb = 0.0;
			for (Object arcNode : arcs) {
				Element arc = (Element) arcNode;
				// Element arcName = arc.element("name");// 激励名称节点
				Element arcName = arc.element("owned");// 激励名称节点
				Element nextState = arc.element("to");
				Element probability = arc.element("prob");
				// 去掉所有概率为0的迁移！！！什么鬼 迁移概率为0还不如去掉！辣鸡时间模型！神魔恋！！
				if (probability.getText().trim().equals("0.0")) {
					continue;
				}
				Element assignValue = arc.element("assignValue");
				Element assignType = arc.element("assignType");
				Element conditions = arc.element("conditions");
				// 每遍历到一个出迁移，就创建一个迁移对象，并将从xml中读到的值赋值给其相应变量
				Transition t = new Transition();
				t.setTime(headState.isTime());
				t.setName(arcName.getText().trim());
				if (t.getName().equals("")) {
					throw new RuntimeException("状态" + headState.getStateName()
							+ "下的owned标签内容为空不符要求");
				}
				t.setProbability(Double.parseDouble(probability.getText()));
				// 打印每条迁移的概率
				System.out.println("状态" + headState.getStateName() + "的迁移"
						+ t.getName() + "的概率为：" + t.getProbability());
				outTransProb += t.getProbability();// 出迁移总概率

				t.setAccessTimes(0);
				t.setNextStateName(nextState.getText());
				if (assignValue != null) {
					t.setAssignValue(assignValue.getText());
				}
				if (assignType != null) {
					t.setAssignType(assignType.getText());
				}

				// 设置测试所需各种条件
				// String con = conditions.getTextTrim();
				// String replace1 = con.replaceAll("≥", "=");
				// String replace2 = replace1.replaceAll("≤", "=");
				t.setConditions(conditions.getTextTrim());

				// 封装transition上的激励stimulate，并赋值给transition上面的stimulate属性
				Stimulate stimulate = new Stimulate();
				stimulate.setTime(headState.isTime());
				stimulate.setName(arcName.getText().trim());
				if (assignValue != null) {
					stimulate.setAssignValue(assignValue.getText());
				}
				if (assignType != null) {
					stimulate.setAssignType(assignType.getText());
				}
				stimulate.setConditions(conditions.getTextTrim());
				// 先封装stimulate上面的参数集合属性
				List<Parameter> parameters = new ArrayList<Parameter>();// 存储每个迁移上的激励中的参数
				// 再封装stimulate上的参数间约束表达式集合
				List<String> constraints = new ArrayList<String>();
				Element stimulate_ele = arc.element("stimulate");
				if (stimulate_ele != null) {

					List paramList = stimulate_ele.elements("parameter");
					for (Object parameterNode : paramList) {

						// 封装每个参数实体
						Parameter parameter = new Parameter();
						Element parameterElement = (Element) parameterNode;

						System.out.println("当前读到的状态名："
								+ headState.getStateName()
								+ "当前读到的激励参数名："
								+ parameterElement.element("paramName")
										.getText());

						Element paramName = parameterElement
								.element("paramName");
						Element paramType = parameterElement
								.element("paramType");
						Element domain = parameterElement.element("domain");
						if (domain == null) {
							throw new RuntimeException("状态"
									+ headState.getStateName()
									+ "下的迁移："
									+ arc.element("name").getText().trim()
									+ "中的参数"
									+ parameterElement.element("paramName")
											.getText() + "缺少domain标签啊！");
						}
						parameter.setName(paramName.getText());
						parameter.setParamType(paramType.getText());
						parameter.setDomainType(domain.attributeValue("type"));
						parameter.setDomain(domain.getText());

						// 求解连续型随机变量的边界值并存入相应参数的集合属性
						if (domain.attributeValue("type").equals("serial")) {
							List<String> borderValue = new Border()
									.getBorder(domain.getText());
							parameter.setBorderValue(borderValue);
						}

						parameters.add(parameter);

					}

					List cons = stimulate_ele.elements("constraint");
					// List cons = arc.selectNodes("//constraint");
					for (Object constraintNode : cons) {
						Element constraint = (Element) constraintNode;
						constraints.add(constraint.getText());
					}

				}
				stimulate.setParameters(parameters);// 设置当前迁移上的激励中的参数集合属性

				stimulate.setConstraints(constraints);

				t.setStimulate(stimulate);// 给当前迁移里面的stimulate属性赋值

				outTransitions.add(t); // 将当前封装的出迁移存入出迁移集合
				transitions.add(t); // 将当前封装的迁移存入总迁移集合
			}
			// 打印出迁移概率和
			// System.out.println("状态节点" + headState.getStateName() +
			// "的出迁移概率和为："
			// + outTransProb);
			if (outTransProb < 0.9 && !headState.getStateName().equals("Exit")) {
				throw new RuntimeException("状态节点" + headState.getStateName()
						+ "的出迁移概率和为" + outTransProb + "小于1");
			} else if (outTransProb > 1.1) {
				throw new RuntimeException("状态节点" + headState.getStateName()
						+ "的出迁移概率和为" + outTransProb + "大于1");
			}
			headState.setOutTransitions(outTransitions); // 将出迁移集合赋值给当前的状态节点的出迁移集合对象
			// System.out.println("第"+i+"个头结点有"+outTransitions.size());
			// outTransitions.clear();不能用clear 已经赋值的状态节点的出迁移集合又会变成null
			states.add(headState); // 将当前的状态节点存入状态节点集合中
			++i;
		}

		markov.setStates(states); // 读取完xml之后，将状态节点集合赋值给markov链对象相应的状态节点集合
		markov.setNumberOfStates(markov.getStates().size()); // 给markov链对象的状态个数属性赋值
		// System.out.println("总共有"+markov.getStates().size()+"个状态节点"+markov.getStates().get(2).getOutTransitions().size());
		markov.setTransitions(transitions); // 读取完xml之后，将总迁移集合赋值给markov链对象相应的总迁移集合
		// System.out.println("总共有"+markov.getTransitions().size()+"个迁移");

		// 给每个状态节点的每个出迁移的nextState属性赋值：遍历整个markov链对象的所有状态节点的每个出迁移，
		// 通过每个出迁移的目标状态节点名称查询遍历整个状态节点集合，
		// 找到拥有相同名称的状态节点后，就将当前的状态节点赋值给当前出迁移的nextState属性。
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

		// 设置markov链的初始状态节点
		/*
		 * markov.getStates().get(0).setLabel("initial");
		 * markov.setInitialState(markov.getStates().get(0)); //
		 * 设置markov链的终止状态节点 markov.getStates().get(markov.getStates().size() -
		 * 1).setLabel("final"); markov.setFinalState(markov.getStates().get(
		 * markov.getStates().size() - 1));
		 */

		// System.out.println(markov.getTransitions().size());
		return markov; // 将构造成功的邻接表结构的markov链对象返回
	}
}
