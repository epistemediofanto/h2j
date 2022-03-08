package org.brioscia.javaz.h2j.mw;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class StandardEntityResolver implements EntityResolver {

	private static final HashMap<String, String> entityMap = new HashMap<String, String>();
	{
		entityMap.put("&amp;", "&#x26;");
		entityMap.put("&lt;", "&#x3C;");
		entityMap.put("&gt;", "&#x3E;");
		entityMap.put("&nbsp;", "&#xA0;");
		entityMap.put("&iexcl;", "&#xA1;");
		entityMap.put("&cent;", "&#xA2;");
		entityMap.put("&pound;", "&#xA3;");
		entityMap.put("&curren;", "&#xA4;");
		entityMap.put("&yen;", "&#xA5;");
		entityMap.put("&brvbar;", "&#xA6;");
		entityMap.put("&sect;", "&#xA7;");
		entityMap.put("&uml;", "&#xA8;");
		entityMap.put("&copy;", "&#xA9;");
		entityMap.put("&ordf;", "&#xAA;");
		entityMap.put("&laquo;", "&#xAB;");
		entityMap.put("&not;", "&#xAC;");
		entityMap.put("&shy;", "&#xAD;");
		entityMap.put("&reg;", "&#xAE;");
		entityMap.put("&macr;", "&#xAF;");
		entityMap.put("&deg;", "&#xB0;");
		entityMap.put("&plusmn;", "&#xB1;");
		entityMap.put("&sup2;", "&#xB2;");
		entityMap.put("&sup3;", "&#xB3;");
		entityMap.put("&acute;", "&#xB4;");
		entityMap.put("&micro;", "&#xB5;");
		entityMap.put("&para;", "&#xB6;");
		entityMap.put("&middot;", "&#xB7;");
		entityMap.put("&cedil;", "&#xB8;");
		entityMap.put("&sup1;", "&#xB9;");
		entityMap.put("&ordm;", "&#xBA;");
		entityMap.put("&raquo;", "&#xBB;");
		entityMap.put("&frac14;", "&#xBC;");
		entityMap.put("&frac12;", "&#xBD;");
		entityMap.put("&frac34;", "&#xBE;");
		entityMap.put("&iquest;", "&#xBF;");
		entityMap.put("&Agrave;", "&#xC0;");
		entityMap.put("&Aacute;", "&#xC1;");
		entityMap.put("&Acirc;", "&#xC2;");
		entityMap.put("&Atilde;", "&#xC3;");
		entityMap.put("&Auml;", "&#xC4;");
		entityMap.put("&Aring;", "&#xC5;");
		entityMap.put("&AElig;", "&#xC6;");
		entityMap.put("&Ccedil;", "&#xC7;");
		entityMap.put("&Egrave;", "&#xC8;");
		entityMap.put("&Eacute;", "&#xC9;");
		entityMap.put("&Ecirc;", "&#xCA;");
		entityMap.put("&Euml;", "&#xCB;");
		entityMap.put("&Igrave;", "&#xCC;");
		entityMap.put("&Iacute;", "&#xCD;");
		entityMap.put("&Icirc;", "&#xCE;");
		entityMap.put("&Iuml;", "&#xCF;");
		entityMap.put("&ETH;", "&#xD0;");
		entityMap.put("&Ntilde;", "&#xD1;");
		entityMap.put("&Ograve;", "&#xD2;");
		entityMap.put("&Oacute;", "&#xD3;");
		entityMap.put("&Ocirc;", "&#xD4;");
		entityMap.put("&Otilde;", "&#xD5;");
		entityMap.put("&Ouml;", "&#xD6;");
		entityMap.put("&times;", "&#xD7;");
		entityMap.put("&Oslash;", "&#xD8;");
		entityMap.put("&Ugrave;", "&#xD9;");
		entityMap.put("&Uacute;", "&#xDA;");
		entityMap.put("&Ucirc;", "&#xDB;");
		entityMap.put("&Uuml;", "&#xDC;");
		entityMap.put("&Yacute;", "&#xDD;");
		entityMap.put("&THORN;", "&#xDE;");
		entityMap.put("&szlig;", "&#xDF;");
		entityMap.put("&agrave;", "&#xE0;");
		entityMap.put("&aacute;", "&#xE1;");
		entityMap.put("&acirc;", "&#xE2;");
		entityMap.put("&atilde;", "&#xE3;");
		entityMap.put("&auml;", "&#xE4;");
		entityMap.put("&aring;", "&#xE5;");
		entityMap.put("&aelig;", "&#xE6;");
		entityMap.put("&ccedil;", "&#xE7;");
		entityMap.put("&egrave;", "&#xE8;");
		entityMap.put("&eacute;", "&#xE9;");
		entityMap.put("&ecirc;", "&#xEA;");
		entityMap.put("&euml;", "&#xEB;");
		entityMap.put("&igrave;", "&#xEC;");
		entityMap.put("&iacute;", "&#xED;");
		entityMap.put("&icirc;", "&#xEE;");
		entityMap.put("&iuml;", "&#xEF;");
		entityMap.put("&eth;", "&#xF0;");
		entityMap.put("&ntilde;", "&#xF1;");
		entityMap.put("&ograve;", "&#xF2;");
		entityMap.put("&oacute;", "&#xF3;");
		entityMap.put("&ocirc;", "&#xF4;");
		entityMap.put("&otilde;", "&#xF5;");
		entityMap.put("&ouml;", "&#xF6;");
		entityMap.put("&divide;", "&#xF7;");
		entityMap.put("&oslash;", "&#xF8;");
		entityMap.put("&ugrave;", "&#xF9;");
		entityMap.put("&uacute;", "&#xFA;");
		entityMap.put("&ucirc;", "&#xFB;");
		entityMap.put("&uuml;", "&#xFC;");
		entityMap.put("&yacute;", "&#xFD;");
		entityMap.put("&thorn;", "&#xFE;");
		entityMap.put("&yuml;", "&#xFF;");
		entityMap.put("&fnof;", "&#x192;");
		entityMap.put("&Alpha;", "&#x391;");
		entityMap.put("&Beta;", "&#x392;");
		entityMap.put("&Gamma;", "&#x393;");
		entityMap.put("&Delta;", "&#x394;");
		entityMap.put("&Epsilon;", "&#x395;");
		entityMap.put("&Zeta;", "&#x396;");
		entityMap.put("&Eta;", "&#x397;");
		entityMap.put("&Theta;", "&#x398;");
		entityMap.put("&Iota;", "&#x399;");
		entityMap.put("&Kappa;", "&#x39A;");
		entityMap.put("&Lambda;", "&#x39B;");
		entityMap.put("&Mu;", "&#x39C;");
		entityMap.put("&Nu;", "&#x39D;");
		entityMap.put("&Xi;", "&#x39E;");
		entityMap.put("&Omicron;", "&#x39F;");
		entityMap.put("&Pi;", "&#x3A0;");
		entityMap.put("&Rho;", "&#x3A1;");
		entityMap.put("&Sigma;", "&#x3A3;");
		entityMap.put("&Tau;", "&#x3A4;");
		entityMap.put("&Upsilon;", "&#x3A5;");
		entityMap.put("&Phi;", "&#x3A6;");
		entityMap.put("&Chi;", "&#x3A7;");
		entityMap.put("&Psi;", "&#x3A8;");
		entityMap.put("&Omega;", "&#x3A9;");
		entityMap.put("&alpha;", "&#x3B1;");
		entityMap.put("&beta;", "&#x3B2;");
		entityMap.put("&gamma;", "&#x3B3;");
		entityMap.put("&delta;", "&#x3B4;");
		entityMap.put("&epsilon;", "&#x3B5;");
		entityMap.put("&zeta;", "&#x3B6;");
		entityMap.put("&eta;", "&#x3B7;");
		entityMap.put("&theta;", "&#x3B8;");
		entityMap.put("&iota;", "&#x3B9;");
		entityMap.put("&kappa;", "&#x3BA;");
		entityMap.put("&lambda;", "&#x3BB;");
		entityMap.put("&mu;", "&#x3BC;");
		entityMap.put("&nu;", "&#x3BD;");
		entityMap.put("&xi;", "&#x3BE;");
		entityMap.put("&omicron;", "&#x3BF;");
		entityMap.put("&pi;", "&#x3C0;");
		entityMap.put("&rho;", "&#x3C1;");
		entityMap.put("&sigmaf;", "&#x3C2;");
		entityMap.put("&sigma;", "&#x3C3;");
		entityMap.put("&tau;", "&#x3C4;");
		entityMap.put("&upsilon;", "&#x3C5;");
		entityMap.put("&phi;", "&#x3C6;");
		entityMap.put("&chi;", "&#x3C7;");
		entityMap.put("&psi;", "&#x3C8;");
		entityMap.put("&omega;", "&#x3C9;");
		entityMap.put("&thetasym;", "&#x3D1;");
		entityMap.put("&upsih;", "&#x3D2;");
		entityMap.put("&piv;", "&#x3D6;");
		entityMap.put("&bull;", "&#x2022;");
		entityMap.put("&hellip;", "&#x2026;");
		entityMap.put("&prime;", "&#x2032;");
		entityMap.put("&Prime;", "&#x2033;");
		entityMap.put("&oline;", "&#x203E;");
		entityMap.put("&frasl;", "&#x2044;");
		entityMap.put("&weierp;", "&#x2118;");
		entityMap.put("&image;", "&#x2111;");
		entityMap.put("&real;", "&#x211C;");
		entityMap.put("&trade;", "&#x2122;");
		entityMap.put("&alefsym;", "&#x2135;");
		entityMap.put("&larr;", "&#x2190;");
		entityMap.put("&uarr;", "&#x2191;");
		entityMap.put("&rarr;", "&#x2192;");
		entityMap.put("&darr;", "&#x2193;");
		entityMap.put("&harr;", "&#x2194;");
		entityMap.put("&crarr;", "&#x21B5;");
		entityMap.put("&lArr;", "&#x21D0;");
		entityMap.put("&uArr;", "&#x21D1;");
		entityMap.put("&rArr;", "&#x21D2;");
		entityMap.put("&dArr;", "&#x21D3;");
		entityMap.put("&hArr;", "&#x21D4;");
		entityMap.put("&forall;", "&#x2200;");
		entityMap.put("&part;", "&#x2202;");
		entityMap.put("&exist;", "&#x2203;");
		entityMap.put("&empty;", "&#x2205;");
		entityMap.put("&nabla;", "&#x2207;");
		entityMap.put("&isin;", "&#x2208;");
		entityMap.put("&notin;", "&#x2209;");
		entityMap.put("&ni;", "&#x220B;");
		entityMap.put("&prod;", "&#x220F;");
		entityMap.put("&sum;", "&#x2211;");
		entityMap.put("&minus;", "&#x2212;");
		entityMap.put("&lowast;", "&#x2217;");
		entityMap.put("&radic;", "&#x221A;");
		entityMap.put("&prop;", "&#x221D;");
		entityMap.put("&infin;", "&#x221E;");
		entityMap.put("&ang;", "&#x2220;");
		entityMap.put("&and;", "&#x2227;");
		entityMap.put("&or;", "&#x2228;");
		entityMap.put("&cap;", "&#x2229;");
		entityMap.put("&cup;", "&#x222A;");
		entityMap.put("&int;", "&#x222B;");
		entityMap.put("&there4;", "&#x2234;");
		entityMap.put("&sim;", "&#x223C;");
		entityMap.put("&cong;", "&#x2245;");
		entityMap.put("&asymp;", "&#x2248;");
		entityMap.put("&ne;", "&#x2260;");
		entityMap.put("&equiv;", "&#x2261;");
		entityMap.put("&le;", "&#x2264;");
		entityMap.put("&ge;", "&#x2265;");
		entityMap.put("&sub;", "&#x2282;");
		entityMap.put("&sup;", "&#x2283;");
		entityMap.put("&nsub;", "&#x2284;");
		entityMap.put("&sube;", "&#x2286;");
		entityMap.put("&supe;", "&#x2287;");
		entityMap.put("&oplus;", "&#x2295;");
		entityMap.put("&otimes;", "&#x2297;");
		entityMap.put("&perp;", "&#x22A5;");
		entityMap.put("&sdot;", "&#x22C5;");
		entityMap.put("&lceil;", "&#x2308;");
		entityMap.put("&rceil;", "&#x2309;");
		entityMap.put("&lfloor;", "&#x230A;");
		entityMap.put("&rfloor;", "&#x230B;");
		entityMap.put("&lang;", "&#x2329;");
		entityMap.put("&rang;", "&#x232A;");
		entityMap.put("&loz;", "&#x25CA;");
		entityMap.put("&spades;", "&#x2660;");
		entityMap.put("&clubs;", "&#x2663;");
		entityMap.put("&hearts;", "&#x2665;");
		entityMap.put("&diams;", "&#x2666;");

	}

	public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {		
		System.out.println(systemId);
		return new InputSource(entityMap.get(publicId));
	}

}
