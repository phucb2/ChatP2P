package protocol;

public class TestProtocol {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] param = new String[]{"bb@gmail.com", "111"};
		ProtocolMethod p = new ProtocolMethod("login", param, 100);
		System.out.println(p.toString());
		
		//ProtocolReturn r = ProtocolMethodExcutor.excute(p);
		//System.out.println(r.toString());
	}
}
