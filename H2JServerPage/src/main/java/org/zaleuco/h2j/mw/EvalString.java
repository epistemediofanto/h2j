package org.zaleuco.h2j.mw;

import org.zaleuco.h2j.filter.H2JFilterException;

public class EvalString {

	public static String process(String string, Enviroments enviroments) throws H2JFilterException {
		int posStart;
		int posEnd;
		
		while (true) {
			posStart = string.indexOf("#{");
			if (posStart != -1) {
				posEnd = string.indexOf("}", posStart);
				if (posEnd != -1) {
					String exp;
					String value;
					int len;
					len = posEnd - posStart - 3;
					exp = string.substring(posStart + 1, len);
					value = enviroments.getStringValue(exp);
					string.replace(exp, value);
					continue;
				}
			}
			break;
		}
		return string;
	}
}
