package sachin.seobox.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import sachin.seobox.seo.SEOPage;

public class StreamUtils {
    private FileOutputStream fout;
    private ObjectInputStream in;

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

    public void closeStreams() throws IOException {
	if(fout!=null) fout.close();
	if(in!=null) in.close();
    }
}
