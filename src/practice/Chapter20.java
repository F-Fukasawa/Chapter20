package practice;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Chapter20 {

	public static void main(String[] args) {
		Counter counter = new Counter();
		// Taskの配列を5つ生成
		Task[] tasks = {
				new Task(counter), 
				new Task(counter), 
				new Task(counter), 
				new Task(counter),
				new Task(counter), };
		// Task数分、Futureを配列で用意
		Future<?>[] futures = new Future<?>[tasks.length];
		// 5スレッドで処理をするExecutorServiceを生成
		ExecutorService pool = Executors.newFixedThreadPool(tasks.length);
		try {
			// Task数分スレッドを実行
			for (int i = 0; i < tasks.length; i++) {
				futures[i] = pool.submit(tasks[i]);
			}
			// Task数分実行結果を待つ
			for (Future<?> future : futures) {
				future.get();
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		// ExecutorServiceの停止(新規のタスクを受け付けないようにする)
		pool.shutdown();

		// 合計の出力 5スレッド×10回足し算=50になるはず
		System.out.println("結果＝" + counter.count);
	}

	// スレッドで実行するためのタスク
	static class Task implements Callable<Void> {
		Counter counter;
		public Task(Counter counter) {
			this.counter = counter;
		}

		@Override
		public Void call() throws Exception {
			System.out.println("start threadName = " + Thread.currentThread().getName());

			// 10回足し算を実施
			for (int i = 0; i < 10; i++) {
				Thread.sleep(50);// 少し待つ(他の処理がある体で)
				counter.add(1);
			}
			System.out.println("end threadName = " + Thread.currentThread().getName());
			return null;
		}
	}

	// スレッドセーフではない実装
	/*
	static class Counter {
		private long count = 0;

		public void add(long i) {
//			System.out.println("足し算をします threadName = " + Thread.currentThread().getName());
			count += i;
		}

		public void mul(long i) {
//			System.out.println("かけ算をします threadName = " + Thread.currentThread().getName());
			count *= i;
		}
	}
	*/

	// スレッドセーフにした実装
	static class Counter {
		private long count = 0;

		public void add(long i) {
			synchronized(this) {
				System.out.println("足し算をします threadName = " + Thread.currentThread().getName());
				count += i;
			}
		}

		public void mul(long i) {
			synchronized(this) {
				System.out.println("かけ算をします threadName = " + Thread.currentThread().getName());
				count *= i;
			}
		}
	}
}
