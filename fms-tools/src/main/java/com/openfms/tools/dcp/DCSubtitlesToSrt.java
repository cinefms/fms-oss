package com.openfms.tools.dcp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.openfms.model.core.media.dcp.subtitles.DCSubtitle;
import com.openfms.model.core.media.dcp.subtitles.Font;
import com.openfms.model.core.media.dcp.subtitles.Subtitle;
import com.openfms.model.core.media.dcp.subtitles.Text;
import com.openfms.utils.common.IOUtils;
import com.openfms.utils.common.jaxb.XmlDeserializer;

public class DCSubtitlesToSrt {
	
	private File in,out;
	
	public DCSubtitlesToSrt(File in, File out) {
		this.in = in;
		this.out = out;
	}

	private List<Subtitle> findSubtitles(Font font) {
		List<Subtitle> subtitles = new ArrayList<Subtitle>();
		if(font==null) {
			return subtitles;
		}
		if(font.getContent()!=null) {
			for(Object o : font.getContent()) {
				if(o instanceof Subtitle) {
					subtitles.add((Subtitle)o);
				} else if (o instanceof Text) {
					throw new RuntimeException("text found outside of a subtitle");
				} else if (o instanceof Font) {
					subtitles.addAll(findSubtitles((Font)o));
				}
			}
		}
		return subtitles;
	}
	
	private List<Subtitle> findSubtitles(DCSubtitle sub) {
		List<Subtitle> subtitles = new ArrayList<Subtitle>();
		if(sub.getSubtitle()!=null) {
			subtitles.addAll(sub.getSubtitle());
		}
		if(sub.getFont()!=null) {
			for(Font f : sub.getFont()) {
				subtitles.addAll(findSubtitles(f));
			}
		}
		return subtitles;
	}
	
	private List<String> findText(Font font) {
		List<String> out = new ArrayList<String>();
		if(font==null) return out;
		if(font.getContent()==null) return out;
		return out;
	}
	
	private List<String> findText(Text text) {
		List<String> out = new ArrayList<String>();
		if(text==null) return out;
		if(text.getContent()==null) return out;
		for(Object o : text.getContent()) {
			if(o instanceof String) {
				out.add((String)o);
			}
		}
		return out;
	}
	
	private List<String> findText(Subtitle sub) {
		List<String> out = new ArrayList<String>();
		if(sub==null) return out;
		if(sub.getFontOrTextOrImage()==null) return out;
		for(Object o : sub.getFontOrTextOrImage()) {
			if(o instanceof Font) {
				out.addAll(findText((Font)o));
			} else if(o instanceof Text) {
				out.addAll(findText((Text)o));
			}
		}
		return out;
	}
	
	
	public static String formatTime(String timeIn) {
		return timeIn.replaceAll("(\\d{2}).(\\d{2}).(\\d{2}).(\\d{3})", "$1:$2:$3,$4");
	}

	private void convert() throws FileNotFoundException, IOException {
		FileInputStream fis = new FileInputStream(in);
		
		DCSubtitle dcs = XmlDeserializer.read(DCSubtitle.class, fis);
		List<Subtitle> subtitles = findSubtitles(dcs);
		Collections.sort(subtitles, new Comparator<Subtitle>() {
			@Override
			public int compare(Subtitle o1, Subtitle o2) {
				String s1 = o1.getTimeIn()+"";
				String s2 = o2.getTimeIn()+"";
				return s1.compareTo(s2);
			}
		});
		int a = 1;
		
		FileOutputStream fos = null;
		
		try {
			fos = new FileOutputStream(out);
			for(Subtitle s : subtitles) {
				fos.write((a+"\r").getBytes("utf-8"));
				
				String tcin = formatTime(s.getTimeIn()); 
				String tcout = formatTime(s.getTimeOut());
				fos.write((tcin+" --> "+tcout+"\r").getBytes("utf-8"));
				for(String t : findText(s)) {
					t = t.trim();
					fos.write((t+"\r").getBytes("utf-8"));
				}
				fos.write(("\r").getBytes("utf-8"));
				a++;
			}
		} catch (Exception e) {
			throw new IOException(e);
		} finally {
			try {
				fos.close();
			} catch (Exception e2) {
			}
		}
		
		
	}


	public static void main(String[] args) {
		Option in = OptionBuilder.hasArg().isRequired(true).withArgName("input").withLongOpt("input").withDescription("the input file").create('i');
		Option out = OptionBuilder.hasArg().isRequired(true).withArgName("output").withLongOpt("output").withDescription("the output file").create('o');
		Options options = new Options();
		options.addOption(in);
		options.addOption(out);
		try {
			CommandLineParser parser = new GnuParser();
			CommandLine line = parser.parse(options, args);
			File fi = new File(line.getOptionValue("input"));
			File fo = new File(line.getOptionValue("output"));
			if(fi.isFile()) {
				fo.getParentFile().mkdirs();
				try {
					DCSubtitlesToSrt d = new DCSubtitlesToSrt(fi, fo);
					d.convert();
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(-1);
				}
			} else {
				if(fi.isDirectory()) {
					fo.mkdirs();
					for(File fc : fi.listFiles(new FileFilter() {
						@Override
						public boolean accept(File pathname) {
							return pathname.isFile() && pathname.getName().toLowerCase().endsWith(".xml");
						}
					})) {
						System.err.println(fc.getAbsolutePath()+" ... ");
						try {
							DCSubtitlesToSrt d = new DCSubtitlesToSrt(fc, new File(fo.getAbsolutePath()+"/"+fc.getName()+".srt"));
							d.convert();
						} catch (Exception e) {
							System.err.println(fc.getAbsolutePath()+" ... filed!");
							e.printStackTrace();
							System.exit(-1);
						}
						
					}
					
				}
			}
		} catch (ParseException exp) {
			System.out.println("Unexpected exception:" + exp.getMessage());
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(DCSubtitlesToSrt.class.getCanonicalName(), options );
		}
	}



}
