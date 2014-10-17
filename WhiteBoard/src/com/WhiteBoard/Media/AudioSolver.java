package com.WhiteBoard.Media;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.util.Log;

public class AudioSolver {

	public static void copyfile(InputStream fromFile, String target) {
		File toFile = new File(target);
		if (!toFile.getParentFile().exists()) {
			toFile.getParentFile().mkdirs();
		}
		if (toFile.exists()) {
			toFile.delete();
		}
		try {
			java.io.FileOutputStream fosto = new FileOutputStream(toFile);
			byte bt[] = new byte[1024];
			int c;
			while ((c = fromFile.read(bt)) > 0) {
				fosto.write(bt, 0, c); // ������д�����ļ�����
			}
			fromFile.close();
			fosto.close();
		} catch (Exception ex) {
			Log.e("readfile", ex.getMessage());
		}
	}
}