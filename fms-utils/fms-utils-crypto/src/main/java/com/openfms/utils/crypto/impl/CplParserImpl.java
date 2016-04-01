package com.openfms.utils.crypto.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.openfms.model.core.movie.FmsMediaClip;
import com.openfms.model.spi.crypto.CplParser;
import com.openfms.utils.common.IOUtils;
import com.openfms.utils.common.text.XmlUtil;
import com.openfms.utils.crypto.AbstractDCIParser;

@Component
public class CplParserImpl extends AbstractDCIParser implements CplParser {

	private static Log log = LogFactory.getLog(CplParserImpl.class);

	@Override
	public boolean isValidCPL(byte[] data) {
		if(log.isDebugEnabled()){ log.debug("checking as KDM: "+data.length+" bytes of data ... "); }
		return isXML(data, "CompositionPlaylist");
	}

	@Override
	public FmsMediaClip parseCpl(byte[] data) throws IOException {
		FmsMediaClip out = null;

		try {
			Document d = XmlUtil.parse(data, false);

			out = new FmsMediaClip();
			
			List<Node> titleText = XmlUtil.getNodes(null, d, "//CompositionPlaylist/ContentTitleText");
			if (titleText.size() > 0) {
				out.setName(titleText.get(0).getTextContent());
			}
			

			List<Node> idNodes = XmlUtil.getNodes(null, d, "//CompositionPlaylist/Id");
			if (idNodes.size() != 1) {
				throw new RuntimeException("unexpected number of uuids (" + idNodes.size() + ")");
			}
			out.setExternalId(getUuid(idNodes.get(0).getFirstChild().getNodeValue()));

			List<Node> creatorNodes = XmlUtil.getNodes(null, d, "//CompositionPlaylist/Creator");
			if (creatorNodes.size() == 1) {
				out.setCreator(creatorNodes.get(0).getFirstChild().getNodeValue());
			}

			List<Node> contentTitleNodes = XmlUtil.getNodes(null, d, "//CompositionPlaylist/ContentTitleText");
			if (contentTitleNodes.size() == 1) {
				out.setContentTitleText(contentTitleNodes.get(0).getFirstChild().getNodeValue());
			}
			
			List<Node> annotationNodes = XmlUtil.getNodes(null, d, "//CompositionPlaylist/AnnotationText");
			if (annotationNodes.size() == 1 && annotationNodes.get(0).hasChildNodes()) {
				out.setAnnotationText(annotationNodes.get(0).getFirstChild().getNodeValue());
			}
			
			out.setMd5(IOUtils.getMd5(data).asHex());

			return out;
		} catch (Exception e) {
			throw new IOException("error parsing CPL", e);
		}

	}


	private static String getUuid(String nodeValue) {
		if(nodeValue == null) {
			return "";
		}
		if(nodeValue.indexOf(":")>-1) {
			nodeValue = nodeValue.substring(nodeValue.lastIndexOf(":"));
		}
		return nodeValue.replaceAll("[^\\dA-Fa-f]*","");
	}

	
	public static void main(String[] args) {
		try {
			
			byte[] b = IOUtils.getBytes(new FileInputStream("/Users/rm/Eclipse/fms/apache-tomcat/var/film5519/11_3_CPL_DOM_FTR_F_RO-EN_51-RO_2K_20120927.xml"));
			CplParserImpl kpi = new CplParserImpl();
			FmsMediaClip k = kpi.parseCpl(b);
			System.err.println(k.getName());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
