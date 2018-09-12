package com.dontpanic.servlets;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.PrintWriter;
import com.dontpanic.ejb.*;

@WebServlet(name = "ServletTry1", urlPatterns = "/serv1")
public class ServletTry1 extends HttpServlet {

    @EJB
    private DataMiner miner;// = (DataMiner) context.lookup("");

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }



    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://192.168.1.106:7001/web-api-0.1");
        WebTarget resourceTarget = target.path("api");
        Invocation.Builder invocationBuilder = resourceTarget.request(MediaType.TEXT_PLAIN_TYPE);
        Response responseTarget = invocationBuilder.get();

        out.println("<html>");
        out.println("<head>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>" + this.getClass().getSimpleName() + "</h1>");
        out.println("<h1>" + responseTarget.readEntity(String.class) + "</h1>");
        out.println("Count: " + miner.getCount());
        out.println("<img src=\"images/8336008_300x300.jpg\">");
        out.println("</body>");
        out.println("</html>");

    }
}
