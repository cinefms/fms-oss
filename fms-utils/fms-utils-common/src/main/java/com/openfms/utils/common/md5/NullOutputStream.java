package com.openfms.utils.common.md5;

import java.io.IOException;
import java.io.OutputStream;

public class NullOutputStream extends OutputStream {

	private boolean closed = false;

	public NullOutputStream() {
	}

	@Override
	public void close() {
		this.closed = true;
	}

	@Override
	public void flush() throws IOException {
		if (this.closed)
			_throwClosed();
	}

	private void _throwClosed() throws IOException {
		throw new IOException("This OutputStream has been closed");
	}

	@Override
	public void write(byte[] b) throws IOException {
		if (this.closed)
			_throwClosed();
	}

	@Override
	public void write(byte[] b, int offset, int len) throws IOException {
		if (this.closed)
			_throwClosed();
	}

	@Override
	public void write(int b) throws IOException {
		if (this.closed)
			_throwClosed();
	}

}
