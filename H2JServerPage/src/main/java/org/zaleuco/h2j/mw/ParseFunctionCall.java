package org.zaleuco.h2j.mw;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.zaleuco.h2j.filter.H2JFilterException;
import org.zaleuco.h2j.mw.ExpNode.Type;

public class ParseFunctionCall extends Assertion {

	public static void main(String[] argv) throws H2JFilterException {
		String s;
		s = "nome.prop (1.3, 2, altro.pippo(1,'abc'), 'abc').v";
		s = "submit.navigate('io.ht.ml9.0', demo)";
//		s = "submit.navigate()";
		System.out.println(new ParseFunctionCall().parse(s));
		System.out.println("Fine.");
	}

	public static ExpNode process(String expression) throws H2JFilterException {
		return new ParseFunctionCall().parse(expression);
	}

	private String STRING_MATCH = "'\\w*.*'";
	private String expression;
	private int currentPosition;
	private Matcher matcher;
	private String token;

	private ExpNode parse(String expression) throws H2JFilterException {
		String patternString = "\\w+|" + STRING_MATCH + "|.|(|)|,|";
		Pattern pattern = Pattern.compile(patternString);
		ExpNode node;
		this.expression = expression;

		this.matcher = pattern.matcher(expression);
		node = parse();

		assertTrue(this.token == null, "Invalid expression: %s at position %d ", this.expression, this.currentPosition);
		return node;
	}

	private ExpNode parse() throws H2JFilterException {
		ExpNode node = null;

		this.token = this.next();
		if (!")".equals(this.token)) {
			assertTrue(this.token.matches("\\w+|" + STRING_MATCH), "Invalid expression: %s at position %d ", this.expression, this.currentPosition);
			
			node = new ExpNode(token, Type.Property);
			this.token = this.next();
			if ("(".equals(this.token)) {
				ExpNode sub;
				node.setType(Type.FunctionCall);
				do {
					sub = this.parse();
					if (sub!=null) {
						node.getParameterList().add(sub);
					}
				} while (",".equals(this.token));

				assertTrue(")".equals(token), "Invalid expression: %s at position %d ", this.expression, this.currentPosition);
				this.token = this.next();
			}
			if (".".equals(this.token)) {
				if (node.getObjectName().matches("\\d")) {
					this.token = this.next();
					node.setObjectName(node.getObjectName() + "." + token);
					this.token = this.next();
				} else {
					node.setProperty(this.parse());
				}
			}
		}
		return node;

	}

	private String next() {
		String token = null;

		if (this.matcher.find()) {
			token = this.matcher.group().trim();
			this.currentPosition = this.matcher.start();
			if (token.length() == 0) {
				return this.next();
			}
		}
		return token;
	}
}
