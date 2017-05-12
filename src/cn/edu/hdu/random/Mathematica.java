package cn.edu.hdu.random;

import com.wolfram.jlink.KernelLink;
import com.wolfram.jlink.MathLinkException;
import com.wolfram.jlink.MathLinkFactory;

public class Mathematica {
	public static KernelLink ml = null;

	public static final String path = "-linkmode launch -linkname 'D:\\Program Files\\Wolfram Research\\Mathematica\\10.2\\MathKernel.exe'";

	public static String getSolution(String param1, String param2) {

		try {
			if (ml == null) {

				ml = MathLinkFactory.createKernelLink(path);
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

		} catch (Exception e) {
			System.out.println("MathLinkException occurred: " + e.getMessage());
		} finally {
			// ml.close();
		}
		return null;

	}

	public static String getSolution2(String param1, String param2) {

		// KernelLink ml = null;
		//
		// String path =
		// "-linkmode launch -linkname 'D:\\Program Files\\Wolfram Research\\Mathematica\\10.2\\MathKernel.exe'";
		try {
			if (ml == null) {

				ml = MathLinkFactory.createKernelLink(path);
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
			// // when it is launched.
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
			String strResult = ml.evaluateToOutputForm(
					"SetAccuracy[FindInstance[" + param1 + ", " + param2
							+ ", 50], 3]", 0);
			// System.out.println(strResult);
			return strResult;

		} catch (Exception e) {
			System.out.println("MathLinkException occurred: " + e.getMessage());
		} finally {
			// ml.close();
		}
		return null;
	}
}
