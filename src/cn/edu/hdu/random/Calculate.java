package cn.edu.hdu.random;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.hdu.entity.Markov;
import cn.edu.hdu.entity.Parameter;
import cn.edu.hdu.entity.State;
import cn.edu.hdu.entity.Stimulate;
import cn.edu.hdu.entity.Transition;

public class Calculate {

	public static void getAllTransValues(Markov markov) {

		for (State state : markov.getStates()) {
			// System.out.println(state.getOutTransitions().size());
			for (Transition t : state.getOutTransitions()) {

				Stimulate stimulate = t.getStimulate();
				List<String> inequalities = new ArrayList<String>(); // �洢����Լ����������������������ͱ�����Լ��
				List<String> variables = new ArrayList<String>(); // �洢���������е�������ȡֵ��Χ�Ĳ�������
				// ������ǰ�����е����в������ռ���Ҫ�����Լ�������Ͳ���
				List<String> allBorder = new ArrayList<String>();// ��ŵ�ǰ���������в����ı߽�
				for (Parameter p : stimulate.getParameters()) {
					// �ռ�Լ������ʽ����inequalities
					if (/*
						 * (p.getParamType().trim().equals("int") ||
						 * p.getParamType().trim().equals("double") ||
						 * p.getParamType().trim().equals("float") ||
						 * p.getParamType().trim().equals("float*") || p
						 * .getParamType().trim().equals("unsigned char")) &&
						 */p.getDomainType().trim().equals("serial")) {
						inequalities.add(p.getDomain());
						for (String b : p.getBorderValue()) {
							allBorder.add(p.getName() + "==" + b);
						}
						variables.add(p.getName());
						// �����ǰ�����Ķ������������ͣ��Ҳ���������int�ͣ�����Լ�������ж��һ��������integersԼ���������Դ����õ��˲��������ν��ֵ
						if (p.getParamType().equals("int")) {
							inequalities.add(p.getName() + "��Integers");
						}
					} /*
					 * else if (p.getDomainType().equals("discrete")) {//
					 * ������ɢ�Ͷ�����ı������������ɢֵ��ѡһ��ֵ��ֵ����ǰ������value���� String[] values =
					 * p.getDomain().split(","); String value = values[new
					 * Random() .nextInt(values.length)]; p.setValue(value); }
					 */
				}
				// �ѵ�ǰ�����ϵĲ���������Լ������Ҳ����Լ����������
				for (String constraint : stimulate.getConstraints()) {
					inequalities.add(constraint);
				}

				Map<String, List<String>> map = null;

				// ���Լ�����ϲ�Ϊ�գ����Ȱ�Լ���������ϺͲ�������ƴ�ӳ�mathematica���߼�������Ҫ�Ĳ�����ʽ
				if (inequalities != null && inequalities.size() != 0) {
					boolean flag = true;
					for (String s : allBorder) {
						if (s.contains("==0")) {
							continue;
						}
						inequalities.add(s);
						// ƴ��Լ�������ɲ�����ʽ
						String param1 = inequalities.toString().substring(1,
								inequalities.toString().length() - 1);
						// ƴ�Ӳ��������ɲ�����ʽ
						String param2 = variables.toString().substring(1,
								variables.toString().length() - 1);
						System.out
								.println("�ӱ߽����㴦����䣺SetAccuracy[FindInstance[{"
										+ param1 + "}, {" + param2
										+ "}, 50], 3]");
						String solution = Mathematica2.getSolution2(param1,
								param2);// ͨ��mathematica���ߵĵ��ýӿڼ��㲻��ʽ�鲢���ؽ��
						if (!solution.equals("{}")) {
							map = parse(solution);
							flag = false;
							break;
						}
						inequalities.remove(s);
					}
					// �ӱ߽�ֵ�����޽��ִ��if
					if (flag) {
						// ƴ��Լ�������ɲ�����ʽ
						String param1 = inequalities.toString().substring(1,
								inequalities.toString().length() - 1);
						// ƴ�Ӳ��������ɲ�����ʽ
						String param2 = variables.toString().substring(1,
								variables.toString().length() - 1);
						System.out
								.println("���ӱ߽����㴦����䣺SetAccuracy[FindInstance[{"
										+ param1
										+ "}, {"
										+ param2
										+ "}, 50], 3]");
						String solution = Mathematica2.getSolution2(param1,
								param2);// ͨ��mathematica���ߵĵ��ýӿڼ��㲻��ʽ�鲢���ؽ��
						map = parse(solution);
					}
				}

				for (Parameter p : stimulate.getParameters()) {

					if (!p.getDomainType().equals("discrete")) {
						List<String> values = map.get(p.getName());
						if (p.getParamType().equals("int")) {
							List<String> list = new ArrayList<String>();
							for (String string : values) {
								list.add((int) Double.parseDouble(string.trim())
										+ "");
							}
							values = list;
						}
						p.setValues(values);
						// System.out.println();
					} else {// ��ɢ�ͱ���ֵ��ֵ
						String[] values = p.getDomain().split(",");
						List<String> list = new ArrayList<String>();
						for (String str : values) {
							list.add(str);
						}
						p.setValues(list);

					}
				}

			}
		}

	}

	private static Map<String, List<String>> parse(String solution) {
		System.out.println("solution---" + solution);
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		String[] results = solution.substring(2, solution.length() - 2).split(
				"\\}, \\{");
		for (String string : results) {
			if (string.contains(",")) {
				String[] strs = string.split(",");
				for (String str : strs) {
					String[] varAndVal = str.split("->");
					// System.out.println(varAndVal);
					if (map.containsKey(varAndVal[0].trim())) {
						map.get(varAndVal[0].trim()).add(varAndVal[1].trim());
					} else {
						List<String> list = new ArrayList<String>();
						list.add(varAndVal[1].trim());
						map.put(varAndVal[0].trim(), list);
					}
				}
			} else {
				String[] varAndVal = string.split("->");
				if (map.containsKey(varAndVal[0].trim())) {
					map.get(varAndVal[0].trim()).add(varAndVal[1].trim());
				} else {
					List<String> list = new ArrayList<String>();
					list.add(varAndVal[1].trim());
					map.put(varAndVal[0].trim(), list);
				}
			}
		}

		return map;
	}

}
