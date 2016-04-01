package com.openfms.core.api.v2.controllers.project;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.openfms.core.service.project.FmsPlaybackDeviceService;
import com.openfms.model.core.playback.FmsDeviceProtocol;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;
import com.openfms.model.exceptions.VersioningException;

@Controller
@RequestMapping(PlaybackDeviceProtocolController.BASE_URI)
public class PlaybackDeviceProtocolController  {

	public static final String BASE_URI = "/playback/deviceprotocols";
	
	@Autowired
	private FmsPlaybackDeviceService playbackDeviceService;
	
	@RequestMapping(value="",method=RequestMethod.GET)
	@ResponseBody
	public List<FmsDeviceProtocol> listPlaybackDevices(
			@RequestParam(required=false)  String searchTerm
		) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		return playbackDeviceService.listPlaybackDeviceProtocols(searchTerm);
	}
	
	@RequestMapping(value="/{playbackDeviceProtocolId}",method=RequestMethod.GET)
	@ResponseBody
	public FmsDeviceProtocol setPlaybackDeviceCredentials(@PathVariable String playbackDeviceProtocolId) throws EntityNotFoundException, DatabaseException, AccessDeniedException, VersioningException {
		return playbackDeviceService.getPlaybackDeviceProtocol(playbackDeviceProtocolId);
	}
	
	
}
