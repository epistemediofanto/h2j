package org.zaleuco.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.zaleuco.expression.NodeToken.Type;

/***
 * <pre>
 * Grammar: G ::= #{ ADD } | ADD
 * ADD ::= MUL + ADD | MUL - ADD | MUL
 * MUL ::= POW * MUL | POW / MUL | POW
 * POW ::= CMP ^ POW | CMP
 * CMP ::= CMP &lt;= LGC | CMP &lt; LGC | CMP &gt;= LGC | CMP &gt;LGC | CMP &lt;&gt; LGC | LGC
 * LGC ::= LGC &amp; FUN | LGC '|' FUN | FUN
 * FUN ::= NAME [( PARVAL ) ] ['[' PARVAL ']'] [. FUN ] | !FUN | -FUN | (ADD)
 * PARVAL ::= ADD [, ADD]*
 * NAME ::= (alfa [num])* | NUM | 'stringa'
 * NUM ::= numero
 * </pre>
 * 
 * @author Achille
 *
 */
public class LexicalParser {

	private String source;
	private List<NodeToken> tokensList;
	private int currentTokenPos = 0;
	private boolean skipSpace = true;

	public static void main(String[] argv) throws SyntaxError {
		String s;
		NodeToken nt;

		s = "'ciao' + ' '";
		nt = LexicalParser.process(s);
		System.out.println(nt);
	}

	public static NodeToken process(String source) throws SyntaxError {
		LexicalParser lexicalParser;

		lexicalParser = new LexicalParser();
		lexicalParser.source = source;

		return lexicalParser.process();
	}

	private NodeToken process() throws SyntaxError {
		String value;
		StringTokenizer stringTokenizer;
		NodeToken token;
		int pos;

		this.tokensList = new ArrayList<NodeToken>();
		pos = 0;
		stringTokenizer = new StringTokenizer(this.source, ".,:;#@[]{}!'$%&|/()=<>?^/*-+ ", true);
		while (stringTokenizer.hasMoreTokens()) {
			value = stringTokenizer.nextToken();
//			if (!" ".equals(value)) {
			token = new NodeToken();
			token.setValue(value);
			token.setPos(pos);
			this.tokensList.add(token);
//			}
			pos += value.length();
		}
		return this.createSyntaxTree();
	}

	private NodeToken createSyntaxTree() throws SyntaxError {
		NodeToken token;

		token = this.prodADD();
		if (this.peekToken().getType() != Type.nil) {
			throw new SyntaxError(this.nextToken(), " unexpected.");
		}

		return token;
	}

	private NodeToken prodADD() throws SyntaxError {
		NodeToken token;

		token = this.prodMUL();
		if (token != null) {
			NodeToken nToken;
			nToken = this.peekToken();
			if (eq(nToken, "+")) {
				nToken = this.nextToken();
				nToken.setType(Type.add);
				nToken.add(token);
				nToken.add(this.prodADD());
				token = nToken;
			} else if (eq(nToken, "-")) {
				nToken = this.nextToken();
				nToken.setType(Type.sub);
				nToken.add(token);
				nToken.add(this.prodADD());
				token = nToken;
			}
		}

		return token;
	}

	private NodeToken prodMUL() throws SyntaxError {
		NodeToken token;

		token = this.prodPOW();
		if (token != null) {
			NodeToken nToken;
			nToken = this.peekToken();
			if (eq(nToken, "*")) {
				nToken = this.nextToken();
				nToken.setType(Type.mul);
				nToken.add(token);
				nToken.add(this.prodMUL());
				token = nToken;
			} else if (eq(nToken, "/")) {
				nToken = this.nextToken();
				nToken.setType(Type.div);
				nToken.add(token);
				nToken.add(this.prodMUL());
				token = nToken;
			}
		}

		return token;
	}

	private NodeToken prodPOW() throws SyntaxError {
		NodeToken token;

		token = this.prodCMP();
		if (token != null) {
			NodeToken nToken;
			nToken = this.peekToken();
			if (eq(nToken, "^")) {
				nToken = this.nextToken();
				nToken.setType(Type.pow);
				nToken.add(token);
				nToken.add(this.prodPOW());
				token = nToken;
			}
		}

		return token;

	}

	private NodeToken prodCMP() throws SyntaxError {
		NodeToken token;

		token = this.prodLGC();
		if (token != null) {
			NodeToken nToken;
			nToken = this.peekToken();
			if (eq(nToken, "<")) {
				nToken = this.nextToken();
				nToken.add(token);
				token = this.peekToken();
				if (eq(token, ">")) {
					this.nextToken();
					nToken.setType(Type.neq);
				} else if (eq(token, "=")) {
					this.nextToken();
					nToken.setType(Type.let);
				} else {
					nToken.setType(Type.lt);
				}
				nToken.add(this.prodCMP());
				token = nToken;
			} else if (eq(nToken, ">")) {
				nToken = this.nextToken();
				if (eq(this.peekToken(), "=")) {
					this.nextToken();
					nToken.setType(Type.get);
				} else {
					nToken.setType(Type.gt);
				}
				nToken.add(token);
				nToken.add(this.prodCMP());
				token = nToken;
			} else if (eq(nToken, "=")) {
				nToken = this.nextToken();
				assertEq(this.nextToken(), "=");
				nToken.setType(Type.eq);
				nToken.add(token);
				nToken.add(this.prodLGC());
				token = nToken;
			}
		}

		return token;
	}

	/***
	 * 
	 * <pre>
	 *  LGC ::= LGC & FUN | LGC '|' FUN | FUN
	 * </pre>
	 * 
	 * @return nodo funzione
	 * @throws SyntaxError
	 */
	private NodeToken prodLGC() throws SyntaxError {
		NodeToken token;

		token = this.prodFUN();
		if (token != null) {
			NodeToken nToken;
			nToken = this.peekToken();
			if (eq(nToken, "&")) {
				nToken = this.nextToken();
				nToken.setType(Type.and);
				nToken.add(token);
				nToken.add(this.prodLGC());
				token = nToken;
			} else if (eq(nToken, "|")) {
				nToken = this.nextToken();
				nToken.setType(Type.or);
				nToken.add(token);
				nToken.add(this.prodLGC());
				token = nToken;
			}
		}

		return token;
	}

	/***
	 * 
	 * <pre>
	 * FUN ::= NAME [( PARVAL ) ] ['[' PARVAL ']'] [. FUN ] | !FUN | -FUN | (ADD)
	 * </pre>
	 * 
	 * @return nodo funzione
	 * @throws SyntaxError
	 */
	private NodeToken prodFUN() throws SyntaxError {
		NodeToken token;

		token = this.nextToken();
		if (eq(token, "!")) {
			token.setType(Type.not);
			token.add(this.prodFUN());
		} else if (eq(token, "-")) {
			token.setType(Type.neg);
			token.add(this.prodFUN());
		} else if (eq(token, "(")) {
			token = this.prodADD();
		} else if (isNumber(token.getValue())) {
			token.setType(Type.number);
		} else if (eq(token, "'")) {
			this.skipSpace = false;
			token = prodString(token);
			this.skipSpace = true;
		} else {
			NodeToken nToken;

			assertName(token);
			token.setType(Type.name);

			nToken = this.peekToken();
			if (eq(nToken, "(")) {
				nToken = this.prodPARVAL("(", ")");
				nToken.setType(Type.function);
				token.add(nToken);
				nToken = this.peekToken();
			}
			if (eq(nToken, "[")) {
				nToken = this.prodPARVAL("[", "]");
				nToken.setType(Type.arrayIndex);
				token.add(nToken);
				nToken = this.peekToken();
			}
			if (eq(nToken, ".")) {
				token.add(nToken);
				this.nextToken();
				nToken.setType(Type.dot);
				nToken.add(this.prodFUN());
				nToken = this.peekToken();
			}
		}

		return token;

	}

	private NodeToken prodString(NodeToken nodeToken) throws SyntaxError {
		int a = 1;
		NodeToken token;
		String value = "";

		nodeToken.setType(Type.string);

		while ((token = this.nextToken()).getType() != Type.nil) {
			if (eq(token, "\\")) {
				token = this.nextToken();
				if (eq(token, "'")) {
					++a;
					value += "'";
				} else {
					value += "\\";
				}
			} else if (eq(token, "'")) {
				--a;
				if (a == 0) {
					break;
				}
			}
			value += token.getValue();
		}
		if (a != 0) {
			throw new SyntaxError(nodeToken, "invalid string value");
		}
		nodeToken.setValue(value);
		return nodeToken;
	}

	private NodeToken prodPARVAL(String open, String close) throws SyntaxError {
		NodeToken token, nToken;
		token = this.nextToken();
		assertEq(token, open);

		nToken = this.peekToken();
		if (!eq(nToken, close)) {
			do {
				token.add(this.prodADD());
				nToken = this.nextToken();
			} while (eq(nToken, ","));
			assertEq(nToken, close);
		} else {
			assertEq(this.nextToken(), close);
		}

		return token;
	}

	private void assertName(NodeToken token) throws SyntaxError {
		String value = token.getValue();
		if ((value.length() == 0) || !isAlpha(value.charAt(0))) {
			throw new SyntaxError(null, "invalid name");
		}
		for (int i = 1; i < token.getValue().length(); ++i) {
			char c = value.charAt(i);
			if (isAlpha(c) || isNumeric(c)) {
				continue;
			}
			throw new SyntaxError(token, "invalid name");
		}
	}

	private boolean eq(NodeToken token, String... strings) {
		if (strings != null) {
			for (String s : strings) {
				if (s.equals(token.getValue())) {
					return true;
				}
			}
		}
		return false;
	}

	private void assertEq(NodeToken token, String... strings) throws SyntaxError {
		if (!eq(token, strings)) {
			throw new SyntaxError(token, "expected: " + print(strings));
		}
	}

	private static String print(String... s) {
		String out = "";
		if (s != null) {
			out = "[";
			for (int i = 0; i < s.length; ++i) {
				out = (i > 1 ? "," : "") + s[i];
			}
		}
		return out;
	}

	private NodeToken peekToken() {
		NodeToken token = new NodeToken();
		token.setType(Type.nil);
		token.setValue("");

		if (this.currentTokenPos < this.tokensList.size()) {
			token = this.tokensList.get(this.currentTokenPos);
			if ((token != null) && (" ".equals(token.getValue()) && this.skipSpace)) {
				++this.currentTokenPos;
				return this.peekToken();
			}
		}

		return token;
	}

	private NodeToken nextToken() {
		NodeToken nt = new NodeToken();
		nt.setType(Type.nil);
		nt.setValue("");

		if (this.currentTokenPos < this.tokensList.size()) {
			nt = this.tokensList.get(this.currentTokenPos);
			this.currentTokenPos++;
			if ((nt != null) && (" ".equals(nt.getValue()) && this.skipSpace)) {
				return this.nextToken();
			}
		}
		return nt;
	}

	private static boolean isNumber(String value) {
		try {
			Double.parseDouble(value);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	private static boolean isNumeric(char c) {
		return (c >= '0') && (c <= '9');
	}

	private static boolean isAlpha(char c) {
		return ((c >= 'A') && (c <= 'Z')) || ((c >= 'a') && (c <= 'z')) || (c == '_');
	}
}
