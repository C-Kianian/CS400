import java.io.IOException;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpContext;
import java.io.OutputStream;

public class HelloWebServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000),0);
        HttpContext context = server.createContext("/");
        context.setHandler(exchange -> {
		System.out.println("Server Received HTTP Request");
		String input = exchange.getRequestURI().getQuery();
		exchange.getResponseHeaders().add("Content-type","text/html");
		String[] inputs = input.split("&");
		String[] vars;
		String course_value = inputs[0].split("=")[1];
		String name_value = inputs[1].split("=")[1];
		String out = "Hello " + name_value + "! <br/> I hope you are having a great " + java.time.LocalDate.now();
		exchange.sendResponseHeaders(200,out.length());
		OutputStream oS = exchange.getResponseBody();
		oS.write(out.getBytes());
		oS.close();
	    });

        server.start();
	System.out.println("Hello Web Server Running...");
    }
}
