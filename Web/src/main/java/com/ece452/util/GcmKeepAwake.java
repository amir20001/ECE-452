package com.ece452.util;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ece452.dao.UserDao;

@Component
public class GcmKeepAwake {

	@Autowired
	UserDao userDao;

	@Scheduled(fixedDelay = 120000) //every 2 mins
	public void heartbeat() {
		System.out.println("heartbeat is now running");
		List<String> gcmIds = userDao.getGcmOfAllUsersInRoom();
		Content content = new Content();
		for (String gcmId : gcmIds) {
			if (!StringUtils.isEmpty(gcmId)) {
				content.addRegId(gcmId);
			}
		}
		if (content.getRegistration_ids().size() > 0) {
			GcmHelper.post(content);
		}
	}
}
