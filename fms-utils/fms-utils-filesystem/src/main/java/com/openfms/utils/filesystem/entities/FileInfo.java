package com.openfms.utils.filesystem.entities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileInfo {

	private String basedir;
	private String path;
	private String filename;
	private String target;
	private long filesize;
	private boolean directory;
	private boolean link;

	public FileInfo() {
	}
	
	public FileInfo(String basedir, File f) throws IOException {
		this.basedir = basedir;
		this.path = f.getAbsolutePath();
		this.filename = f.getName();
		this.filesize = f.length();
		Path p = Paths.get(f.toURI());
		this.link = Files.isSymbolicLink(p);
		if(this.link) {
			this.target = resolve(p);
		} else {
			this.target = f.getCanonicalPath();
		}
		this.directory = f.isDirectory();
	}

	private String resolve(Path p) throws IOException {
		if(!Files.isSymbolicLink(p)) {
			return p.toFile().getAbsolutePath();
		} else {
			return resolve(Files.readSymbolicLink(p));
		}
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public long getFilesize() {
		return filesize;
	}

	public void setFilesize(long filesize) {
		this.filesize = filesize;
	}

	public String getBasedir() {
		return basedir;
	}

	public void setBasedir(String basedir) {
		this.basedir = basedir;
	}

	public boolean isLink() {
		return link;
	}

	public void setLink(boolean link) {
		this.link = link;
	}

	public boolean isDirectory() {
		return directory;
	}

	public void setDirectory(boolean directory) {
		this.directory = directory;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

}
