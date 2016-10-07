package sachin.seobox.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sachin.seobox.seo.SEOPage;

public class StreamUtils {
    protected static final Logger logger = LoggerFactory.getLogger(StreamUtils.class);

    public static void writeFile(File file, SEOPage seoPage) throws IOException {
	try {
	    FileOutputStream fout = new FileOutputStream(file);
	    ObjectOutputStream out = new ObjectOutputStream(fout);
	    out.writeObject(seoPage);
	    out.flush();
	    out.close();
	} catch (IOException e) {
	    logger.debug("Error in Reading File: " + file.getAbsolutePath(), e);
	}
    }

    public static SEOPage readFile(File file) {
	try {
	    ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
	    SEOPage link = (SEOPage) in.readObject();
	    in.close();
	    return link;
	} catch (IOException | ClassNotFoundException e) {
	    logger.debug("Error in Reading File: " + file.getAbsolutePath(), e);
	}
	return null;
    }

}
