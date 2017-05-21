package jp.co.teruuu.mycat.util;

import java.io.IOException;
import java.io.OutputStream;

public interface ResponseHeaderGenerator {
	void generate(OutputStream out) throws IOException;
}
