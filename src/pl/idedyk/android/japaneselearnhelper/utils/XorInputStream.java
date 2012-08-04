package pl.idedyk.android.japaneselearnhelper.utils;

import java.io.IOException;
import java.io.InputStream;

public class XorInputStream extends InputStream {

	private InputStream reallyInputStream;
	
	private int xor;
	
	public XorInputStream(InputStream reallyInputStream, int xor) {
		super();
		this.reallyInputStream = reallyInputStream;
		this.xor = xor;
	}

	public int read() throws IOException {
		return reallyInputStream.read() ^ xor;
	}

	@Override
	public void close() throws IOException {
		reallyInputStream.close();
	}
}
