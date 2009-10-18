package org.ssa4j;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScrapeScriptDeployer {
	
	File importDir = null;
	
	protected static Logger log = LoggerFactory.getLogger(ScrapeScriptDeployer.class);
	
	/**
	 * Creates a ScreenScraper script deployer
	 * @param dir The install directory for the screen-scraper product
	 * @throws ScrapeException
	 */
	public ScrapeScriptDeployer(File dir) throws ScrapeException {
		if (dir.exists()) {
			importDir = new File(dir, "import");
			if (importDir.exists() == false) {
				throw new ScrapeException("Screen-Scraper 'import' directory '" + importDir + "' not found.");
			}
		} else {
			throw new ScrapeException("Screen-Scraper install directory '" + dir + "' not found.");
		}
	}
	
	public void deploy(String resource) throws ScrapeException {
		InputStream in = getClass().getResourceAsStream(resource);
		try {
			if (resource.endsWith(".zip")) {
				if (in == null)
					throw new ScrapeException("Resource '" + resource + "' not found.");
					deploy(new ZipInputStream(in));
			} else if (resource.endsWith(".sss")) { 
				System.out.println("Deploying: " + resource);
	
	            int size;
	            byte[] buffer = new byte[2048];
	
	            FileOutputStream fos = new FileOutputStream(new File(importDir, resource), false);
	            BufferedOutputStream bos = new BufferedOutputStream(fos, buffer.length);
	
	            while ((size = in.read(buffer, 0, buffer.length)) != -1) {
	                bos.write(buffer, 0, size);
	            }
	            bos.flush();
	            bos.close();
			} else {
				throw new ScrapeException("Unsupported file type '" + resource + "'");
			}
		} catch (IOException e) {
			throw new ScrapeException("Unabled to deploy ZIP", e);
		}
	}
	
	public void deploy(ZipInputStream zip) throws IOException {
		ZipEntry entry;
        //
        // Read each entry from the ZipInputStream until no more entry found
        // indicated by a null return value of the getNextEntry() method.
        //
        while ((entry = zip.getNextEntry()) != null) {
            log.info("Unzipping: " + entry.getName());

            int size;
            byte[] buffer = new byte[2048];

            FileOutputStream fos = new FileOutputStream(new File(importDir, entry.getName()), false);
            BufferedOutputStream bos = new BufferedOutputStream(fos, buffer.length);

            while ((size = zip.read(buffer, 0, buffer.length)) != -1) {
                bos.write(buffer, 0, size);
            }
            bos.flush();
            bos.close();
        }

        zip.close();
	}

}
