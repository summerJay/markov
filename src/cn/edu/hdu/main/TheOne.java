package cn.edu.hdu.main;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Scanner;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import cn.edu.hdu.assign.BestAssign;
import cn.edu.hdu.assign.CollectRoute;
import cn.edu.hdu.assign.SearchConditions;
import cn.edu.hdu.entity.Markov;
import cn.edu.hdu.entity.Route;
import cn.edu.hdu.entity.State;
import cn.edu.hdu.entity.Stimulate;
import cn.edu.hdu.entity.TCDetail;
import cn.edu.hdu.entity.Transition;
import cn.edu.hdu.random.Calculate;
import cn.edu.hdu.random.CalculateDistribution;
import cn.edu.hdu.random.CalculateSimilarity;
import cn.edu.hdu.random.GenerateCases;
import cn.edu.hdu.random.RandomCase;
import cn.edu.hdu.random.ReadMarkov2;
import cn.edu.hdu.utils.Constant;

/**
 * ��Ŀ�������������࣬������ȡfilees�ļ�������ж�markov�������Ƿ���δ����������Ǩ�ơ�
 * 
 * @author YJ
 * @version 1.0
 * */

public class TheOne {

	/**
	 * ������:������ö�ȡ���еĶ����������������������е����ɲ������������� ����ƽ�ȷֲ����еļ���ƽ�ȷֲ��������������ƶ����еļ������ƶȵķ����ȡ�
	 * */
	public static void main(String[] args) throws Exception {

		// ReadMarkov rm = new ReadMarkov();
		ReadMarkov2 rm = new ReadMarkov2();
		Markov markov = rm.readMarkov();

		Scanner s = new Scanner(System.in);
		System.out.println("��ѡ�������������ģʽ��");
		System.out.println("			1.����ģ�����ƶ��������");
		System.out.println("			2.�Զ������������������");
		int model = s.nextInt();
		if (model == 2) {
			int min = getMinTCNum(markov, s);
			System.out.println("����������Ҫ���ɵĲ�����������,���Ҳ��������㵱ǰ�����ָ�����Ͳ�����������" + min
					+ "��");
			int N;
			while ((N = s.nextInt()) < 0) {
				System.out.println("��ǰ����Ĳ�����������������Ҫ�����������룺");
			}
			markov.setTcNumber(N);
		}

		s.close();

		// System.out.println(markov.getNumberOfStates());
		// Calculate.getAllTransValues(markov);

		// ����һ��document�������ڴ洢��������
		Document dom = DocumentHelper.createDocument();
		Element root = dom.addElement("TCS");
		// ����markov����ƽ�ȷֲ�
		double[] PI = CalculateDistribution.stationaryDistribution(markov);

		if (model == 2) {
			new CollectRoute().collect(markov);

			// ��ȡ�����������
			// showTestSequence(markov);
			// mathematica����
			Calculate.getAllTransValues(markov);

			new BestAssign().assign(markov, root);

			System.out.println("ָ��---�ɿ��Բ����������ݿ⸲����:" + markov.getDbCoverage());
			markov.setDeviation(CalculateSimilarity.statistic(markov, PI));
			System.out.println("ָ��---�ɿ��Բ����������ɱ�����ʹ��ģ��ʵ��ʹ�ø���ƽ��ƫ��:"
					+ markov.getDeviation());
			System.out.println("����ƽ�ȷֲ��������ʹ��ģ�ͺͲ���ģ�͵Ĳ���ȣ�"
					+ CalculateSimilarity.statistic(markov, PI));
			System.out.println("����ƻ�����Ϊ��" + (Constant.maxCircle - 1));
		} else if (model == 1) {
			// mathematica����

			double similarity = 999991;
			boolean sufficiency = false;
			GenerateCases gc = new GenerateCases();
			boolean flag = true;

			do {
				int numberOfTestCases = gc.generate(markov, root);
				// System.out.println(numberOfTestCases);
				if (flag) {

					sufficiency = isSufficient(markov);
				}
				// Ǩ�ƻ���״̬���ǰٷְ�

				if (!sufficiency) {
					continue;
				}

				flag = false;
				// similarity = CalculateSimilarity.statistic_1(markov);
				similarity = CalculateSimilarity.statistic(markov, PI);
				markov.setDeviation(similarity);
				markov.setActualNum(numberOfTestCases);

			} while (similarity > 0.1);

			// System.out.println("����������" + gc.testCasesExtend.size());
			// ���ɷ�ʽ1��ȡ�����������
			getAbstractTestSeqByModeOne(gc);
			// ʵ���������������������ݿ�
			Calculate.getAllTransValues(markov);
			for (int i = 0; i < gc.testCasesExtend.size(); i++) {
				TCDetail.getInstance().setTestSequence(gc.abstractTS.get(i));
				String stimulateSequence = getStimulateSeq(gc.testCasesExtend
						.get(i));
				TCDetail.getInstance().setStimulateSequence(stimulateSequence);
				RandomCase.getCase(gc.testCasesExtend.get(i), root);
			}
			System.out.println("ָ��---�ɿ��Բ����������ɱ�����ʹ��ģ��ʵ��ʹ�ø���ƽ��ƫ��:"
					+ markov.getDeviation());
			System.out.println("\n����ƽ�ȷֲ��������ʹ��ģ�ͺͲ���ģ�͵Ĳ����:" + similarity);
			System.out.println("\n��ǰ���ɵĲ��������Ͳ���·���ĸ���:" + markov.getActualNum()
					+ "\n\n");
			// WriteTestCases.writeCases(gc.getTestCases());
			// ��ӡ����״̬�ڵ��ƽ�ȷֲ�ֵ
			for (double d : PI) {
				System.out.print(d + "  ");
			}
		}

		// ���洢�ò���������document����д��XML�ļ�
		OutputFormat format = OutputFormat.createPrettyPrint();
		XMLWriter writer = new XMLWriter(new FileOutputStream(
				"E:/Markov/tcs.xml"), format);
		writer.write(dom);
		writer.close();

	}

	private static String getStimulateSeq(List<Stimulate> oneCaseExtend) {
		String stimulateSequence = "";
		for (int i = 0; i < oneCaseExtend.size(); i++) {
			if (i != oneCaseExtend.size() - 1) {
				stimulateSequence = stimulateSequence
						+ oneCaseExtend.get(i).toString() + "-->>";
				// System.out.print(oneCaseExtend.get(i).toString() + "-->>");
			} else {
				stimulateSequence = stimulateSequence
						+ oneCaseExtend.get(i).toString();
				// System.out.println(oneCaseExtend.get(i).toString());
			}
		}
		return stimulateSequence;
	}

	// ��ȡ���еĳ����������mode1
	private static void getAbstractTestSeqByModeOne(GenerateCases gc) {
		for (String ts : gc.abstractTS) {
			System.out.println(ts);
		}

	}

	// ��ȡ���еĳ����������mode2
	private static void showTestSequence(Markov markov) {
		for (Route r : markov.getRouteList()) {

			String testSequence = "";
			for (int i = 0; i < r.getTransitionList().size(); i++) {
				if (i != r.getTransitionList().size() - 1) {
					testSequence = testSequence
							+ r.getTransitionList().get(i).getName() + "-->>";
					// System.out.print(oneCaseExtend.get(i).toString() +
					// "-->>");
				} else {
					testSequence = testSequence
							+ r.getTransitionList().get(i).getName();
					// System.out.println(oneCaseExtend.get(i).toString());
				}
			}
			r.setTcSequence(testSequence);
			for (int i = 0; i < r.getNumber(); i++) {

				// ��ʾ�����������testSequence���б�
				System.out.println(testSequence);
			}

		}

	}

	private static int getMinTCNum(Markov markov, Scanner s) throws Exception {
		// ����pc��ʽ������С����������Ŀ
		System.out.println("����������ɿ���ָ��p�����Ŷ�c��");
		System.out.print("p = ");
		double p = s.nextDouble();
		System.out.print("c = ");
		double c = s.nextDouble();
		int min_pc = (int) Math.ceil(Math.log10(1 - c) / Math.log10(1 - p));

		// ���չ̶���С����·������Ϊһ��������С����������Ŀ
		new CollectRoute().collect(markov);
		double prob = 1;
		for (Route r : markov.getRouteList()) {
			if (r.getRouteProbability() < prob) {
				prob = r.getRouteProbability();
			}
		}
		int min_routePro = (int) Math.round(1.0 / prob);

		// ����DO-178B MCDC׼�������С����������Ŀ(������+1)
		int min_mcdc = SearchConditions.findConditionNum() + 1;

		int temp = Math.max(min_pc, min_routePro);

		return Math.max(temp, min_mcdc);

	}

	/**
	 * �ж�markov�����Ƿ��з��ʴ���Ϊ0��Ǩ��
	 * 
	 * @param markov
	 *            ����һ��ָ����markov������
	 * @return ����ָ��markov�������Ƿ�����˳���ԵĲ���ֵ
	 * */
	private static boolean isSufficient(Markov markov) {

		for (State state : markov.getStates()) {

			for (Transition outTransition : state.getOutTransitions()) {

				if (outTransition.getAccessTimes() == 0) {
					System.out.println("δ���ʵ�Ǩ�ƣ�stateName:"
							+ state.getStateName() + "   outTransition:"
							+ outTransition.getName());
					return false;
				}
			}
		}
		return true;
	}

}
