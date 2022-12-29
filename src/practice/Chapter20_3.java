package practice;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Chapter20_3 {

	public static void main(String[] args) {
		Task task = new Task();
		// 1スレッドで処理をするExecutorServiceを生成
		ExecutorService pool = Executors.newSingleThreadExecutor();
		try {
			// タスクを実行するとFutureが返される
			Future<Long> future = pool.submit(task);
			try {
				// スレッドの終了を待ち、戻り値を受け取る
				System.out.println("★スレッドの終了待ち");
				long sum = future.get();
				System.out.println("★計算完了=" + sum);
			// マルチキャッチ。複数の例外をまとめて処理することができる
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		} finally {
			// ExecutorServiceの停止(新規のタスクを受け付けないようにする)
			pool.shutdown();
		}
	}

	// Long型の値を返すCallable
	static class Task implements Callable<Long> {
		@Override
		public Long call() throws Exception {
			long sum = 0;
			// １から10の合計を算出
			for (int i = 0; i < 10; i++) {
				sum += (i + 1);
				// 時間のかかる処理の代わりに300ms待ち
				Thread.sleep(300);
				System.out.println("処理" + (i + 1) + "回目");
			}
			return sum;
		}
	}
}