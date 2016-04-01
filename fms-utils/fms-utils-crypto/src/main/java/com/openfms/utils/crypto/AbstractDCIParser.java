package com.openfms.utils.crypto;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.util.DateParser;
import org.w3c.util.InvalidDateException;

import com.openfms.utils.common.text.XmlUtil;

public abstract class AbstractDCIParser {

	protected boolean isXML(byte[] data, String rootnode) {
		try {

			Document d = XmlUtil.parse(data, true);
			
			String docRoot = d.getDocumentElement().getLocalName();
			
			if (docRoot.compareTo(rootnode) != 0) {
				return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	protected boolean isXML(File data, String rootnode) {
		try {
			Document d = XmlUtil.parse(data, true);
			if (d.getDocumentElement().getLocalName().compareTo(rootnode) != 0) {
				return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	protected static Date parseDate(String nodeValue) throws ParseException, InvalidDateException {
		return DateParser.parse(nodeValue.trim());
	}

	protected String getSingleXPathResult(Node n, String xpath) throws XPathExpressionException {
		List<Node> recipientNodes = XmlUtil.getNodes(null, n, xpath);
		if (recipientNodes.size() != 1) {
			throw new RuntimeException("unexpected number of results for ("+xpath+")");
		}
		return recipientNodes.get(0).getFirstChild().getNodeValue();
	}

	protected List<String> getMultiXPathResult(Node n, String xpath) throws XPathExpressionException {
		List<String> out = new ArrayList<String>();
		List<Node> recipientNodes = XmlUtil.getNodes(null, n, xpath);
		if(recipientNodes!=null) {
			for(int i=0;i<recipientNodes.size();i++) {
				Node nx = recipientNodes.get(i);
				out.add(nx.getFirstChild().getTextContent());
			}
		}
		return out;
	}
	
}
