package org.ssa4j;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ScrapeScriptDeployer {

	protected static Logger log = LoggerFactory.getLogger(ScrapeScriptDeployer.class);
	
	protected ClassLoader loader = ScrapeScriptDeployer.class.getClassLoader();
	
	public void setClassLoader(ClassLoader loader) {
		this.loader = loader;
	}
	
	public void deploy(File file) throws IOException {
		
		if (file.exists() == false) {
			if (file.isDirectory())
				throw new IOException(String.format("Directory '%s' does not exist", file.getAbsolutePath()));
			else 
				throw new IOException(String.format("File '%s' does not exist", file.getAbsolutePath()));
		}
		
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				deploy(f);
			}
		} else {
			if (file.getName().endsWith(".zip")) {
				
				deploy(new ZipInputStream(new FileInputStream(file)));
				
			} else if (file.getName().endsWith(".sss")) {
				
				deploy(file.getName(), new FileInputStream(file));
				
			} else {
				log.info(String.format("Ignoring '%s'", file.getName()));
			}
		}
	}
	
	public void deploy(String resource) throws IOException {
		InputStream in = loader.getResourceAsStream(resource);
		
		if (in == null)
			throw new IOException("Resource '" + resource + "' not found.");
		
		if (resource.endsWith(".zip")) {
			
			deploy(new ZipInputStream(in));
			
		} else if (resource.endsWith(".sss")) {
			
			deploy(resource, in);
			
		} else {
			throw new IOException("Unsupported file type '" + resource + "'");
		}
	}
	
	public void deploy(ZipInputStream zip) throws IOException {
		ZipEntry entry;

        while ((entry = zip.getNextEntry()) != null) {
        	if (entry.getName().endsWith(".zip")) {
				
				deploy(new ZipInputStream(zip));
				
			} else if (entry.getName().endsWith(".sss")) {
				
				deploy(entry.getName(), zip);    
				
			} else {
				log.info(String.format("Ignoring '%s'", entry.getName()));
			}
            
        }

        zip.close();
	}
	
	public void deploy(String name, InputStream in) throws IOException {
		log.info(String.format("Importing '%s'", name));
		
		int size;
        byte[] data = new byte[2048];

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        while ((size = in.read(data, 0, data.length)) != -1) {      	
            out.write(data, 0, size);
        }
        
        out.flush();
        out.close();
	}
	
	protected abstract void deploy(String name, byte[] data) throws IOException;

}
