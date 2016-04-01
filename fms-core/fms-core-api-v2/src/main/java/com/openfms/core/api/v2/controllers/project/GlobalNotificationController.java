package com.openfms.core.api.v2.controllers.project;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cinefms.dbstore.query.api.DBStoreQuery;
import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.api.v2.controllers.GenericController;
import com.openfms.core.service.GenericService;
import com.openfms.core.service.project.FmsNotificationService;
import com.openfms.model.core.auth.FmsSessionHolder;
import com.openfms.model.core.global.FmsNotification;
import com.openfms.model.exceptions.AccessDeniedException;
import com.openfms.model.exceptions.DatabaseException;
import com.openfms.model.exceptions.EntityNotFoundException;

@Controller
@RequestMapping(GlobalNotificationController.BASE_URI)
public class GlobalNotificationController  extends GenericController<FmsNotification>{

	public static final String BASE_URI = "/global/notifications";
	
	@Autowired
	private FmsNotificationService notificationService;
	
	@Override
	protected GenericService<FmsNotification> getService() {
		return notificationService;
	}

	@Override
	protected Class<FmsNotification> getType() {
		return FmsNotification.class;
	}
	
	@RequestMapping(value="",method=RequestMethod.GET)
	@ResponseBody
	public List<FmsNotification> list(
			@RequestParam(required=false,defaultValue="false") boolean includeRead
		) throws DatabaseException, EntityNotFoundException, AccessDeniedException {
		DBStoreQuery q = BasicQuery.createQuery();
		if(FmsSessionHolder.getCurrentUser()==null) {
			return new ArrayList<FmsNotification>();
		}
		q = q.eq("to", FmsSessionHolder.getCurrentUser().getId());
		if(!includeRead) {
			q = q.eq("read", false);
		}
		q = q.order("date",false);
		return notificationService.list(q);
	}

	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	@Override
	public FmsNotification get(@PathVariable String id) throws AccessDeniedException, DatabaseException, EntityNotFoundException {
		FmsNotification out = super.get(id);
		if(!out.isRead()) {
			out.setRead(true);
			try {
				super.save(out.getId(), out);
			} catch (Exception e) {
				throw new DatabaseException(e);
			}
		}
		return out;
	}
	
	
	
	
}
