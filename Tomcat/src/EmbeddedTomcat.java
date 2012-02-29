import java.net.InetAddress;

import org.apache.catalina.Context;
import org.apache.catalina.Engine;
import org.apache.catalina.Host;
import org.apache.catalina.startup.Embedded;

public class EmbeddedTomcat {

	private String hostName = null;

	private String catalinaHomePath = null;

	private int port = 8080;

	private Embedded embedded = null;

	private Host host = null;

	public static EmbeddedTomcat instance;

	public static EmbeddedTomcat getInstance() {
		if (instance == null) {
			synchronized (EmbeddedTomcat.class) {
				if (instance == null) {
					Config config = Config.getConfig();
					instance = new EmbeddedTomcat(config.getCatalinaHomePath(),
							config.getHostName(), Integer.parseInt(config
									.getCatalinaPort()));
				}
			}
		}
		return instance;

	}

	private EmbeddedTomcat(String catalinaHomePath, String hostName, int port)

	{
		this.catalinaHomePath = catalinaHomePath;

		this.hostName = hostName;

		this.port = port;

	}

	/**
	 * 
	 * This method Starts the Tomcat server.
	 */

	public void startTomcat() throws Exception {
		// Create an embedded server
		embedded = new Embedded();
		// Create an engine
		Engine engine = embedded.createEngine();
		embedded.setCatalinaHome(catalinaHomePath);

		// Create a default virtual host
		host = embedded.createHost(hostName, embedded.getCatalinaHome()
				+ "/webapps");

		engine.addChild(host);
		engine.setDefaultHost(host.getName());
		host.setAutoDeploy(true);
		// Create the ROOT context
		Context rootCxt = embedded.createContext("/ROOT", host.getAppBase()
				+ "/ROOT");

		Context manageCxt = embedded.createContext("/manager",
				host.getAppBase() + "/manager");

		// Create your own context

		Context scoreCxt = embedded.createContext("/valuelist-0.1.8",
				host.getAppBase() + "/valuelist-0.1.8");

		rootCxt.setPrivileged(true);

		host.addChild(rootCxt);

		host.addChild(manageCxt);

		host.addChild(scoreCxt);

		// Install the assembled container hierarchy

		embedded.addEngine(engine);

		// Assemble and install a default HTTP connector

		embedded.addConnector(embedded.createConnector(
				InetAddress.getByName(null), port, false));

		// Start the embedded server
		System.out.println("start-----------");
		embedded.start();
		System.out.println("end-----------");
	}

	/**
	 * 
	 * This method Stops the Tomcat server.
	 */

	public void stopTomcat() throws Exception {

		// Stop the embedded server

		embedded.stop();

	}

	public static void main(String args[]) {

		try {

			EmbeddedTomcat tomcat = EmbeddedTomcat.getInstance();

			tomcat.startTomcat();
			Thread.sleep(500000);
		}

		catch (Exception e) {

			e.printStackTrace();

		}

	}

}
