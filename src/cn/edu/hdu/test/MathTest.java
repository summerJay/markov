package cn.edu.hdu.test;

import java.util.Scanner;

public class MathTest {
	public static void main(String[] args) throws Exception {

		Scanner s = new Scanner(System.in);
		double r = s.nextDouble();
		double c = s.nextDouble();

		int num = (int) Math.ceil(Math.log10(1 - c) / Math.log10(r));
		System.out.println(num);
	}

}