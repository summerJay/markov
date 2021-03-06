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
import cn.edu.hdu.assign.ClassifyAssign;
import cn.edu.hdu.assign.CollectRoute;
import cn.edu.hdu.assign.CollectRoutePaper;
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
 * 项目的主函数所在类，包括获取filees文件对象和判断markov链对象是否还有未被遍历过的迁移。
 * 
 * @author YJ
 * @version 1.0
 * */

public class TheOne {

	/**
	 * 主函数:负责调用读取类中的读方法，产生测试用例类中的生成测试用例方法， 计算平稳分布类中的计算平稳分布方法，计算相似度类中的计算相似度的方法等。
	 * */
	public static void main(String[] args) throws Exception {

		// ReadMarkov rm = new ReadMarkov();
		ReadMarkov2 rm = new ReadMarkov2();
		Markov markov = rm.readMarkov();

		Scanner s = new Scanner(System.in);
		System.out.println("请选择测试用例生成模式：");
		System.out.println("			1.根据模型相似度随机生成");
		System.out.println("			2.自定义测试用例个数生成");
		int model = s.nextInt();
		if (model == 2) {
			// int min = getMinTCNum(markov, s);
			int min = getMinTCNumFromTwo(markov, s);
			System.out.println("请输入你想要生成的测试用例个数,并且不低于满足当前充分性指标的最低测试用例个数" + min
					+ "：");
			int N;
			while ((N = s.nextInt()) < 0) {
				System.out.println("当前输入的测试用例个数不满足要求，请重新输入：");
			}
			markov.setTcNumber(N);
		}

		s.close();

		// System.out.println(markov.getNumberOfStates());
		// Calculate.getAllTransValues(markov);

		// 创建一个document对象，用于存储测试用例
		Document dom = DocumentHelper.createDocument();
		Element root = dom.addElement("TCS");
		// 计算markov链的平稳分布
		double[] PI = CalculateDistribution.stationaryDistribution(markov);
		// 先初始化所有迁移个数为1，防止无法计算相似度
		/*
		 * for (State state : markov.getStates()) {
		 * 
		 * for (Transition t : state.getOutTransitions()) { t.setAccessTimes(1);
		 * 
		 * } }
		 */
		if (model == 2) {
			new CollectRoute().collect(markov);
			// new CollectRoutePaper().collect(markov);

			// 获取抽象测试序列
			showTestSequence(markov);
			// mathematica计算
			Calculate.getAllTransValues(markov);

			new BestAssign().assign(markov, root);
			// new ClassifyAssign().assign(markov, root);

			System.out.println("指标---可靠性测试用例数据库覆盖率:" + markov.getDbCoverage());
			markov.setDeviation(CalculateSimilarity.statistic(markov, PI));
			// markov.setDeviation(CalculateSimilarity.discriminant(markov,
			// PI));
			// markov.setDeviation(CalculateSimilarity.statistic_1(markov));
			System.out.println("逆向计算完相似度是否满足迁移覆盖：" + isSufficient(markov));
			System.out.println("指标---可靠性测试用例生成比率与使用模型实际使用概率平均偏差:"
					+ markov.getDeviation());
			System.out.println("利用变种平稳分布计算出的使用模型和测试模型的差异度："
					+ CalculateSimilarity.discriminant(markov, PI));
			System.out.println("利用欧氏距离计算出的使用模型和测试模型的差异度："
					+ CalculateSimilarity.statistic_1(markov));
			System.out.println("最大绕环次数为：" + (Constant.maxCircle - 1));
		} else if (model == 1) {
			// mathematica计算

			double similarity = 999991;
			boolean sufficiency = false;
			GenerateCases gc = new GenerateCases();
			boolean flag = true;
			int count = 0;

			do {
				int numberOfTestCases = gc.generate(markov, root);
				// System.out.println(numberOfTestCases);
				if (flag) {

					sufficiency = isSufficient(markov);
				}
				// 迁移或者状态覆盖百分百

				if (!sufficiency) {
					// ++count;
					// continue;
					// if (count <= 6) {
					//
					// continue;
					// }
					// similarity = CalculateSimilarity.statistic_1(markov);
					similarity = CalculateSimilarity.discriminant(markov, PI);
				} else {
					similarity = CalculateSimilarity.statistic(markov, PI);
					flag = false;
				}

				// flag = false;
				// similarity = CalculateSimilarity.statistic_1(markov);
				// similarity = CalculateSimilarity.statistic(markov, PI);
				// similarity = CalculateSimilarity.discriminant(markov, PI);
				System.out.println(similarity + "+++++++++++++++++++++++++");
				markov.setDeviation(similarity);
				markov.setActualNum(numberOfTestCases);
				// if (numberOfTestCases > 8000) {
				// break;
				// }

			} while (similarity > 0.1 /* || (similarity + "").equals("NaN") */);

			// System.out.println("激励个数：" + gc.testCasesExtend.size());
			// 生成方式1获取抽象测试序列
			// getAbstractTestSeqByModeOne(gc);
			// 实例化测试用例并存入数据库
			Calculate.getAllTransValues(markov);
			for (int i = 0; i < gc.testCasesExtend.size(); i++) {
				TCDetail.getInstance().setTestSequence(gc.abstractTS.get(i));
				String stimulateSequence = getStimulateSeq(gc.testCasesExtend
						.get(i));
				TCDetail.getInstance().setStimulateSequence(stimulateSequence);
				RandomCase.getCase(gc.testCasesExtend.get(i), root);
			}
			System.out.println("指标---可靠性测试用例数据库覆盖率:" + markov.getDbCoverage());
			System.out.println("指标---可靠性测试用例生成比率与使用模型实际使用概率平均偏差:"
					+ markov.getDeviation());
			System.out.println("\n利用平稳分布计算出的使用模型和测试模型的差异度:" + similarity);
			System.out.println("\n当前生成的测试用例和测试路径的个数:" + markov.getActualNum()
					+ "\n\n");
			System.out.println("顺向计算完相似度是否满足迁移覆盖：" + isSufficient(markov));

			// WriteTestCases.writeCases(gc.getTestCases());
			// 打印所有状态节点的平稳分布值
			for (double d : PI) {
				System.out.print(d + "  ");
			}
		} else if (model == 3) {
			// new CollectRoute().collect(markov);
			new CollectRoutePaper().collect(markov);

			// 获取抽象测试序列
			showTestSequence(markov);
			// mathematica计算
			Calculate.getAllTransValues(markov);

			// new BestAssign().assign(markov, root);
			new ClassifyAssign().assign(markov, root);

			System.out.println("指标---可靠性测试用例数据库覆盖率:" + markov.getDbCoverage());
			markov.setDeviation(CalculateSimilarity.statistic(markov, PI));
			// markov.setDeviation(CalculateSimilarity.discriminant(markov,
			// PI));
			// markov.setDeviation(CalculateSimilarity.statistic_1(markov));
			System.out.println("逆向计算完相似度是否满足迁移覆盖：" + isSufficient(markov));
			System.out.println("指标---可靠性测试用例生成比率与使用模型实际使用概率平均偏差:"
					+ markov.getDeviation());
			System.out.println("利用变种平稳分布计算出的使用模型和测试模型的差异度："
					+ CalculateSimilarity.discriminant(markov, PI));
			System.out.println("利用欧氏距离计算出的使用模型和测试模型的差异度："
					+ CalculateSimilarity.statistic_1(markov));
			System.out.println("最大绕环次数为：" + (Constant.maxCircle - 1));
		}

		// 将存储好测试用例的document对象写入XML文件
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

	// 获取所有的抽象测试序列mode1
	private static void getAbstractTestSeqByModeOne(GenerateCases gc) {
		for (String ts : gc.abstractTS) {
			System.out.println(ts);
		}

	}

	// 获取所有的抽象测试序列mode2
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
			if (r.getNumber() == 0) {
				// 显示抽象测试序列testSequence至列表
				System.out.println(testSequence);
			}
			for (int i = 0; i < r.getNumber(); i++) {

				// 显示抽象测试序列testSequence至列表
				System.out.println(testSequence);
			}

		}

	}

	private static int getMinTCNum(Markov markov, Scanner s) throws Exception {
		// 按照pc公式计算最小测试用例数目
		System.out.println("请输入软件可靠性指标p和置信度c：");
		System.out.print("p = ");
		double p = s.nextDouble();
		System.out.print("c = ");
		double c = s.nextDouble();
		int min_pc = (int) Math.ceil(Math.log10(1 - c) / Math.log10(1 - p));

		// 按照固定最小概率路径个数为一来计算最小测试用例数目
		new CollectRoute().collect(markov);
		double prob = 1;
		for (Route r : markov.getRouteList()) {
			if (r.getRouteProbability() < prob) {
				prob = r.getRouteProbability();
			}
		}
		int min_routePro = (int) Math.round(1.0 / prob);

		// 按照DO-178B MCDC准则计算最小测试用例数目(条件数+1)
		int min_mcdc = SearchConditions.findConditionNum() + 1;

		int temp = Math.max(min_pc, min_routePro);

		return Math.max(temp, min_mcdc);

	}

	// 从两个用例数中挑个大的即可 去除了上面方法中的按照固定最小概率路径个数为一来计算最小测试用例数目 防止用例数爆炸
	private static int getMinTCNumFromTwo(Markov markov, Scanner s)
			throws Exception {
		// 按照pc公式计算最小测试用例数目
		System.out.println("请输入软件可靠性指标p和置信度c：");
		System.out.print("p = ");
		double p = s.nextDouble();
		System.out.print("c = ");
		double c = s.nextDouble();
		int min_pc = (int) Math.ceil(Math.log10(1 - c) / Math.log10(1 - p));

		// 按照DO-178B MCDC准则计算最小测试用例数目(条件数+1)
		int min_mcdc = SearchConditions.findConditionNum() + 1;

		return Math.max(min_pc, min_mcdc);

	}

	/**
	 * 判断markov链中是否有访问次数为0的迁移
	 * 
	 * @param markov
	 *            接受一个指定的markov链对象
	 * @return 返回指定markov链对象是否满足此充分性的布尔值
	 * */
	private static boolean isSufficient(Markov markov) {

		for (State state : markov.getStates()) {

			for (Transition outTransition : state.getOutTransitions()) {

				if (outTransition.getAccessTimes() == 0) {
					// System.out.println("未访问的迁移：stateName:"
					// + state.getStateName() + "   outTransition:"
					// + outTransition.getName());
					return false;
				}
			}
		}
		return true;
	}

}
