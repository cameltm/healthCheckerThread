import java.net.*;

class PingTask implements Runnable {

	private int timeout;
	private Target target;

	PingTask(Target target, int timeout) {
		this.target = target;
		this.timeout = timeout;
	}

	public String getHost() {
		return target.getAddress().getAddress() + ":" + target.getAddress().getPort();
	}

	@Override
	public void run() {
		Socket connection = new Socket();
		boolean reachable;
		long start = System.currentTimeMillis();
		try {
			try {
				connection.connect(target.getAddress(), timeout);
			} finally {
				connection.close();
			}
			long dur = (System.currentTimeMillis() - start);
			System.out.printf("%5d %5d ms %s%n", target.getOrder(), dur, target.getAddress().getHostString() );
			reachable = true;
		} catch (Exception e) {
			reachable = false;
		}

		if (!reachable) {
			System.out.println(
				String.format(
					"\t%s was UNREACHABLE",
					getHost()
				)
			);
		}
	}

} // PingTask class end.
