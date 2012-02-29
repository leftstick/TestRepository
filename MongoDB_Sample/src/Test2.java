import org.nanfeng.common.bean.UserInfo;
import org.nanfeng.common.db.mongo.connection.DBConnection;
import org.nanfeng.common.db.mongo.statement.Executor;
import org.nanfeng.common.exception.ConnectionException;

public class Test2 {
	public static void main(String[] args) {
		try {
			DBConnection conn = new DBConnection("test");
			Executor exe = new Executor(conn);
			insert(exe);
			conn.close();
		} catch (ConnectionException e) {
			e.printStackTrace();
		}
	}

	public static void insert(Executor exe) {
		UserInfo user = new UserInfo();
		user.setUser_id(4);
		user.setUser_name("dongfeng");
		try {
			System.out.println("�������Ϸ��������ȴ��洢");
			Thread.sleep(5000);
			System.out.println("�������Ϸ���������ʼ�洢");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		exe.save(user);
		System.out.println("�������Ϸ��������洢����");
	}
}
