package com.openfms.core.api.v2.controllers.project;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cinefms.dbstore.api.DBStoreBinary;
import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.api.v2.controllers.GenericController;
import com.openfms.core.service.GenericService;
import com.openfms.core.service.project.FmsCryptoCertificateService;
import com.openfms.model.core.crypto.FmsCertificate;
import com.openfms.model.core.crypto.FmsCertificate.TYPE;
import com.openfms.model.core.crypto.FmsCertificateParseResult;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.CertificateException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;
import com.openfms.model.exceptions.InvalidParameterException;

@Controller
@RequestMapping(CryptoCertController.BASE_URI)
public class CryptoCertController extends GenericController<FmsCertificate> {

	public static final String BASE_URI = "/crypto/certificates";
	
	@Autowired
	private FmsCryptoCertificateService cryptoCertificateService;

	@Override
	protected GenericService<FmsCertificate> getService() {
		return cryptoCertificateService;
	}
	
	@Override
	protected Class<FmsCertificate> getType() {
		return FmsCertificate.class;
	}
	
	
	@RequestMapping(value="/{certificateId}/data",method=RequestMethod.GET)
	@ResponseBody
	public void getCertificateData(@PathVariable String certificateId, HttpServletResponse response) throws EntityNotFoundException, DatabaseException, InvalidParameterException, IOException, AccessDeniedException {
		FmsCertificate cert = cryptoCertificateService.get(certificateId);
		DBStoreBinary data = cryptoCertificateService.getData(certificateId);
		response.setContentType("application/binary");
		response.setHeader("Content-Disposition", "attachment; filename=\""+cert.getCertificateDn().replaceAll("[^a-zA-Z0-9_-]+", "_")+".pem\"");
		if(data!=null) {
			response.setHeader("Content-Length", data.getLength()+"");
			OutputStream os = response.getOutputStream();
			data.writeTo(os);
		}
	}
	
	
	@RequestMapping(value="/upload",method=RequestMethod.POST)
	@ResponseBody
	public List<FmsCertificateParseResult> uploadCertificates(@RequestParam MultipartFile file) throws EntityNotFoundException, DatabaseException, InvalidParameterException, IOException, AccessDeniedException, CertificateException {
		return cryptoCertificateService.upload(file.getBytes());
	}
	
	

	@RequestMapping(value="",method=RequestMethod.GET)
	@ResponseBody
	public List<FmsCertificate> listCertificates(
			@RequestParam(required=false) String searchTerm,
			@RequestParam(required=false) String certificateId,
			@RequestParam(required=false) TYPE type,
			@RequestParam(required=false) String playbackDeviceId,
			@RequestParam(required=false) String certificateDnQualifier,
			@RequestParam(required=false) String certificateUuid,
			@RequestParam(required=false) String parentDnQualifier,
			@RequestParam(required=false) String parentId,
			@RequestParam(required=false) Date validAt,
			@RequestParam(required=false,defaultValue="certificateDn")  String order,
			@RequestParam(required=false,defaultValue="true")  boolean asc,
			@RequestParam(required=false,defaultValue="0") Integer start,
			@RequestParam(required=false,defaultValue="25") int max
		) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		
		DBStoreQuery q = BasicQuery.createQuery();

		if (certificateId != null) {
			q = q.eq("_id", certificateId);
		}
		if (playbackDeviceId != null) {
			q = q.eq("playbackDeviceId", playbackDeviceId);
		}
		if (type != null) {
			q = q.eq("type", type);
		}
		if (certificateId != null) {
			q = q.in("_id", certificateId);
		}
		if (searchTerm != null) {
			q = q.contains("certificateDn", searchTerm);
		}
		if (certificateDnQualifier != null) {
			q = q.eq("certificateDnQualifier", certificateDnQualifier);
		}
		if (certificateUuid != null) {
			q = q.eq("certificateUuid", certificateUuid);
		}
		if (parentDnQualifier != null) {
			q = q.eq("parentDnQualifier", parentDnQualifier);
		}
		if (parentId != null) {
			q = q.eq("parentId", parentId);
		}
		if (validAt != null) {
			q = q.lte("validFrom", validAt);
			q = q.gte("validTo", validAt);
		}
		if (order != null) {
			q = q.order(order, asc);
		}
		q = q.start(start == null ? 0 : start);
		q = q.max(max);
		return getService().list(q);
	}
	
	
	
}
