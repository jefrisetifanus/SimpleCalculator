package simplecalculator.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jasper.tagplugins.jstl.core.ForEach;

import simplecalculator.calculator.Calculator;



public class CalculatorServlet extends HttpServlet{
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		response.setContentType("text/html;");
		PrintWriter out = response.getWriter();
		String input = request.getParameter("input-string");
		String infix = request.getParameter("input-string");
		input = input.substring(1, infix.length());
		infix = input;		
		
		System.out.println("INFIX:"+infix + "length: "+infix.length());
		System.out.println("INPUT:"+input + "length: "+input.length());
		
		if(infix.length() > 6){
			if(infix.indexOf(" &#8730;")>-1){
				StringBuffer str = new StringBuffer(infix);
				while(str.indexOf(" &#8730;")>-1){
					int index = str.indexOf(" &#8730;");
					int endIndex = index + 8;
					str.replace(index, endIndex, "\u221A");
				}
				infix = str.toString();
				
			}
		}
		
		Calculator calc = new Calculator();
		float res = -1;
		try {
			res = calc.calculateInfix(infix);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("INFIX:"+infix + "length: "+infix.length());
		System.out.println("INPUT:"+input + "length: "+input.length());
		out.println(input+"<br>");
		out.println("Jawaban: "+res);
		//response.sendRedirect("Calculator?result="+res+"&input="+input);
		response.sendRedirect("Calculator?result="+res);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		doGet(request, response);
	}
}
