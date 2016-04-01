package com.openfms.core.service.project.impl.listeners;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cinefms.dbstore.query.api.impl.BasicQuery;
import com.openfms.core.service.impl.listeners.FmsListenerAdapter;
import com.openfms.core.service.project.FmsCryptoCertificateService;
import com.openfms.core.service.project.FmsCryptoKeyService;
import com.openfms.core.service.project.FmsKeyRequestService;
import com.openfms.core.service.project.impl.ProjectDataStore;
import com.openfms.model.core.crypto.FmsCertificate;
import com.openfms.model.core.crypto.FmsCertificate.TYPE;
import com.openfms.model.core.crypto.FmsKey;
import com.openfms.model.core.crypto.FmsKeyRequest;
import com.openfms.model.core.playback.FmsPlaybackDevice;
import com.openfms.model.core.references.FmsCertificateReference;
import com.openfms.model.core.references.FmsDeviceReference;
import com.openfms.utils.crypto.impl.CertificateThumbprint;


@Component
public class CertificateListener extends FmsListenerAdapter<FmsCertificate> {
	
	@Autowired
	private FmsCryptoKeyService keyService;
	
	@Autowired
	private FmsKeyRequestService keyRequestService;
	
	@Autowired
	private FmsCryptoCertificateService certificateService;
	
	@Autowired
	private ProjectDataStore dataStore;
	
	protected CertificateListener() {
	}

	@Override
	protected boolean beforeSave(String db, FmsCertificate k) {
		try {
			{
				FmsCertificate c = dataStore.findObject(FmsCertificate.class, BasicQuery.createQuery().eq("certificateDnQualifier", k.getCertificateDnQualifier()));
				if(c!=null) {
					k.setId(c.getId());
				}
			}
			
			if(k.getParentDnQualifier().compareTo(k.getCertificateDnQualifier())==0) {
				k.setType(TYPE.ROOT);
			} else {

				List<FmsCertificate> children = dataStore.findObjects(FmsCertificate.class, BasicQuery.createQuery().eq("parentDnQualifier", k.getCertificateDnQualifier()));
				FmsCertificate parent = dataStore.findObject(FmsCertificate.class, BasicQuery.createQuery().eq("certificateDnQualifier", k.getParentDnQualifier()));
				
				if(parent!=null) {
					k.setParentId(parent.getId());
				}
				
				if(children!=null && children.size()>0) {
					k.setType(TYPE.SIGNER);
				} else {
					k.setType(TYPE.DEVICE);
				}
				
			}
			
			{
				if(k.getId()!=null) {
					List<FmsPlaybackDevice> ds = dataStore.findObjects(FmsPlaybackDevice.class, BasicQuery.createQuery().eq("certificateId", k.getId()));
					List<FmsDeviceReference> refs = new ArrayList<FmsDeviceReference>();
					for(FmsPlaybackDevice d : ds) {
						refs.add(new FmsDeviceReference(d));
					}
					k.setDevices(refs);
				}
			}
			
			{ 
				
				List<String> tdlThumbprints = new ArrayList<String>();
				
				List<FmsCertificateReference> refs = new ArrayList<FmsCertificateReference>();
				if(k.getTrustedCertificateIds()!=null) {
					for(String id : k.getTrustedCertificateIds()) {
						FmsCertificate c = certificateService.get(id);
						refs.add(new FmsCertificateReference(c));
						try {
							InputStream is = certificateService.getData(id).getInputStream();
							String x = CertificateThumbprint.getThumbprint(is);
							tdlThumbprints.add(x);
						} catch (Exception e) {
						}
					}
				}
				k.setTrustedCertificates(refs);
				k.setTdlThumbprints(tdlThumbprints);
			}
			
			
			k.setName(k.getCertificateDn());
			
		} catch (Exception e) {
			throw new RuntimeException("error setting derived data (before saving ceritificate) ... ",e);
		}
		return true;
	}

	@Override
	protected void beforeDelete(String db, FmsCertificate object) {
		try {
			if(object.getId()!=null) {
				List<FmsCertificate> cs = dataStore.findObjects(FmsCertificate.class, BasicQuery.createQuery().eq("parentId",object.getId()));
				for(FmsCertificate c: cs) {
					certificateService.delete(c.getId());
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("error deleting child certificates .... ",e);
		}
		super.beforeDelete(db, object);
	}

	@Override
	protected void deleted(String db, FmsCertificate object) {
		updated(db, object, null);
	}

	@Override
	protected void created(String db, FmsCertificate object) {
		updated(db, null, object);
	}
	
	@Override
	protected void updated(String db, FmsCertificate o, FmsCertificate n) {
		try {
			Set<String> keyIds = new TreeSet<String>();
			Set<String> keyRequestIds = new TreeSet<String>();
			for(FmsCertificate c : new FmsCertificate[] { o, n } ) {
				if(c!=null) {
					for(FmsKey k : dataStore.findObjects(FmsKey.class, BasicQuery.createQuery().eq("certificateDnQualifier", c.getCertificateDnQualifier()))) {
						keyIds.add(k.getId());
					}
					for(FmsKeyRequest kr : dataStore.findObjects(FmsKeyRequest.class, BasicQuery.createQuery().eq("certificateDnQualifier", c.getCertificateDnQualifier()))) {
						keyRequestIds.add(kr.getId());
					}
				}
			}
			for(String id : keyIds) {
				keyService.update(id);
			}
			for(String id : keyRequestIds) {
				keyRequestService.update(id);
			}
			
		} catch (Exception e) {
			throw new RuntimeException("error updating dependent objects .... ",e);
		}

		super.updated(db, o, n);
	}
	

}
