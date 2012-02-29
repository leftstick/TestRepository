import java.util.ArrayList;
import java.util.List;

import org.nanfeng.common.bean.UserInfo;
import org.nanfeng.common.db.mongo.connection.DBConnection;
import org.nanfeng.common.db.mongo.query.JQLFilter;
import org.nanfeng.common.db.mongo.query.JQLFilters;
import org.nanfeng.common.db.mongo.statement.Executor;
import org.nanfeng.common.exception.ConnectionException;

public class Test {
	public static void main(String[] args) {
		try {
			DBConnection conn = new DBConnection("test");
			Executor exe = new Executor(conn);
			find3(exe);
			conn.close();
		} catch (ConnectionException e) {
			e.printStackTrace();
		}
	}

	public static void insert(Executor exe) {
		UserInfo user = new UserInfo();
		user.setUser_id(3);
		user.setUser_name("xifeng");
		exe.save(user);
	}

	public static void find(Executor exe) {
		List<Object> in = new ArrayList<Object>();
		in.add(1);
		in.add(3);
		JQLFilter filter = JQLFilters.in("user_id", in);
		List<UserInfo> list = exe.find(UserInfo.class, filter);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
	}

	public static void count(Executor exe) {
		JQLFilter filter = JQLFilters.like("user_name", "nanssf", JQLFilters.BOTH);
		System.out.println(exe.count(UserInfo.class, filter));
	}

	public static void find2(Executor exe) {
		JQLFilter filter = JQLFilters.like("user_name", "f", JQLFilters.BOTH);
		List<UserInfo> list = exe.find(UserInfo.class, filter);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
	}
	public static void find3(Executor exe) {
		JQLFilter filter = JQLFilters.like("user_name", "f", JQLFilters.BOTH);
		List<UserInfo> list = exe.find(UserInfo.class, filter,2,2);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
	}
}
