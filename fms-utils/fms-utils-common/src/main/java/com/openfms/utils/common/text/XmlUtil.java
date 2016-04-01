package com.openfms.utils.common.text;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlUtil {
	
	public static Document parse(String in, boolean namespace) throws ParserConfigurationException, SAXException, IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(in.getBytes("utf-8"));
		Document d = parse(bais,namespace);
		return d;
	}
	public static Document parse(byte[] data, boolean namespace) throws ParserConfigurationException, SAXException, IOException {
		return parse(new ByteArrayInputStream(data),namespace);
	}
	
	public static Document parse(File f, boolean namespace) throws ParserConfigurationException, SAXException, IOException {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(f);
			return parse(fis,namespace);
		} catch (Exception e) {
			throw new IOException(e);
		} finally {
			try {
				fis.close();
			} catch (Exception e2) {
			}
		}
	}
	
	public static Document parse(InputStream is, boolean namespace) throws IOException {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(namespace);
			DocumentBuilder db = dbf.newDocumentBuilder();
			return db.parse(is);
		} catch (Throwable t) {
			throw new IOException(t);
		}
	}
	
	public static Document parse(URI uri, boolean namespace) throws IOException {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(namespace);
			DocumentBuilder db = dbf.newDocumentBuilder();
			return db.parse(uri.toString());
		} catch (Throwable t) {
			throw new IOException(t);
		}
	}
	
	public static void transform(Document d, OutputStream os) throws IOException {
		try {
			DOMSource domSource = new DOMSource(d);
			StreamResult res = new StreamResult(os);
			Transformer t = TransformerFactory.newInstance().newTransformer();
			t.transform(domSource, res);
			os.flush();
		} catch (Throwable t) {
			throw new IOException(t);
		}
	}

	public static void transform(Document d, File f) throws IOException {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(f);
			transform(d,fos);
		} catch (Exception e) {
			throw new IOException(e);
		} finally {
			try {
				fos.flush();
			} catch (Exception e2) {
			}
			try {
				fos.close();
			} catch (Exception e2) {
			}
		}
	}
	
	public static List<Node> getNodes(NamespaceContext nameSpace, Node in, String expr) throws XPathExpressionException {
		XPath xp = XPathFactory.newInstance().newXPath();
		if(nameSpace!=null) {
			xp.setNamespaceContext(nameSpace);
		}
		XPathExpression exp1 = xp.compile(expr);
		NodeList nodes1 = (NodeList) exp1.evaluate(in, XPathConstants.NODESET);
		List<Node> out = new ArrayList<Node>();
		for (int i = 0; i < nodes1.getLength(); i++) {
			out.add(nodes1.item(i));
		}
		return out;
		
	}
	
	
	
}
