package com.openfms.utils.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.fop.fonts.apps.TTFReader;

import com.openfms.model.exceptions.RenderingException;
import com.openfms.model.spi.pdf.PdfRenderer;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class PdfRendererImpl implements PdfRenderer {

	private static Log log = LogFactory.getLog(PdfRendererImpl.class);
	
	private FopFactory fopFactory = FopFactory.newInstance();
	
	private String baseDir;
	private String fontDir;
	
	private HashMap<String, Template> templates = new HashMap<String, Template>();
	
	public PdfRendererImpl() {
	}

	public PdfRendererImpl(String basedir) {
		this(basedir,basedir+"/fonts");
	}

	public PdfRendererImpl(String basedir,String fontdir) {
		this.baseDir = basedir;
		this.fontDir = fontdir;
		init();
	}
	
	
	private List<File> findFonts(List<File> fonts, File dir) {
		if(fonts==null) {
			fonts = new ArrayList<File>();
		}
		for(File f : dir.listFiles()) {
			if(f.isDirectory()) {
				findFonts(fonts, f);
			} else if (f.getName().toUpperCase().endsWith(".TTF")) {
				fonts.add(f);
			}
		}
		return fonts;
	}

	public void init () {
		try {
			fopFactory.setBaseURL(baseDir);
			fopFactory.setUserConfig(new File(baseDir+"/userconfig.xml"));
			List<File> fontFiles = findFonts(null, new File(fontDir));
			for(File f : fontFiles) {
				System.err.println("font file: "+f.getAbsolutePath());
				File fMetrics =  new File(f.getAbsolutePath()+".xml");
				if(!fMetrics.exists()) {
					TTFReader.main(new String[] { f.getAbsolutePath(), fMetrics.getAbsolutePath() } );
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String loadFile(File f, String charset) throws Exception {
		InputStream in = new FileInputStream(f);
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			IOUtils.copy(in, baos);
			return new String(baos.toByteArray(),charset);
		} catch (Exception e) {
			log.error("error loading file: "+f.getAbsolutePath()+" (exists:"+f.exists()+")");
			throw e;
		} finally {
			in.close();			
		}
	}
	

	private Template getTemplate(String template) throws IOException {
		String key = "/"+template;
		Template out = null; //templates.get(key);
		if(out == null) {
			try {
				Configuration freemarkerCfg = new Configuration();
				StringTemplateLoader tl = new StringTemplateLoader();
				freemarkerCfg.setURLEscapingCharset("utf-8");
				freemarkerCfg.setTemplateLoader(tl);
				
				String templ = loadFile(new File(baseDir+key), "utf-8");
				tl.putTemplate("a", templ);
				out = freemarkerCfg.getTemplate("a");
				templates.put(key, out);
	
			} catch (Exception e) {
				log.error("error processing template: ", e);
				throw new IOException("error creating template from string",e);
			}
		}
		return out;
	}

	private InputStream runFreemarker(Map<String,Object> model, Template t) throws IOException, TemplateException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputStreamWriter osw = new OutputStreamWriter(baos,"utf-8");
		t.process(model, osw);
		osw.flush();
		osw.close();
		return new ByteArrayInputStream(baos.toByteArray());
	}
	
	
	private byte[] renderFOP(InputStream in) throws FOPException, TransformerException, IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
		Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, baos);
	    TransformerFactory factory = TransformerFactory.newInstance();
	    Transformer transformer = factory.newTransformer();
	    transformer.setOutputProperty("encoding", "UTF-8");
	    Source src = new StreamSource(in);
	    Result res = new SAXResult(fop.getDefaultHandler());
	    transformer.transform(src, res);
	    baos.flush();
	    baos.close();
	    return baos.toByteArray();

	}
	
	@Override
	public byte[] renderPdf(String template, Map<String, Object> model) throws RenderingException {
		try {
			model.put("basedir", new File(getBaseDir()).getAbsolutePath());
			Template t = getTemplate(template);
			return renderFOP(runFreemarker(model, t));
		} catch (Exception e) {
			throw new RenderingException("RENDERING_FAILED",e);
		}
	}

	public String getFontDir() {
		return fontDir;
	}

	public void setFontDir(String fontDir) {
		this.fontDir = fontDir;
	}

	public String getBaseDir() {
		return baseDir;
	}

	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}

	
}
