package com.openfms.utils.filesystem.entities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Directory {

	private String basedir;
	private List<FileInfo> files;

	public String getBasedir() {
		return basedir;
	}

	public void setBasedir(String basedir) {
		this.basedir = basedir;
	}

	public List<FileInfo> getFiles() {
		return files;
	}

	public void setFiles(List<FileInfo> files) {
		this.files = files;
	}

	public static List<FileInfo> getFileInfo(String basedir, File f) throws IOException {
		try {
			List<FileInfo> out = new ArrayList<FileInfo>();
			if(f.getName().compareTo("..")==0) {
				// not a file
			} else if(f.isFile()) {
				FileInfo fi = new FileInfo(basedir, f);
				out.add(fi);
			} else if(f.isDirectory()) {
				FileInfo fi = new FileInfo(basedir, f);
				out.add(fi);
				File[] fcs = f.listFiles();
				if(fcs!=null) {
					for(File fc : fcs) {
						out.addAll(getFileInfo(basedir, fc));
					}
				}
			}
			return out;
		} catch (Exception e) {
			throw new RuntimeException("error getting file info: "+basedir+" / "+(f==null?"[null]":f.getAbsolutePath())+": ",e);
		}
	}
	
	public static Directory create(String basedir) throws IOException {
		Directory d = new Directory();
		d.setFiles(getFileInfo(basedir, new File(basedir).getCanonicalFile()));
		return d;
	}
	
	
	
}
