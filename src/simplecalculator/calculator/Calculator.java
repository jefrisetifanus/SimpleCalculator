package simplecalculator.calculator;

import java.util.Stack;
import java.util.Vector;

public class Calculator {
	Vector<Symbol> symbols;
	Stack<Object> stack;
	Stack<Float> calcStack;
	Stack<String> operatorStack;
	
	public Calculator() {
		symbols = new Vector<Symbol>();

		symbols.add(new Symbol("sin", 5, 1, Assosiative.RIGHT));
		symbols.add(new Symbol("cos", 5, 1, Assosiative.RIGHT));
		symbols.add(new Symbol("tan", 5, 1, Assosiative.RIGHT));
		symbols.add(new Symbol("\u221A", 4, 1, Assosiative.RIGHT));
		symbols.add(new Symbol("^", 4, 2, Assosiative.RIGHT));
		symbols.add(new Symbol("x", 3, 2, Assosiative.LEFT));
		symbols.add(new Symbol("/", 3, 2, Assosiative.LEFT));
		symbols.add(new Symbol("+", 2, 2, Assosiative.LEFT));
		symbols.add(new Symbol("-", 2, 2, Assosiative.LEFT));
	}
	
	public float calculateInfix(String infix) throws Exception{
		try {
			infix = infix.trim();
			String[] splitted = infix.split("\\s+");
			parseInfixToRPN(splitted);
			printRPN();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return calcRPN();
	}
	
	public void printRPN(){
		for(Object o : stack){
			if(o == null)
				System.out.print(o + " ");
			else if(o.getClass().equals(Symbol.class)){
				Symbol s = (Symbol) o;
				System.out.print(s.getSymbol() + " ");
			} else if(o.getClass().equals(Float.class)){
				Float f = (Float) o;
				System.out.print(f + " ");
			}
		}
		System.out.println("");
	}
	
	public float calcRPN() throws Exception{
		calcStack = new Stack<Float>();
		
		for(Object o : stack){
			for(Float f : calcStack){
				System.out.print(f + " ");
			}
			System.out.println("");
			if(o.getClass().equals(Float.class)){
				Float f = (Float) o;
				calcStack.push(f);
			} else if(o.getClass().equals(Symbol.class)){
				Symbol s = (Symbol) o;
				if(s.getN() > calcStack.size()){
					throw new Exception("format invalid");
				}
				calcSymbol(s);
			} 
		}
		
		return calcStack.peek();
	}
	
	private void calcSymbol(Symbol s) {
		float param1, param2;
		switch (s.getSymbol()) {
		case "sin":
			param1 = calcStack.pop();
			calcStack.push((float) Math.sin(Math.toRadians(param1)));
			break;
		case "cos":
			param1 = calcStack.pop();
			calcStack.push((float) Math.cos(Math.toRadians(param1)));
			break;
		case "tan":
			param1 = calcStack.pop();
			calcStack.push((float) Math.tan(Math.toRadians(param1)));
			break;
		case "\u221A":
			param1 = calcStack.pop();
			calcStack.push((float) Math.pow(param1, 0.5));
			break;
		case "^":
			param1 = calcStack.pop();
			param2 = calcStack.pop();
			calcStack.push((float) Math.pow(param2, param1));
			break;
		case "x":
			param1 = calcStack.pop();
			param2 = calcStack.pop();
			calcStack.push(param2 * param1);
			break;
		case "/":
			param1 = calcStack.pop();
			param2 = calcStack.pop();
			calcStack.push(param2 / param1);
			break;
		case "+":
			param1 = calcStack.pop();
			param2 = calcStack.pop();
			calcStack.push(param2 + param1);
			break;
		case "-":
			param1 = calcStack.pop();
			param2 = calcStack.pop();
			calcStack.push(param2 - param1);
			break;
		}
	}

	public void parseInfixToRPN(String[] strings) throws Exception {
		stack = new Stack<Object>();
		operatorStack = new Stack<String>();
		
		for(String string : strings){
			string = string.trim();
			if( isSymbol(string) ){
				Symbol symbol1 = getSymbol(string);
				if(operatorStack.isEmpty()){
					operatorStack.push(string);
					continue;
				}
				Symbol symbol2 = getTopSymbol();
				while(true){
					if(symbol2 == null) break;
					if(operatorStack.isEmpty()) break;
					if(symbol1.getAssosiative() == Assosiative.LEFT){
						if(symbol1.getPrecedence() <= symbol2.getPrecedence()){
							operatorStack.pop();
							stack.push(symbol2);
							symbol2 = getTopSymbol();
						} else {
							break;
						}
					} else if(symbol1.getAssosiative() == Assosiative.RIGHT){
						if(symbol1.getPrecedence() < symbol2.getPrecedence()){
							operatorStack.pop();
							stack.push(symbol2);
							symbol2 = getTopSymbol();
						} else {
							break;
						}
					} else {
						break;
					}
				}
				operatorStack.push(string);
			} else if( string.equals("(") ){
				operatorStack.push(string);
			} else if( string.equals(")") ){
				String op = (String) operatorStack.pop();				
				while( !op.equals("(") ){
					if(operatorStack.isEmpty())
						throw new Exception("paranthesis format is invalid");
					
					Symbol symbol = getSymbol(op);
					if(symbol == null)
						throw new Exception("a symbol is invalid");;
					
					op = (String) operatorStack.pop();
					stack.push(symbol);
				}
			} else {
				try {
					Float number = Float.parseFloat(string);
					stack.push(number);
				} catch (NumberFormatException e) {
					e.printStackTrace();
					stack = new Stack<Object>();
					operatorStack = new Stack<String>();
					return;
				}
			}
		}
		while(!operatorStack.isEmpty()){
			Symbol symbol = getTopSymbol();
			stack.push(symbol);
			operatorStack.pop();
		}
	}
	
	private boolean isSymbol(String string){
		for(Symbol symbol : symbols){
			if(symbol.getSymbol().equals(string))
				return true;
		}
		return false;
	}
	
	private Symbol getSymbol(String string){
		for(Symbol symbol : symbols){
			if(symbol.getSymbol().equals(string))
				return symbol;
		}
		return null;
	}
	
	private Symbol getTopSymbol(){
		String s = (String) operatorStack.peek();
		for(Symbol symbol : symbols){
			if(symbol.getSymbol().equals(s))
				return symbol;
		}
		return null;
	}
}
