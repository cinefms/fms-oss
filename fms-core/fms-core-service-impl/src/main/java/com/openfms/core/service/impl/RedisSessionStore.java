package com.openfms.core.service.impl;

import java.util.concurrent.TimeUnit;

import org.redisson.Redisson;
import org.redisson.core.RBucket;
import org.springframework.beans.factory.annotation.Autowired;

import com.openfms.core.service.SessionStore;
import com.openfms.model.core.auth.FmsSession;

public class RedisSessionStore implements SessionStore {

	@Autowired
	private Redisson redisson;

	private String namespace = "sessions";

	@Override
	public void updateSession(FmsSession session) {
		String key = namespace + ":" + session.getKey();
		RBucket<FmsSession> bucket = redisson.getBucket(key);
		bucket.setAsync(session);
		bucket.expire(30, TimeUnit.MINUTES);
	}

	@Override
	public FmsSession getSession(String key) {
		key = namespace + ":" + key;
		RBucket<FmsSession> bucket = redisson.getBucket(key);
		FmsSession out = null;
		if (bucket != null) {
			out = bucket.get();
		}
		return out;
	}

	@Override
	public void destroySession(FmsSession session) {
		if (session != null) {
			RBucket<FmsSession> bucket = redisson.getBucket(namespace + ":" + session.getKey());
			bucket.delete();
		}
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub

	}

}
