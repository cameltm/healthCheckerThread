import java.net.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;

class FillDomains {

	private final static int SOCKET_PORT = 80;

	FillDomains() {
	}

	public void fill(ArrayList<Target> servers, String filePath) {

		List<String> domainsList = new ArrayList<>();
		try {
			domainsList = Files.readAllLines(Paths.get(filePath));
		} catch (FileNotFoundException fnfexc) {
			System.err.println(filePath + " not found");
		} catch (Exception exc) {
			System.err.println(exc.getMessage());
		}

		int order = 0;
		for (String hostname : domainsList) {
			try {
				servers.add(new Target(order++,
					new InetSocketAddress(InetAddress.getByName(hostname), SOCKET_PORT))
				);
			} catch (UnknownHostException e) {
				System.err.println("\tUnknown host: " + hostname + " SKIPPED");
			}
		}

		System.out.println("Total number of target servers: " + servers.size());
	}

}
