package org.zaleuco.h2j.mw;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

import org.zaleuco.h2j.filter.H2JFilterException;

public class ObjectWrapper {

	public static final String MARKER = "#$@-@$#";

	public static String write(Object object) throws H2JFilterException {
		String ow;
		if (object instanceof Serializable) {
			ByteArrayOutputStream bos;
			ObjectOutputStream out = null;
			byte[] bytes;

			bos = new ByteArrayOutputStream();
			try {
				out = new ObjectOutputStream(bos);
				out.writeObject(object);
				out.flush();
				bytes = bos.toByteArray();
				ow = MARKER + Base64.getEncoder().encodeToString(bytes);

			} catch (IOException e) {
				throw new H2JFilterException(e);
			} finally {
				try {
					if (bos != null) {
						bos.close();
					}
				} catch (IOException ex) {
					throw new H2JFilterException(ex);
				}
			}
		} else {
			throw new H2JFilterException(object.getClass().getName() + " must be serializable");
		}
		return ow;
	}

	public static Object read(String data) throws H2JFilterException {
		ByteArrayInputStream bis;
		ObjectInput in = null;
		Object o = null;

		if (data != null) {
			if (data.startsWith(MARKER)) {
				data = data.substring(MARKER.length());
				try {
					bis = new ByteArrayInputStream(Base64.getDecoder().decode(data));
					in = new ObjectInputStream(bis);
					o = in.readObject();
				} catch (Exception e) {
					throw new H2JFilterException(e);
				} finally {
					try {
						if (in != null) {
							in.close();
						}
					} catch (IOException ex) {
						throw new H2JFilterException(ex);
					}
				}
			} else {
				throw new H2JFilterException("Invalid mapped object");
			}
		}
		return o;
	}
}