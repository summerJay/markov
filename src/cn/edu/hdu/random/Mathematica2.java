package cn.edu.hdu.random;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.wolfram.jlink.KernelLink;
import com.wolfram.jlink.MathLinkException;
import com.wolfram.jlink.MathLinkFactory;

public class Mathematica2 {
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

	public static KernelLink ml;
	public static final String PATH = "-linkmode launch -linkname 'D:\\Program Files\\Wolfram Research\\Mathematica\\10.2\\MathKernel.exe'";

	public static String getSolution2(String param1, String param2) {

		// KernelLink ml = null;

		// String path =
		// "-linkmode launch -linkname 'D:\\Program Files\\Wolfram Research\\Mathematica\\10.2\\MathKernel.exe'";
		try {
			// ml = MathLinkFactory.createKernelLink(PATH);
			if (ml == null) {

				ml = MathLinkFactory.createKernelLink(PATH);
				ml.discardAnswer();

				ml.evaluate("<<MyPackage.m");
				ml.discardAnswer();

				ml.putFunction("FindInstance", 3);

				ml.endPacket();
				ml.waitForAnswer();
				String result1 = ml.getString();
			}
		} catch (MathLinkException e) {
			System.out.println("Fatal error opening link: " + e.getMessage());
			return null;
		}

		try {
			// Get rid of the initial InputNamePacket the kernel will send
			// when it is launched.
			// ml.discardAnswer();
			//
			// ml.evaluate("<<MyPackage.m");
			// ml.discardAnswer();
			//
			// ml.putFunction("FindInstance", 3);
			//
			// ml.endPacket();
			// ml.waitForAnswer();
			// String result1 = ml.getString();

			// If you want the result back as a string, use evaluateToInputForm
			// or evaluateToOutputForm. The second arg for either is the
			// requested page width for formatting the string. Pass 0 for
			// PageWidth->Infinity. These methods get the result in one
			// step--no need to call waitForAnswer.

			// ��param1��param2�в�����ʶ����˳��ȫ���滻Ϊ26����ĸ,��ֹ��������������mathematica�������޷�����
			System.out.println("����ʽ��Ͳ������滻ǰ��" + param1 + "======" + param2);
			replace(param1, param2);
			System.out.println("����ʽ��Ͳ������滻��" + parameter1 + "======"
					+ parameter2);
			String strResult = ml.evaluateToOutputForm(
					"SetAccuracy[FindInstance[{" + parameter1 + "}, {"
							+ parameter2 + "}, 50], 3]", 0);
			// System.out.println(strResult);
			// �ٽ��滻�Ĳ�����ʶ���滻��ԭ��
			System.out.println("��������ԭǰ��������" + strResult);
			recovery(strResult);
			System.out.println("��������ԭ���������" + result);
			return result;

		} catch (Exception e) {
			System.out.println("MathLinkException occurred: " + e.getMessage());
		} finally {
			// ml.close();
			// ml = null;
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
		for (String var : vars) {
			param2 = param2.replace(var.trim(), vocabulary + "" + vocabulary
					+ vocabulary);
			param1 = param1.replace(var.trim(), vocabulary + "" + vocabulary
					+ vocabulary);
			varToVoc.put(var.trim(), vocabulary + "" + vocabulary + vocabulary);
			vocabulary = (char) (vocabulary + 1);

		}

		parameter1 = param1;
		parameter2 = param2;
		// System.out.println(param1 + "---" + param2);

	}

}
