package cn.edu.hdu.random;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.edu.hdu.entity.Parameter;
import cn.edu.hdu.entity.Stimulate;

/**
 * ����ר��������⼤��������ÿ��������Լ�������õ�ÿ�������ĺϷ�ֵ �Դ������ɲ�������
 * 
 * @author YJ
 * @version 1.0
 */

public class Evaluation {

	public static void getValue(List<Stimulate> oneCaseExtend) {

		// �������������ϵ�ÿ������
		for (int i = 0; i < oneCaseExtend.size(); i++) {
			List<String> inequalities = new ArrayList<String>(); // �洢����Լ����������������������ͱ�����Լ��
			List<String> variables = new ArrayList<String>(); // �洢���������е�������ȡֵ��Χ�Ĳ�������
			// ������ǰ�����е����в������ռ���Ҫ�����Լ�������Ͳ���
			for (Parameter p : oneCaseExtend.get(i).getParameters()) {
				// �ռ�Լ������ʽ����inequalities
				if ((p.getParamType().equals("int") || p.getParamType().equals(
						"double"))
						&& p.getDomainType().equals("serial")) {
					inequalities.add(p.getDomain());
					variables.add(p.getName());
					// �����ǰ�����Ķ������������ͣ��Ҳ���������int�ͣ�����Լ�������ж��һ��������integersԼ���������Դ����õ��˲��������ν��ֵ
					if (p.getParamType().equals("int")) {
						inequalities.add(p.getName() + "��Integers");
					}
				} else if (p.getDomainType().equals("discrete")) {// ������ɢ�Ͷ�����ı������������ɢֵ��ѡһ��ֵ��ֵ����ǰ������value����
					String[] values = p.getDomain().split(",");
					String value = values[new Random().nextInt(values.length)];
					p.setValue(value);
				}
			}
			// �ѵ�ǰ�����ϵĲ���������Լ������Ҳ����Լ����������
			for (String constraint : oneCaseExtend.get(i).getConstraints()) {
				inequalities.add(constraint);
			}
			// ���Լ�����ϲ�Ϊ�գ����Ȱ�Լ���������ϺͲ�������ƴ�ӳ�mathematica���߼�������Ҫ�Ĳ�����ʽ
			if (inequalities != null && inequalities.size() != 0) {
				// ƴ��Լ�������ɲ�����ʽ
				String param1 = "{"
						+ inequalities.toString().substring(1,
								inequalities.toString().length() - 1) + "}";
				// ƴ�Ӳ��������ɲ�����ʽ
				String param2 = "{"
						+ variables.toString().substring(1,
								variables.toString().length() - 1) + "}";
				String solution = Mathematica.getSolution(param1, param2);// ͨ��mathematica���ߵĵ��ýӿڼ��㲻��ʽ�鲢���ؽ��
				// ��ȥ�����Ű����ŷָ��ַ������͵Ľ���õ� ����->ֵ ��ʽ�������α�������->�ָ��ַ����͵õ��� ������ֵ �ַ�������
				// ���������ȥ�Աȱ�����ǰ�����ϵĲ������ϣ������������ͬ����� ֵ
				// ������Ӧ������value���ԣ�������ÿ�������㶼�����Լ���ֵ
				String[] results = solution.substring(1, solution.length() - 1)
						.split(",");
				for (String string : results) {
					String[] varAndVal = string.split("->");
					for (Parameter p : oneCaseExtend.get(i).getParameters()) {
						if (p.getName().equals(varAndVal[0].trim())) {
							if (p.getParamType().equals("int")) {// ����˲�����int�ͣ���ֵ�ȸ�ʽ��Ϊintֵ��ʽ�ٸ�ֵ���˲���value����
								p.setValue((int) Double
										.parseDouble(varAndVal[1].trim()) + "");
							} else {

								p.setValue(varAndVal[1].trim());
							}
							break;
						}
					}
				}
			}
			String stimul = oneCaseExtend.get(i).getName();
			String params = "";
			// ������ǰ�����Ĳ������ϣ��Ѳ������Ͳ���ֵƴ�ӳ� ��������=����ֵ��������=����ֵ������ʽ
			for (int i1 = 0; i1 < oneCaseExtend.get(i).getParameters().size(); i1++) {

				if (i1 != oneCaseExtend.get(i).getParameters().size() - 1) {
					params = params
							+ oneCaseExtend.get(i).getParameters().get(i1)
									.getName()
							+ "="
							+ oneCaseExtend.get(i).getParameters().get(i1)
									.getValue() + ",";
				} else {
					params = "["
							+ params
							+ oneCaseExtend.get(i).getParameters().get(i1)
									.getName()
							+ "="
							+ oneCaseExtend.get(i).getParameters().get(i1)
									.getValue() + "]";
				}
			}

			// ���������ӡʵ������ĵ�ǰ����
			if (i != oneCaseExtend.size() - 1) {

				System.out
						.print(oneCaseExtend.get(i).getName() + params + "����");
			} else {
				System.out.println(oneCaseExtend.get(i).getName() + params);

			}

		}

	}
}
