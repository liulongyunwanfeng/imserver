public class TestRedis {

	public static void main(String[] args) {
		for (int i=0;i<5;i++){
			Thread th=new ThreadTest();
			th.start();
		}
	}

}
