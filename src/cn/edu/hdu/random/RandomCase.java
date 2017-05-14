package cn.edu.hdu.random;

import java.util.List;
import java.util.Random;

import org.dom4j.Element;

import cn.edu.hdu.entity.Parameter;
import cn.edu.hdu.entity.Stimulate;
import cn.edu.hdu.entity.TCDetail;
import cn.edu.hdu.utils.HibernateUtils;

public class RandomCase {

	public static void getCase(List<Stimulate> oneCaseExtend, Element root) {

		Element tc = root.addElement("testcase");

		String testCase = "";
		for (int i = 0; i < oneCaseExtend.size(); i++) {
			Stimulate stimulate = oneCaseExtend.get(i);
			// ���ɲ�������ʱ��Ҫȥ�����������еĿ�Ǩ��null
			if ("null".equals(stimulate.getName())) {
				continue;
			}

			int random = -1;
			String str = "";
			for (int j = 0; j < stimulate.getParameters().size(); j++) {

				Parameter parameter = stimulate.getParameters().get(j);
				String value = null;
				if (parameter.getDomainType().equals("serial")) {
					List<String> values = parameter.getValues();
					if (random == -1) {

						// System.out.println("����" + stimulate.getName() + "�Ĳ���"
						// + parameter.getName() + "��Ӧ��ֵ��Ϊ:" + values);

						random = new Random().nextInt(values.size());
					}
					value = values.get(random);
				} else {

					value = parameter.getValues().get(
							new Random().nextInt(parameter.getValues().size()));
				}

				if (j != stimulate.getParameters().size() - 1) {
					str = str + parameter.getName() + "=" + value + ",";
				} else {
					str = "[" + str + parameter.getName() + "=" + value + "]";
				}
			}

			if (i != oneCaseExtend.size() - 1) {
				testCase += stimulate.getName() + str + "[return "
						+ stimulate.getAssignType() + ":"
						+ stimulate.getAssignValue() + "]" + "����";

				Element process = tc.addElement("process");
				Stimulate nextStimulate = oneCaseExtend.get(i + 1);
				process.addElement("conditions").setText(
						nextStimulate.getConditions());
				process.addElement("operation").setText(stimulate.getName());
				if (str.equals("")) { // û��������������
				// process.addElement("input").setText("null");
					process.addElement("input").setText(
							nextStimulate.getConditions());
				} else {
					String inputContent = str.substring(1, str.length() - 1);
					if (!nextStimulate.getConditions().equals("")) {
						inputContent += ("," + nextStimulate.getConditions());
					}
					process.addElement("input").setText(inputContent);
				}
				if (stimulate.getAssignType() != null) {
					process.addElement("assignType").setText(
							stimulate.getAssignType());
					process.addElement("assignValue").setText(
							stimulate.getAssignValue());
				}

			} else {
				testCase += stimulate.getName() + str + "[return "
						+ stimulate.getAssignType() + ":"
						+ stimulate.getAssignValue() + "]";

				Element process = tc.addElement("process");
				// process.addElement("conditions").setText(
				// stimulate.getConditions());
				process.addElement("operation").setText(stimulate.getName());
				if (str.equals("")) {
					process.addElement("input").setText("null");
				} else {
					process.addElement("input").setText(
							str.substring(1, str.length() - 1));
				}
				if (stimulate.getAssignType() != null) {
					process.addElement("assignType").setText(
							stimulate.getAssignType());
					process.addElement("assignValue").setText(
							stimulate.getAssignValue());
				}

			}
		}
		if (testCase.endsWith("����")) {
			testCase = testCase.substring(0, testCase.length() - 2);
		}
		System.out.println(testCase);
		TCDetail.getInstance().setTestCase(testCase);
		// �˴�����mysql���������ΪTCDetail.getinstance
		HibernateUtils.saveTCDetail(TCDetail.getInstance());
	}
}