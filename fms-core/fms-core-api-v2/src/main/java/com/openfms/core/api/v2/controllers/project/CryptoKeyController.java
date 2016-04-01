package com.openfms.core.api.v2.controllers.project;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
import com.openfms.core.api.v2.controllers.GenericController;
import com.openfms.core.service.GenericService;
import com.openfms.core.service.project.FmsCryptoKeyService;
import com.openfms.model.core.crypto.FmsKey;
import com.openfms.model.core.crypto.FmsKeyParseResult;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;
import com.openfms.model.exceptions.InvalidParameterException;
import com.openfms.model.spi.crypto.KeyParser;
import com.openfms.utils.common.IOUtils;

@Controller
@RequestMapping(CryptoKeyController.BASE_URI)
public class CryptoKeyController extends GenericController<FmsKey> {

	public static final String BASE_URI = "/crypto/keys";
	
	@Autowired
	private FmsCryptoKeyService cryptoKeyService;

	@Autowired
	private KeyParser keyParser;

	@Override
	protected GenericService<FmsKey> getService() {
		return cryptoKeyService;
	}
	
	@Override
	protected Class<FmsKey> getType() {
		return FmsKey.class;
	}
	
	
	@RequestMapping(value="/{keyId}/data",method=RequestMethod.GET)
	@ResponseBody
	public void getKeyData(@PathVariable String keyId, HttpServletResponse response) throws EntityNotFoundException, DatabaseException, InvalidParameterException, IOException, AccessDeniedException {
		FmsKey key = cryptoKeyService.get(keyId);
		DBStoreBinary data = cryptoKeyService.getData(keyId);
		response.setContentType("application/binary");
		response.setHeader("Content-Disposition", "attachment; filename=\""+key.getName().replaceAll("[^a-zA-Z0-9\\._-]+", "_")+"\"");
		if(data!=null) {
			if(data.getLength()>-1) {
				response.setHeader("Content-Length", data.getLength()+"");
			}
			OutputStream os = response.getOutputStream();
			data.writeTo(os);
			os.flush();
		}
	}
	
	
	@RequestMapping(value="/upload",method=RequestMethod.POST)
	@ResponseBody
	public List<FmsKeyParseResult> uploadKeys(@RequestParam MultipartFile file, @RequestParam String movieId) throws EntityNotFoundException, DatabaseException, InvalidParameterException, IOException, AccessDeniedException {
		return cryptoKeyService.upload(movieId, file.getOriginalFilename(), file.getBytes(), "Internal Upload");
	}
	
	@RequestMapping(value="",method=RequestMethod.GET)
	@ResponseBody
	public List<FmsKey> getKeys(
			HttpServletResponse response,
			@RequestParam(required=false,defaultValue="false") boolean download,
			@RequestParam(required=false) String searchTerm,
			@RequestParam(required=false) String movieId,
			@RequestParam(required=false) String movieVersionId,
			@RequestParam(required=false) String externalId,
			@RequestParam(required=false) String mediaClipExternalId,
			@RequestParam(required=false) String mediaClipId,
			@RequestParam(required=false) String issuerDnQualifier,
			@RequestParam(required=false) String certificateDnQualifier,
			@RequestParam(required=false) String certificateId,
			@RequestParam(required=false) String deviceId,
			@RequestParam(required=false) String eventId,
			@RequestParam(required=false) Date validAt,
			@RequestParam(required=false,defaultValue="validFrom") String order,
			@RequestParam(required=false,defaultValue="true") boolean asc,
			@RequestParam(required=false,defaultValue="0") Integer start,
			@RequestParam(required=false,defaultValue="25") int max
		) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		List<FmsKey> keys = cryptoKeyService.listKeys(searchTerm, externalId, null, movieId, movieVersionId, mediaClipId, mediaClipExternalId, issuerDnQualifier, certificateDnQualifier, certificateId, deviceId, eventId, validAt, order, asc, start, max);
		
		if(!download) {
			return keys;
		}
		
		response.setContentType("application/binary");
		response.setHeader("Content-Disposition", "attachment; filename=\"keys.zip\"");
		
		System.err.println("   ---  "+keys.size()+" keys");
		 
		
		OutputStream os = null;
		ZipOutputStream zos = null;
		try {
			os =  response.getOutputStream();
			zos = new ZipOutputStream(os);
			for(int i=0;i<keys.size();i++) {
				FmsKey k = keys.get(i);
				System.err.println("   ---  "+(i+1)+"/"+keys.size()+" key: "+k.getName());
				try {
					InputStream is = cryptoKeyService.getData(k.getId()).getInputStream();
					
					ZipEntry ze = new ZipEntry(k.getId()+"-"+k.getMediaClipExternalId()+"-"+k.getName());
					zos.putNextEntry(ze);
					IOUtils.copy(is, zos);
					zos.flush();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				zos.flush();
			} catch (Exception e2) {
			}
			try {
				zos.close();
			} catch (Exception e2) {
			}
		}
		return null;
	}
	
	
	
}
