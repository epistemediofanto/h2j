package org.brioscia.javaz.h2j.fs;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.servlet.ServletContext;

import org.brioscia.javaz.h2j.mw.Enviroments;

public class VirtualFileSystem {

	private HashMap<String, byte[]> fileCache;
	private ServletContext context;

	public VirtualFileSystem(ServletContext context) {
		this.fileCache = new HashMap<String, byte[]>();
		this.context = context;
	}

	public InputStream load(String filename) throws FileNotFoundException, IOException {
		byte[] stream;

		stream = this.fileCache.get(filename);
		System.out.println("read file: " + filename);
		if (stream == null) {
			stream = this.store(filename, Enviroments.enableCacheFile);
		}
		return new ByteArrayInputStream(stream);
	}

	private byte[] store(String filename, boolean cacheOn) throws FileNotFoundException, IOException {
		FileInputStream fis;
		byte[] buffer;
		File file;
		int size;
		int r, off;

		file = new File(this.context.getRealPath(filename));
		size = (int) file.length();
		buffer = new byte[size];

		off = 0;
		fis = new FileInputStream(file);
		try {
			while (((r = fis.read(buffer, off, size)) != -1) && (size > 0)) {
				off += r;
				size -= r;
			}
		} finally {
			fis.close();
		}

		if (cacheOn) {
			this.fileCache.put(filename, buffer);
		}
		return buffer;
	}

}
