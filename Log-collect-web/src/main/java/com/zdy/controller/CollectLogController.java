package com.zdy.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zdy.common.Constants;
import com.zdy.entity.AppBaseLog;
import com.zdy.entity.AppLogEntity;
import com.zdy.kafka.KafkaProducerService;
import com.zdy.kafka.KafkaProperties;
import com.zdy.utils.PropertiesUtil;


import com.alibaba.fastjson.JSONObject;


import javax.servlet.http.HttpServletRequest;

/**
 */
@RestController
@RequestMapping("/collect")
public class CollectLogController {
	
	@RequestMapping(value = "/index", method = RequestMethod.POST)
	public AppLogEntity collect(@RequestBody AppLogEntity e, HttpServletRequest req) {

		//1.修正时间
		long myTime = System.currentTimeMillis() ;
		long clientTime = Long.parseLong(req.getHeader("clientTime"));
		long diff = myTime - clientTime ;
		verifyTime(e,diff) ;

		//2.基本属性复制
		copyBaseProperties(e);

		//4.发送log给kafka主题
		sendMessage(e);
		return e;
	}

	/**
	 * 消息发送
	 */
	public void sendMessage(AppLogEntity e) {
		//创建生产者
		KafkaProducerService producer=new KafkaProducerService(KafkaProperties.KAFKA_SERVER_URL,true);
		sendMessage(producer,Constants.TOPIC_APP_STARTUP,e.getAppStartupLogs());
		sendMessage(producer,Constants.TOPIC_APP_ERRROR,e.getAppErrorLogs());
		sendMessage(producer,Constants.TOPIC_APP_EVENT,e.getAppEventLogs());
		sendMessage(producer,Constants.TOPIC_APP_PAGE,e.getAppPageLogs());
		sendMessage(producer,Constants.TOPIC_APP_USAGE,e.getAppUsageLogs());
	    //关闭
		producer.close();
	}

	private void sendMessage(KafkaProducerService producer,String topic,AppBaseLog[] logs){
		for(AppBaseLog log:logs){
			//发送消息
			producer.sendMessage(topic,JSONObject.toJSONString(log));
		}
	}
	
	/**
	 * 修正时间
	 */
	private void verifyTime(AppLogEntity e,long diff){
		//startuplog
		for(AppBaseLog log : e.getAppStartupLogs()){
			log.setCreatedAtMs(log.getCreatedAtMs() + diff );
		}
		for(AppBaseLog log : e.getAppUsageLogs()){
			log.setCreatedAtMs(log.getCreatedAtMs() + diff );
		}
		for(AppBaseLog log : e.getAppPageLogs()){
			log.setCreatedAtMs(log.getCreatedAtMs() + diff );
		}
		for(AppBaseLog log : e.getAppEventLogs()){
			log.setCreatedAtMs(log.getCreatedAtMs() + diff );
		}
		for(AppBaseLog log : e.getAppErrorLogs()){
			log.setCreatedAtMs(log.getCreatedAtMs() + diff );
		}
	}

	/**
	 * 复制基本属性
	 */
	private void copyBaseProperties(AppLogEntity e){
		PropertiesUtil.copyProperties(e, e.getAppStartupLogs());
		PropertiesUtil.copyProperties(e, e.getAppErrorLogs());
		PropertiesUtil.copyProperties(e, e.getAppEventLogs());
		PropertiesUtil.copyProperties(e, e.getAppPageLogs());
		PropertiesUtil.copyProperties(e, e.getAppUsageLogs());
	}
}