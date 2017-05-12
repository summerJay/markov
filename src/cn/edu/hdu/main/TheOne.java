package cn.edu.hdu.main;

import java.io.FileOutputStream;
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
import cn.edu.hdu.entity.Transition;
import cn.edu.hdu.random.Calculate;
import cn.edu.hdu.random.CalculateDistribution;
import cn.edu.hdu.random.CalculateSimilarity;
import cn.edu.hdu.random.GenerateCases;
import cn.edu.hdu.random.ReadMarkov2;
import cn.edu.hdu.utils.Constant;

/**
 * ��Ŀ�������������࣬������ȡfile�ļ�������ж�markov�������Ƿ���δ����������Ǩ�ơ�
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
			while ((N = s.nextInt()) < min) {
				System.out.println("��ǰ����Ĳ�����������������Ҫ�����������룺");
			}
			markov.setTcNumber(N);
		}

		s.close();

		// System.out.println(markov.getNumberOfStates());
		Calculate.getAllTransValues(markov);

		// ����һ��document�������ڴ洢��������
		Document dom = DocumentHelper.createDocument();
		Element root = dom.addElement("TCS");
		// ����markov����ƽ�ȷֲ�
		double[] PI = CalculateDistribution.stationaryDistribution(markov);

		if (model == 2) {
			new CollectRoute().collect(markov);
			new BestAssign().assign(markov, root);

			System.out.println("ָ��---�ɿ��Բ����������ݿ⸲����:" + markov.getDbCoverage());
			markov.setDeviation(CalculateSimilarity.statistic(markov, PI));
			System.out.println("ָ��---�ɿ��Բ����������ɱ�����ʹ��ģ��ʵ��ʹ�ø���ƽ��ƫ��:"
					+ markov.getDeviation());
			System.out.println("����ƽ�ȷֲ��������ʹ��ģ�ͺͲ���ģ�͵Ĳ���ȣ�"
					+ CalculateSimilarity.statistic(markov, PI));
			System.out.println("����ƻ�����Ϊ��" + (Constant.maxCircle - 1));
		} else if (model == 1) {

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

				if (!sufficiency) {
					continue;
				}
				flag = false;
				// similarity = CalculateSimilarity.statistic_1(markov);
				similarity = CalculateSimilarity.statistic(markov, PI);
				markov.setDeviation(similarity);
				System.out.println("ָ��---�ɿ��Բ����������ɱ�����ʹ��ģ��ʵ��ʹ�ø���ƽ��ƫ��:"
						+ markov.getDeviation());
				System.out.println("\n����ƽ�ȷֲ��������ʹ��ģ�ͺͲ���ģ�͵Ĳ����:" + similarity);
				System.out.println("\n��ǰ���ɵĲ��������Ͳ���·���ĸ���:" + numberOfTestCases
						+ "\n\n");

			} while (similarity > 0.001);

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
					return false;
				}
			}
		}
		return true;
	}

}
