package org.brioscia.javaz.h2j.mw;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.brioscia.javaz.h2j.filter.H2JProcessorFilter;

public interface OnResponseErrorCallback {

	public void onResponseError(H2JProcessorFilter filter, ServletRequest request, ServletResponse response, Exception e);
}
