package org.zaleuco.h2j.mw;

import org.zaleuco.h2j.filter.H2JFilterException;

public interface ObjectCastModel {

	public Object cast(String value) throws H2JFilterException ;
 
}
