
public class TryCatchBlock {

	public void function() {
		
		try {
			System.out.println("try");
			int a = 1 / 0;
		}
		catch (RuntimeException e) {
			System.out.println("catch");
		}
		
	}
	
}
