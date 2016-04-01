package com.openfms.core.api.v2.controllers.project;

import java.io.InputStream;
import java.io.OutputStream;
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
import com.cinefms.dbstore.api.impl.BasicBinary;
import com.cinefms.dbstore.api.impl.IOUtils;
import com.openfms.core.api.v2.controllers.GenericController;
import com.openfms.core.service.GenericService;
import com.openfms.core.service.project.FmsFileService;
import com.openfms.model.core.movie.FmsFile;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;
import com.openfms.model.exceptions.RequestedEntityTooLargeException;

@Controller
@RequestMapping(PackageFileController.BASE_URI)
public class PackageFileController extends GenericController<FmsFile> {

	public static final String BASE_URI = "/packages/files";
	
	@Autowired
	private FmsFileService fileService;
	
	@Override
	protected GenericService<FmsFile> getService() {
		return fileService;
	}
	
	@Override
	protected Class<FmsFile> getType() {
		return FmsFile.class;
	}

	@RequestMapping(value="",method=RequestMethod.GET)
	@ResponseBody
	public List<FmsFile> getFiles(
			@RequestParam(required=false) String searchTerm, 
			@RequestParam(required=false) String type, 
			@RequestParam(required=false) String externalId, 
			@RequestParam(required=false) String quickhash, 
			@RequestParam(required=false) String packageId, 
			@RequestParam(required=false,defaultValue="0") Integer start,
			@RequestParam(required=false,defaultValue="25") int max
		) throws RequestedEntityTooLargeException, DatabaseException, EntityNotFoundException, AccessDeniedException {
		List<FmsFile> out = fileService.listFiles(searchTerm,type,externalId,quickhash, packageId, start, max);
		return out;
	}
	
	@RequestMapping(value="/{fileId}/data",method=RequestMethod.POST)
	@ResponseBody
	public void uploadFile(@PathVariable String fileId,  @RequestParam("file") MultipartFile file) throws Exception {
		InputStream is = null;
		try {
			
			is = file.getInputStream();
			BasicBinary bb = new BasicBinary(fileId,is,file.getSize());
			fileService.saveFileContents(bb);
			
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				is.close();
			} catch (Exception e2) {
			}
		}
	}
	
	
	@RequestMapping(value="/{fileId}/data",method=RequestMethod.GET)
	@ResponseBody
	public void getFile(@PathVariable String fileId,  HttpServletResponse response) throws Exception {
		InputStream is = null;
		OutputStream os = null;
		try {
			DBStoreBinary bb = fileService.getFileContents(fileId);
			is = bb.getInputStream();
			
			FmsFile f = fileService.get(fileId);

			response.setHeader("Content-Length",bb.getLength()+"");
			response.setHeader("Content-Type","application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename=\""+f.getName()+"\"");
			
			os = response.getOutputStream();
			
			IOUtils.copy(is, os);
			os.flush();
			
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				is.close();
			} catch (Exception e2) {
			}
		}
	}
	
	
	
	
	
	
}
