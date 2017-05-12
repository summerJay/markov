package cn.edu.hdu.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchConditions {

	private static int count = 0;

	public static void main(String[] args) throws Exception {
		// File directory = new File(
		// "C:/Users/����/Desktop/ardupilot-master");
		File file = new File("C:/Users/����/Desktop/RC_Channel.cpp");
		// read(file);
		readFile(file);
		// File directory = new File("E:/Markov/�ɿ�Դ��/ardupilot-master");
		// search(directory);
		System.out.println(count);
	}

	private static void search(File directory) throws Exception {
		File[] childFiles = directory.listFiles();
		for (File file : childFiles) {
			if (file.isDirectory()) {
				if (!file.getName().equals("examples")) {// ���ɿ�Դ����examples�ļ��и����˵�
					// System.out.println(file.getName());
					search(file);
				}
			} else {

				String extension = file.getName().substring(
						file.getName().indexOf(".") + 1);
				if (extension.equals("cpp")) {
					// System.out.println(file.getName());
					System.out.println(file.getAbsolutePath());
					readFile(file);
				}
			}

		}
	}

	public static void readFile(File file) throws Exception {
		// File file = new File(
		// "C:/Users/����/Desktop/ardupilot-master/ardupilot-master/libraries/AP_ServoRelayEvents/AP_ServoRelayEvents.cpp");
		BufferedReader bufReader = new BufferedReader(new FileReader(file));
		String line = null;

		StringBuilder sb = new StringBuilder();
		while ((line = bufReader.readLine()) != null) {
			// �ڶ�ȡ�ļ���ʱ����ˡ�//"�͡�///��������ע��
			if (line.trim().contains("//")) {
				continue;
			}
			sb.append(line + "\n");

		}
		// System.out.println(sb.toString());
		String srcCode = sb.toString();

		Pattern leftpattern = Pattern.compile("/\\*");
		Matcher leftmatcher = leftpattern.matcher(srcCode);
		Pattern rightpattern = Pattern.compile("\\*/");
		Matcher rightmatcher = rightpattern.matcher(srcCode);
		/**
		 * begin ��������������ƥ����α��ʼֵΪ�ļ���ͷ
		 * **/
		String src = srcCode;
		int begin = 0;
		while (leftmatcher.find(begin)) {
			rightmatcher.find(leftmatcher.start());
			src = src.replace(
					srcCode.substring(leftmatcher.start(), rightmatcher.end()),
					"");
			/** Ϊ���ʱ��ʽ������ **/
			begin = rightmatcher.end();
		}
		// System.out.println(src);

		// ƥ�����е�if��while��for��䲢ͳ�����е�������
		Pattern leftpattern2 = Pattern.compile("(if|while|for) *\\(");
		Matcher leftmatcher2 = leftpattern2.matcher(src);
		Pattern rightpattern2 = Pattern.compile("\\) *\\{*;*\n");
		Matcher rightmatcher2 = rightpattern2.matcher(src);
		Pattern rightpattern3 = Pattern.compile("\\) *\\{");
		Matcher rightmatcher3 = rightpattern3.matcher(src);
		/**
		 * begin2 ��������������ƥ����α��ʼֵΪsrc��ͷ
		 * **/
		int begin2 = 0;
		while (leftmatcher2.find(begin2)) {
			String decision = null;
			int matcher2start = -1;
			int matcher3start = -1;
			if (rightmatcher2.find(leftmatcher2.start())) {
				matcher2start = rightmatcher2.start();

			}
			if (rightmatcher3.find(leftmatcher2.start())) {
				matcher3start = rightmatcher3.start();

			}
			if (matcher2start != -1) {
				if (matcher3start != -1) {
					if (matcher2start <= matcher3start) {
						decision = src.substring(leftmatcher2.end(),
								rightmatcher2.start());
						begin2 = rightmatcher2.end();
					} else {
						decision = src.substring(leftmatcher2.end(),
								rightmatcher3.start());
						begin2 = rightmatcher3.end();
					}
				} else {
					decision = src.substring(leftmatcher2.end(),
							rightmatcher2.start());
					begin2 = rightmatcher2.end();
				}
			} else {
				decision = src.substring(leftmatcher2.end(),
						rightmatcher3.start());
				begin2 = rightmatcher3.end();
			}

			System.out.println(decision);
			int length = decision.split("(\\&\\&|\\|\\|)").length;
			count += length;

		}

		// System.out.println(count);
		bufReader.close();
	}

	public static void read_last(File file) throws Exception {
		// File file = new File(
		// "C:/Users/����/Desktop/ardupilot-master/ardupilot-master/libraries/AP_ServoRelayEvents/AP_ServoRelayEvents.cpp");
		BufferedReader bufReader = new BufferedReader(new FileReader(file));
		String line = null;

		StringBuilder sb = new StringBuilder();
		while ((line = bufReader.readLine()) != null) {
			// �ڶ�ȡ�ļ���ʱ����ˡ�//"�͡�///��������ע��
			if (line.trim().contains("//")) {
				continue;
			}
			sb.append(line);

		}
		// System.out.println(sb.toString());
		String srcCode = sb.toString();

		// ���ˡ�/* */���͡�/** */������ע��

		/**
		 * ��/* ע�͵�����ƥ��
		 * 
		 * 
		 * ͨ����������ע�͵�����ƥ�䣬��Ϊ/*ע�����ǳɶԳ��� ��ƥ�䵽һ��/*ʱ�ܻ��ڽ������������л�����ƥ�䵽"*\\/",
		 * ����ڻ�ȡ��Ӧ��"*\\/"ע��ʱֻ��Ҫ�ӵ�ǰƥ���/*��ʼ���ɣ� ��һ��ƥ��ʱֻ��Ҫ����һ��ƥ��Ľ�β��ʼ����
		 * ���������ڴ��ı����Խ�ʡƥ��Ч�ʣ����� ����ǽ���ƥ�䷨
		 * 
		 * 
		 * */
		Pattern leftpattern = Pattern.compile("/\\*");
		Matcher leftmatcher = leftpattern.matcher(srcCode);
		Pattern rightpattern = Pattern.compile("\\*/");
		Matcher rightmatcher = rightpattern.matcher(srcCode);
		/**
		 * begin ��������������ƥ����α��ʼֵΪ�ļ���ͷ
		 * **/
		String src = srcCode;
		int begin = 0;
		while (leftmatcher.find(begin)) {
			rightmatcher.find(leftmatcher.start());
			src = src.replace(
					srcCode.substring(leftmatcher.start(), rightmatcher.end()),
					"");
			/** Ϊ���ʱ��ʽ������ **/
			begin = rightmatcher.end();
		}
		System.out.println(src);

		// ƥ�����е�if��while��for��䲢ͳ�����е�������
		Pattern leftpattern2 = Pattern.compile("(if|while|for) *\\(");
		Matcher leftmatcher2 = leftpattern2.matcher(src);
		Pattern rightpattern2 = Pattern.compile("\\) *\\{");
		Matcher rightmatcher2 = rightpattern2.matcher(src);
		Pattern rightpattern3 = Pattern.compile("[\\}\\{]");
		Matcher rightmatcher3 = rightpattern3.matcher(src);
		/**
		 * begin2 ��������������ƥ����α��ʼֵΪsrc��ͷ
		 * **/
		int begin2 = 0;
		while (leftmatcher2.find(begin2)) {
			String decision = null;
			if (rightmatcher2.find(leftmatcher2.start())) {
				decision = src.substring(leftmatcher2.end(),
						rightmatcher2.start());
				begin2 = rightmatcher2.end();
			} else {
				rightmatcher3.find(leftmatcher2.start());
				decision = src.substring(leftmatcher2.end(),
						rightmatcher3.start());
				begin2 = rightmatcher3.end();
			}
			System.out.println(decision);
			int length = decision.split("(\\&\\&|\\|\\|)").length;
			count += length;

		}

		System.out.println(count);

		// String annoRegex = "/\\*(^(/\\*))*\\*/";
		// String src = srcCode.replaceAll(annoRegex, "");
		// System.out.println(src);

		// Pattern pattern = Pattern.compile(annoRegex);

		// String regex = "(if|while|for) *\\((.+)\\)";
		// Pattern pattern = Pattern.compile(regex);
		// Matcher matcher = pattern.matcher(line);
		// while (matcher.find()) {
		// String decision = matcher.group(2);
		// // System.out.println(decision);
		// int length = decision.split("(\\&\\&|\\|\\|)").length;
		// count += length;
		// }
		// System.out.println(count);
		bufReader.close();
	}

	public static void read(File file) throws Exception {
		// File file = new File(
		// "C:/Users/����/Desktop/ardupilot-master/ardupilot-master/libraries/AP_ServoRelayEvents/AP_ServoRelayEvents.cpp");
		BufferedReader bufReader = new BufferedReader(new FileReader(file));
		String line = null;

		String regex = "(if|while|for) *\\((.+)\\)";
		Pattern pattern = Pattern.compile(regex);
		while ((line = bufReader.readLine()) != null) {
			Matcher matcher = pattern.matcher(line);
			while (matcher.find()) {
				String decision = matcher.group(2);
				System.out.println(decision);
				int length = decision.split("(\\&\\&|\\|\\|)").length;
				count += length;
			}
		}
		// System.out.println(count);
		bufReader.close();
	}

	public static void test() {
		// String str =
		// "yang jie if (a>b&&a<c)  summer jay while (a+b>c)  lemon tree for(int i=0;i<10;i++) over";
		// String regex = "(if|while|for) *\\(([^\\(\\)]+)\\)";
		String str = "yang jie if ((a>b)&&(a<c))  summer jay";
		String regex = "(if|while|for) *\\((.+)\\)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		while (matcher.find()) {
			String decision = matcher.group(2);
			System.out.println(decision);
			int length = decision.split("(\\&\\&|\\|\\|)").length;
		}
	}

}
