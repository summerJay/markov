package cn.edu.hdu.random;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.wolfram.jlink.KernelLink;
import com.wolfram.jlink.MathLinkException;
import com.wolfram.jlink.MathLinkFactory;

public class Mathematica3 {
	private static String parameter1;
	private static String parameter2;
	private static String result;

	private static Map<String, String> varToVoc;

	public static String getSolution(String param1, String param2) {

		KernelLink ml = null;

		String path = "-linkmode launch -linkname 'D:\\Program Files\\Wolfram Research\\Mathematica\\10.2\\MathKernel.exe'";
		try {

			ml = MathLinkFactory.createKernelLink(path);
		} catch (MathLinkException e) {
			System.out.println("Fatal error opening link: " + e.getMessage());
			return null;
		}

		try {
			// Get rid of the initial InputNamePacket the kernel will send
			// when it is launched.
			ml.discardAnswer();

			ml.evaluate("<<MyPackage.m");
			ml.discardAnswer();

			ml.putFunction("FindInstance", 3);

			ml.endPacket();
			ml.waitForAnswer();
			String result1 = ml.getString();

			// If you want the result back as a string, use evaluateToInputForm
			// or evaluateToOutputForm. The second arg for either is the
			// requested page width for formatting the string. Pass 0 for
			// PageWidth->Infinity. These methods get the result in one
			// step--no need to call waitForAnswer.
			String strResult = ml.evaluateToOutputForm(
					"A = SetAccuracy[FindInstance[" + param1 + ", " + param2
							+ ", 50], 3]; A[[RandomInteger[{1, Length[A]}]]]",
					0);
			// System.out.println(strResult);
			return strResult;

		} catch (MathLinkException e) {
			System.out.println("MathLinkException occurred: " + e.getMessage());
		} finally {
			ml.close();
		}
		return null;

	}

	public static String getSolution2(String param1, String param2) {

		KernelLink ml = null;

		String path = "-linkmode launch -linkname 'D:\\Program Files\\Wolfram Research\\Mathematica\\10.2\\MathKernel.exe'";
		try {

			ml = MathLinkFactory.createKernelLink(path);
		} catch (MathLinkException e) {
			System.out.println("Fatal error opening link: " + e.getMessage());
			return null;
		}

		try {
			// Get rid of the initial InputNamePacket the kernel will send
			// when it is launched.
			ml.discardAnswer();

			ml.evaluate("<<MyPackage.m");
			ml.discardAnswer();

			ml.putFunction("FindInstance", 3);

			ml.endPacket();
			ml.waitForAnswer();
			String result1 = ml.getString();

			// If you want the result back as a string, use evaluateToInputForm
			// or evaluateToOutputForm. The second arg for either is the
			// requested page width for formatting the string. Pass 0 for
			// PageWidth->Infinity. These methods get the result in one
			// step--no need to call waitForAnswer.

			// ��param1��param2�в�����ʶ����˳��ȫ���滻Ϊ26����ĸ,��ֹ��������������mathematica�������޷�����
			replace(param1, param2);
			System.out.println(parameter1 + "======" + parameter2);
			String strResult = ml.evaluateToOutputForm(
					"SetAccuracy[FindInstance[{" + parameter1 + "}, {"
							+ parameter2 + "}, 50], 3]", 0);
			// System.out.println(strResult);
			// �ٽ��滻�Ĳ�����ʶ���滻��ԭ��
			recovery(strResult);
			System.out.println(result);
			return result;

		} catch (MathLinkException e) {
			System.out.println("MathLinkException occurred: " + e.getMessage());
		} finally {
			ml.close();
		}
		return null;
	}

	/**
	 * ���������еı�����ԭΪ�����Ĳ�����ʶ��
	 * 
	 * @param strResult
	 */
	private static void recovery(String strResult) {
		Set<String> keySet = varToVoc.keySet();
		for (String key : keySet) {
			strResult = strResult.replace(varToVoc.get(key) + " ", key.trim());
		}
		result = strResult;
	}

	/**
	 * ��������ʶ������ĸ���滻
	 * 
	 * @param param1
	 * @param param2
	 */
	private static void replace(String param1, String param2) {
		String[] vars = param2.split(",");
		varToVoc = new HashMap<String, String>();
		char vocabulary = 'a';
		// �滻������
		for (int i = 0; i < vars.length; i++) {
			varToVoc.put(vars[i].trim(), vocabulary + "");
			vars[i] = vocabulary + "";
			vocabulary = (char) (vocabulary + 1);
		}
		// �滻����ʽ��
		String[] inequals = param1.split(",");
		for (int i = 0; i < inequals.length; i++) {
			String inequ = inequals[i].trim();
			List<String> names = filter(inequ);
			for (String name : names) {

				String value = varToVoc.get(name);
				inequals[i] = inequals[i].replace(name, value);
			}
		}

		parameter1 = compose(inequals);
		parameter2 = compose(vars);
		// System.out.println(param1 + "---" + param2);

	}

	private static String compose(String[] vars) {
		String str = "";
		for (int i = 0; i < vars.length; i++) {
			if (i < vars.length - 1) {
				str = str + vars[i] + ",";
			} else {
				str = str + vars[i];
			}
		}
		return str;
	}

	private static List<String> filter(String inequ) {
		List<String> names = new ArrayList<String>();
		Set<String> keySet = varToVoc.keySet();
		String[] split = inequ.split("[<>=]=?");
		for (String string : split) {

			for (String key : keySet) {
				if (string.trim().equals(key.trim())) {
					names.add(string.trim());
				}
			}
		}
		return names;
	}

}
