package cn.edu.hdu.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

import cn.edu.hdu.entity.AbstractTestSeq;
import cn.edu.hdu.entity.TCDetail;

public class HibernateUtils {

	private static SessionFactory sf;
	static {
		// �����������ļ�, ������Session�Ĺ���
		sf = new Configuration().configure().buildSessionFactory();
		// sf = new Configuration().configure(new
		// File("hibernate.cfg.xml")).buildSessionFactory();
	}

	// ����Session����
	public static Session getSession() {
		return sf.openSession();
	}

	public static void saveTCDetail(TCDetail detail) {
		// ����

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

	public static void saveTCSeq(AbstractTestSeq ats) {
		// ����

		// ����session�Ĺ���������session����
		Session session = sf.openSession();
		// ��������
		Transaction tx = session.beginTransaction();
		// -----ִ�в���-----

		session.save(ats);

		// �ύ����/ �ر�
		tx.commit();
		session.close();
	}

	@Test
	public void save() throws Exception {
		// ����
		TCDetail detail = TCDetail.getInstance();
		detail.setStimulateSequence("stimulate");
		detail.setTestCase("testcase");
		detail.setTestSequence("testSequence");
		// ����session�Ĺ���������session����
		SessionFactory sf = new Configuration().configure()
				.buildSessionFactory();
		Session session = sf.openSession();
		// ��������
		Transaction tx = session.beginTransaction();
		// -----ִ�в���-----
		session.save(detail);

		// �ύ����/ �ر�
		tx.commit();
		session.close();
	}
}
