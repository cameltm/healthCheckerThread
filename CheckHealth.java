// Test

import java.io.*;

import java.nio.file.*;

import java.util.concurrent.*;
import java.util.*;

import java.net.*;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class CheckHealth {

	private static ArrayList<Target> servers = new ArrayList<>();

	private static Properties prop;
	private static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
	private static int TIMEOUT = 5000;
	private static int DELAY_SESSION = 15;
	private static String DOMAINS_FILE_PATH = "domains.properties";

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		HealthConfig cfg = new HealthConfig();
		prop = cfg.getProperties();

		FORMATTER = DateTimeFormatter.ofPattern(prop.getProperty("formatter"));
		TIMEOUT = Integer.parseInt(prop.getProperty("timeout"));
		DELAY_SESSION = Integer.parseInt(prop.getProperty("delay"));
		DOMAINS_FILE_PATH = prop.getProperty("domains");

		FillDomains fill = new FillDomains();
		fill.fill(servers, DOMAINS_FILE_PATH);

		int proc_cores = Runtime.getRuntime().availableProcessors();
//		System.out.println("Available processors: " + proc_cores);
		System.out.println("Check servers health started " + LocalDateTime.now().format(FORMATTER));

		BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(proc_cores + 1);	// was 5

		ThreadPoolExecutor pool = new ThreadPoolExecutor(
			proc_cores + 1,		// corePoolSize (was 5)
			10,			// maximumPoolSize
			TIMEOUT,		// keepAliveTime
			TimeUnit.MILLISECONDS,	// TimeUnit
			workQueue		// workQueue
		);

		pool.setRejectedExecutionHandler(
			new RejectedExecutionHandler() {
				@Override
				public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
					System.out.println("Work queue is currently full");
					try {
						System.out.println("PingTask Rejected : " + ((PingTask) r).getHost());
						System.out.println("Waiting for a second !!");
						Thread.sleep(1000);	// was 3000
					} catch (InterruptedException ignore) {
						// ignore
					}
					executor.submit(r);
				}
		});
		pool.prestartAllCoreThreads();

		Collection<Future<?>> futures = new LinkedList<Future<?>>();

		while (true) {
			System.out.printf( "%s - New session srarted (domains %d)%n", LocalDateTime.now().format(FORMATTER), servers.size() );
			for (Target server : servers) {
				futures.add(pool.submit(new PingTask(server, TIMEOUT)));
			}
			for (Future<?> future : futures) {
				future.get();
			}
			System.out.printf(
				"%s - All servers checked. Will wait for %d seconds until next round%n",
				 LocalDateTime.now().format(FORMATTER), DELAY_SESSION );
			Thread.sleep(DELAY_SESSION * 1000);
		}
	} // main class end.
} // The End.
