package sachin.seobox.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sachin.seobox.seo.SEOPage;

public class StreamUtils implements AutoCloseable {
	private FileOutputStream fout;
	private ObjectInputStream in;
	protected static final Logger logger = LoggerFactory.getLogger(StreamUtils.class);

	public void writeFile(File file, SEOPage seoPage) throws IOException {
		fout = new FileOutputStream(file);
		ObjectOutputStream out = new ObjectOutputStream(fout);
		out.writeObject(seoPage);
		out.flush();
		out.close();
	}

	public SEOPage readFile(File file) throws FileNotFoundException, IOException, ClassNotFoundException {
		in = new ObjectInputStream(new FileInputStream(file));
		SEOPage link = (SEOPage) in.readObject();
		in.close();
		return link;
	}

	@Override
	public void close() {
		if (fout != null)
			try {
				fout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		if (in != null)
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}
