package sachin.seobox.helpers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.relevantcodes.extentreports.ExtentTest;

import sachin.seobox.seo.SEOPage;

public class StreamUtils {
	protected static final Logger logger = LoggerFactory.getLogger(StreamUtils.class);

	public void writeFile(File file, SEOPage seoPage) {
		try {
			FileOutputStream fout = new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(fout));
			out.writeObject(seoPage);
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.debug("Error in Writing File: " + file.getAbsolutePath(), e);
		}
	}

	public void writeTestCase(File file, ExtentTest seoPage) {
		try {
			FileOutputStream fout = new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(fout));
			out.writeObject(seoPage);
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.debug("Error in Writing File: " + file.getAbsolutePath(), e);
		}
	}

	public void writeJSON(File file, JSONObject json) {
		try {
			BufferedWriter br = new BufferedWriter(new FileWriter(file));
			json.write(br);
			br.close();
		} catch (IOException e) {
			logger.debug("Error in Writing File: " + file.getAbsolutePath(), e);
		}
	}

	public SEOPage readFile(File file) {
		try {
			ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
			SEOPage link = (SEOPage) in.readObject();
			in.close();
			return link;
		} catch (IOException | ClassNotFoundException e) {
			logger.debug("Error in Reading File: " + file.getAbsolutePath(), e);
		}
		return null;
	}

	public ExtentTest readTestCase(File file) {
		try {
			ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
			ExtentTest link = (ExtentTest) in.readObject();
			in.close();
			return link;
		} catch (IOException | ClassNotFoundException e) {
			logger.debug("Error in Reading File: " + file.getAbsolutePath(), e);
		}
		return null;
	}
}
