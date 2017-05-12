package cn.edu.hdu.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

import cn.edu.hdu.entity.TCDetail;

public class App2 {

	private static SessionFactory sf;
	static {
		/*
		 * //1. �������ù�������� Configuration config = new Configuration(); // ���������ļ�
		 * (Ĭ�ϼ���src/hibernate.cfg.xml) config.configure(); //2.
		 * ���ݼ��ص����ù�������󣬴���SessionFactory���� sf = config.buildSessionFactory();
		 */

		// ����sf����
		sf = new Configuration().configure().buildSessionFactory();
	}

	// 1. �������
	@Test
	public void testSave() throws Exception {
		// ����
		// Employee emp = new Employee();
		// emp.setEmpName("����123");
		// emp.setWorkDate(new Date());

		TCDetail detail = TCDetail.getInstance();
		detail.setStimulateSequence("stimulate");
		detail.setTestCase("testcase");
		detail.setTestSequence("testSequence");

		// ����session�Ĺ���������session����
		Session session = sf.openSession();
		// ��������
		Transaction tx = session.beginTransaction();
		// -----ִ�в���-----
		session.save(detail);

		// �ύ����/ �ر�
		tx.commit();
		session.close();
	}

	// ����
	@Test
	public void testUpdate() throws Exception {
		// ����
		// Employee emp = new Employee();
		// emp.setEmpId(1000000);
		// emp.setEmpName("����3");
		TCDetail detail = TCDetail.getInstance();
		detail.setStimulateSequence("stimulate");
		detail.setTestCase("testcase");
		detail.setTestSequence("testSequence");
		// ����session
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();

		// -------ִ�в���-------
		// û������������ִ�б��棻������������ִ�и��²���; ����������������ڱ���
		session.saveOrUpdate(detail);

		tx.commit();
		session.close();
	}
}
