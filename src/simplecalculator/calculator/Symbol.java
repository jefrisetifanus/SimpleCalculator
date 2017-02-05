package simplecalculator.calculator;

public class Symbol {
	private String symbol;
	private int precedence;
	private int n_parameters;
	private Assosiative assosiative;
	
	public Symbol(String symbol, int precedence, int n_parameters, Assosiative assosiative) {
		super();
		this.symbol = symbol;
		this.precedence = precedence;
		this.n_parameters = n_parameters;
		this.assosiative = assosiative;
	}

	public String getSymbol() {
		return symbol;
	}
	
	public int getN() {
		return n_parameters;
	}
	
	public Assosiative getAssosiative() {
		return assosiative;
	}
	
	public int getPrecedence() {
		return precedence;
	}
}
