import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.io.File;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Stream;

/**
 * Instructions for running this WebApp alongside working Frontend, Backend,
 * DijkstraGraph, and HashtableMap implementations:
 *
 * On GoogleVM (after setting firewall to allow http traffic):
 *     run webserver using command: sudo java WebApp 80
 * Then visit through browser via http://EXTERNAL_IP/
 *
 * On Department (CSL) Linux Machines:
 *     copy all files to /afs/cs.wisc.edu/p/cs400-web/CS_LOGIN/
 *     compile WebApp in that location
 *     there is no need to run your sever, the provided index.cgi handles this 
 * Then visit through browser via https://cs400-web.cs.wisc.edu/CS_LOGIN/
 */
public class WebApp {
    public static void main(String[] args) throws IOException {
				// expects the port number as a command line argument to this program
				// or if a non-numeric argument is passed treat this like the query
				// part of a requst URI and display response through standard out
				if(args.length != 1) {
						throw new IllegalArgumentException("You must pass a command line" +
						    " argument representing the port that this servers should be" +
								" bound to when running this program.  Or a Query string.");
				}
				int portNumber = -1;
				try {
						portNumber = Integer.parseInt(args[0]);
				} catch(NumberFormatException e) {
						// When a non integer argument is passed, treat as a query string
						// and output response through standard out.  This is only used
						// when running through index.cgi on department linux machines.
						handleSingleResponse(args[0]);
						return;
				}
				
				// configure and start server on this port, responding in this way
				InetSocketAddress address = new InetSocketAddress(portNumber);
				HttpServer server = HttpServer.create(address,8);
				HttpContext context = server.createContext("/");
				context.setHandler( WebApp::requestHandler );
				System.out.println("Starting Campus Navigator Server...");
				server.start();
    }

    // http request handler handler for the context "/"
    public static void requestHandler(HttpExchange exchange) {
				try {
						// extract the query (part of URI after?) part of URI
						String query = exchange.getRequestURI().getQuery();	    
						System.out.println("Received Request with query: " + query);
						// extract argument key-value pairs from request query
						Map<String,String> keyValuePairs = parseQuery(
																					exchange.getRequestURI().getQuery());
						System.out.println("Query includes args: "+keyValuePairs);
	    
						// create backend and frontend objects to respond to this request
						FrontendInterface frontend = createWorkingFrontend("./campus.dot");
						// compute answer to user's requested problem based on query args:
						String response = generateResponseHTML(keyValuePairs,frontend);
						// generate HTML prompts for user for make next requests
						String prompts = generatePromptHTML(frontend);
						// compose response and prompts into a complete html template
						String html = composeHTML(response,prompts);
		
						// complete exchange response to send this html back to requester
						byte[] bytes = html.getBytes();
						exchange.sendResponseHeaders(200,bytes.length);
						OutputStream out = exchange.getResponseBody();
						out.write(bytes);
						out.close();
	    
				// unless something goes wrong, in which case report problem
				} catch (Exception e) {
						System.out.println("Exception Thrown: "+e.toString());
						e.printStackTrace();
						// attempt to send 500 Server Error Response to client
						try { exchange.sendResponseHeaders(500,-1); }
						catch(IOException i){} // do nothing when this fails
				}
    }

    // reads key value pairs from the query string of a URI into a map
    private static Map<String,String> parseQuery(String query) {
				HashMap<String,String> map = new HashMap<>();
				if(query != null && query.contains("="))
						Stream.of(query.split("&")).forEach(arg -> {
										String[] pair = arg.split("=");
										if(pair.length != 2)
												throw new IllegalArgumentException("Unable to split "+
												    "arg: " + arg+" into a key value pair around a "+
														"single = delimiter.");
										map.put(pair[0],pair[1]);
								});
				return map;
    }

    // creates a working Frontend, Backend, DijkstraGraph, and HashtableMap
    private static FrontendInterface createWorkingFrontend(String filename) throws IOException {
				GraphADT<String,Double> graph = new DijkstraGraph<>();
				BackendInterface backend = new Backend(graph);
				backend.loadGraphData(filename);			
				FrontendInterface frontend = new Frontend(backend);
				return frontend;
    }

    // creates the html response for the kind of question requeted (if any)
    private static String generateResponseHTML(Map<String,String> keyValuePairs, FrontendInterface frontend) {
				// compute response for shortest path request
				String response = "<div id=\"response\">";
				if(keyValuePairs.containsKey("start") &&
					 keyValuePairs.containsKey("end")) {
						response += frontend.generateShortestPathResponseHTML(
						            keyValuePairs.get("start"),
												keyValuePairs.get("end")) + "</div>";
						// compute response for other request
				} else if(keyValuePairs.containsKey("from")) {
						response += frontend.generateTenClosestDestinationsResponseHTML(
												keyValuePairs.get("from")) + "</div>";
						// otherwise, leave response div blank 
				} else
						response += "</div>";

				return response;
    }

    // generate separate div sections with a prompt for each kind of request
    private static String generatePromptHTML(FrontendInterface frontend) {
				String firstPrompt = "<div id=\"firstPrompt\">" +
						frontend.generateShortestPathPromptHTML() + "</div>";
				String secondPrompt = "<div id=\"secondPrompt\">" +
						frontend.generateTenClosestDestinationsPromptHTML() + "</div>";
				return firstPrompt + secondPrompt;
    }

    // compose reponse with prompts inside a complete html tree
    private static String composeHTML(String response, String prompts) throws IOException {
				// read contents of template file into html string
				String html = "";
				Scanner in = new Scanner(new File("template.html"));
				while(in.hasNextLine()) html += in.nextLine() + "\n";

				// replace placeholders for response and prompts
				html = html.replaceFirst("<!-- RESPONSE GOES HERE -->",response);
				html = html.replaceFirst("<!-- PROMPTS GO HERE -->",prompts);
	
				return html;
    }

		// Since we cannot run a public webserver on the department's linux
		// machines, we are using a cgi script to pass the query argument to
		// the method below, and then displaying a response to standard out.
		public static void handleSingleResponse(String query) {
				try {
						query = URLDecoder.decode(query, StandardCharsets.UTF_8);
						Map<String,String> keyValuePairs = parseQuery(query);
	    
						// create backend and frontend objects to respond to this request
						FrontendInterface frontend = createWorkingFrontend("./campus.dot");
						// compute answer to user's requested problem based on query args:
						String response = generateResponseHTML(keyValuePairs,frontend);
						// generate HTML prompts for user for make next requests
						String prompts = generatePromptHTML(frontend);
						// compose response and prompts into a complete html template
						String html = composeHTML(response,prompts);
		
						System.out.println(html);
						
				// unless something goes wrong, in which case report problem
				} catch (Exception e) {
						System.out.println("Exception Thrown: "+e.toString());
						e.printStackTrace();
				}
    }
}
