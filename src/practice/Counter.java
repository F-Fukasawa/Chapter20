package practice;

public class Counter {
	private long count = 0;
	
	public void add(long i) {
		synchronized(this) {
			System.out.println("足し算します treadName = " + Thread.currentThread().getName());
			count += i;
		}
	}
	
	public void mul(long i) {
		synchronized(this) {
		long c = count;
			System.out.println("掛け算をします threadName = " + Thread.currentThread().getName());
			count *= i;
		}
	}
}
