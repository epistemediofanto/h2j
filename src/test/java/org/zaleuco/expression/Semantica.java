package org.zaleuco.expression;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.brioscia.javaz.expression.Executor;
import org.brioscia.javaz.expression.InvokerException;
import org.brioscia.javaz.expression.LexicalParser;
import org.brioscia.javaz.expression.NodeToken;
import org.brioscia.javaz.expression.SyntaxError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zaleuco.expression.utils.CaseBean;
import org.zaleuco.expression.utils.TestEnvContext;

class Semantica {

	private TestEnvContext context;
	private CaseBean bean;

	@BeforeEach
	private void setup() {
		this.bean = new CaseBean();
		this.context = new TestEnvContext();
		this.context.put("ooo", this.bean);
	}

	@Test
	void oggetto_01() throws SyntaxError, InvokerException {
		NodeToken nodeToken;
		Object o;

		nodeToken = LexicalParser.process("ooo");
		o = Executor.get(nodeToken, this.context);
		assertEquals(this.bean, o);
	}

	@Test
	void oggetto_02() throws SyntaxError, InvokerException {
		NodeToken nodeToken;
		Object o;

		nodeToken = LexicalParser.process("ooo.intero");
		o = Executor.get(nodeToken, this.context);
		assertEquals(13, o);
	}

	@Test
	void oggetto_03() throws SyntaxError, InvokerException {
		NodeToken nodeToken;
		Object o;

		nodeToken = LexicalParser.process("ooo.stringa");
		o = Executor.get(nodeToken, this.context);
		assertEquals("ciao", o);
	}

	@Test
	void oggetto_04() throws SyntaxError, InvokerException {
		NodeToken nodeToken;
		Object o;

		nodeToken = LexicalParser.process("ooo.intArray");
		o = Executor.get(nodeToken, this.context);
		assertEquals(this.bean.getIntArray(), o);
	}

	@Test
	void oggetto_05() throws SyntaxError, InvokerException {
		NodeToken nodeToken;
		Object o;

		nodeToken = LexicalParser.process("ooo.intArray[2]");
		o = Executor.get(nodeToken, this.context);
		assertEquals(this.bean.getIntArray()[2], o);
	}

	@Test
	void oggetto_05b() throws SyntaxError, InvokerException {
		NodeToken nodeToken;
		Object o;

		nodeToken = LexicalParser.process("ooo.intIntArray[0,1]");
		o = Executor.get(nodeToken, this.context);
		assertEquals(this.bean.getIntIntArray()[0][1], o);
	}

	@Test
	void oggetto_05c() throws SyntaxError, InvokerException {
		NodeToken nodeToken;
		Object o;

		nodeToken = LexicalParser.process("ooo.intIntArray[5-4,5/5]");
		o = Executor.get(nodeToken, this.context);
		assertEquals(this.bean.getIntIntArray()[1][1], o);
	}

	@Test
	void oggetto_05d() throws SyntaxError, InvokerException {
		NodeToken nodeToken;
		Object o;

		nodeToken = LexicalParser.process("ooo.intIntArray[ooo.uno, ooo.due-1]");
		o = Executor.get(nodeToken, this.context);
		assertEquals(this.bean.getIntIntArray()[1][1], o);
	}

	@Test
	void oggetto_06() throws SyntaxError, InvokerException {
		NodeToken nodeToken;
		Object o;

		nodeToken = LexicalParser.process("ooo.strArray[2]");
		o = Executor.get(nodeToken, this.context);
		assertEquals(this.bean.getStrArray()[2], o);
	}

	@Test
	void oggetto_07() throws SyntaxError, InvokerException {
		NodeToken nodeToken;
		Object o;

		nodeToken = LexicalParser.process("ooo.floatArray[2]");
		o = Executor.get(nodeToken, this.context);
		assertEquals(this.bean.getFloatArray()[2], o);
	}

	@Test
	void oggetto_08() throws SyntaxError, InvokerException {
		NodeToken nodeToken;
		Object o;

		nodeToken = LexicalParser.process("ooo.doubleArray[2]");
		o = Executor.get(nodeToken, this.context);
		assertEquals(this.bean.getDoubleArray()[2], o);
	}

	@Test
	void oggetto_09() throws SyntaxError, InvokerException {
		NodeToken nodeToken;
		Object o;

		nodeToken = LexicalParser.process("ooo.boolArray[2]");
		o = Executor.get(nodeToken, this.context);
		assertEquals(this.bean.getBoolArray()[2], o);
	}

	@Test
	void oggetto_10() throws SyntaxError, InvokerException {
		NodeToken nodeToken;
		Object o;

		nodeToken = LexicalParser.process("ooo.charArray[2]");
		o = Executor.get(nodeToken, this.context);
		assertEquals(this.bean.getCharArray()[2], o);
	}

	@Test
	void oggetto_11() throws SyntaxError, InvokerException {
		NodeToken nodeToken;
		Object o;

		nodeToken = LexicalParser.process("ooo.byteArray[2]");
		o = Executor.get(nodeToken, this.context);
		assertEquals(this.bean.getByteArray()[2], o);
	}

	@Test
	void oggetto_12() throws SyntaxError, InvokerException {
		NodeToken nodeToken;
		Object o;

		nodeToken = LexicalParser.process("ooo.funInt_1()");
		o = Executor.get(nodeToken, this.context);
		assertEquals(this.bean.funInt_1(), o);
	}

	@Test
	void oggetto_13() throws SyntaxError, InvokerException {
		NodeToken nodeToken;
		Object o;

		nodeToken = LexicalParser.process("ooo.funAdd(3, 4)");
		o = Executor.get(nodeToken, this.context);
		assertEquals(this.bean.funAdd(3, 4), o);
	}

	@Test
	void oggetto_14() throws SyntaxError, InvokerException {
		NodeToken nodeToken;
		Object o;

		nodeToken = LexicalParser.process("ooo.subCaseBean");
		o = Executor.get(nodeToken, this.context);
		assertEquals(this.bean.getSubCaseBean(), o);
	}

	@Test
	void oggetto_15() throws SyntaxError, InvokerException {
		NodeToken nodeToken;
		Object o;

		nodeToken = LexicalParser.process("ooo.subCaseBean.subIntero");
		o = Executor.get(nodeToken, this.context);
		assertEquals(this.bean.getSubCaseBean().getSubIntero(), o);
	}

	@Test
	void oggetto_16() throws SyntaxError, InvokerException {
		NodeToken nodeToken;
		Object o;

		nodeToken = LexicalParser.process("ooo.subCaseBean.subStringa");
		o = Executor.get(nodeToken, this.context);
		assertEquals(this.bean.getSubCaseBean().getSubStringa(), o);
	}
	
	@Test
	void oggetto_17() throws SyntaxError, InvokerException {
		NodeToken nodeToken;
		Object o;

		nodeToken = LexicalParser.process("ooo.data");
		o = Executor.get(nodeToken, this.context);
		assertEquals(this.bean.getData(), o);
	}

	@Test
	void oggetto_18() throws SyntaxError, InvokerException {
		NodeToken nodeToken;
		Object o;

		nodeToken = LexicalParser.process("ooo.setStringa('bau')");
		Executor.get(nodeToken, this.context);
		assertEquals("bau", this.bean.getStringa());
	}
	@Test
	void oggetto_100() throws SyntaxError, InvokerException {
		NodeToken nodeToken;
		Object o;

		nodeToken = LexicalParser.process("1+1");
		o = Executor.get(nodeToken, this.context);
		assertEquals(2, o);
	}

	@Test
	void oggetto_101() throws SyntaxError, InvokerException {
		NodeToken nodeToken;
		Object o;

		nodeToken = LexicalParser.process("ooo==null");
		o = Executor.get(nodeToken, this.context);
		assertEquals(false, o);
	}

	@Test
	void oggetto_102() throws SyntaxError, InvokerException {
		NodeToken nodeToken;
		Object o;

		nodeToken = LexicalParser.process("null==ooo");
		o = Executor.get(nodeToken, this.context);
		assertEquals(false, o);
	}

	@Test
	void oggetto_103() throws SyntaxError, InvokerException {
		NodeToken nodeToken;
		Object o;

		nodeToken = LexicalParser.process("ooo<>null");
		o = Executor.get(nodeToken, this.context);
		assertEquals(true, o);
	}

	@Test
	void oggetto_200() throws SyntaxError, InvokerException {
		NodeToken nodeToken;
		Object o;

		nodeToken = LexicalParser.process("bbb.fun(ooo.uno)");
		o = Executor.get(nodeToken, this.context);
		assertEquals("bbb.fun(1)", o.toString());
	}
	
	@Test
	void oggetto_201() throws SyntaxError, InvokerException {
		NodeToken nodeToken;
		Object o;

		nodeToken = LexicalParser.process("bbb.fun(ooo.uno, fun(4+ooo.due))");
		o = Executor.get(nodeToken, this.context);
		assertEquals("bbb.fun(1, fun(6))", o.toString());
	}
	
	@Test
	void oggetto_202() throws SyntaxError, InvokerException {
		NodeToken nodeToken;
		Object o;

		nodeToken = LexicalParser.process("bbb.fun(ooo.uno, fun(ooo.stringa))");
		o = Executor.get(nodeToken, this.context);
		assertEquals("bbb.fun(1, fun('ciao'))", o.toString());
	}
	
	@Test
	void oggetto_203() throws SyntaxError, InvokerException {
		NodeToken nodeToken;
		Object o;

		nodeToken = LexicalParser.process("bbb.fun(ooo.uno, bbb.fun(ooo.stringa))");
		o = Executor.get(nodeToken, this.context);
		assertEquals("bbb.fun(1, bbb.fun('ciao'))", o.toString());
	}
	
	@Test
	void oggetto_204() throws SyntaxError, InvokerException {
		NodeToken nodeToken;
		Object o;

		nodeToken = LexicalParser.process("bbb.fun(ooo.uno, bbb.fun(ooo.stringa, ooo.strArray[2]))");
		o = Executor.get(nodeToken, this.context);
		assertEquals("bbb.fun(1, bbb.fun('ciao', 'c'))", o.toString());
	}
	
	@Test
	void oggetto_205() throws SyntaxError, InvokerException {
		NodeToken nodeToken;
		Object o;

		nodeToken = LexicalParser.process("bbb.fun(ooo.uno, bbb.fun(ooo.stringa, ooo.funAdd(3,4)))");
		o = Executor.get(nodeToken, this.context);
		assertEquals("bbb.fun(1, bbb.fun('ciao', 7))", o.toString());
	}
}
