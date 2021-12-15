import java.net.InetSocketAddress;

class Target {
	
	private InetSocketAddress addr;
	private int order;

	Target(int order, InetSocketAddress addr) {
		this.order = order;
		this.addr = addr;
	}

	public InetSocketAddress getAddress() {
		return addr;
	}

	public int getOrder() {
		return order;
	}

}
