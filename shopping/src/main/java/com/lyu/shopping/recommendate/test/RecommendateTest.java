package com.lyu.shopping.recommendate.test;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.lyu.shopping.recommendate.dto.UserActiveDTO;
import com.lyu.shopping.recommendate.dto.UserSimilarityDTO;
import com.lyu.shopping.recommendate.service.UserActiveService;
import com.lyu.shopping.recommendate.util.RecommendUtils;

/**
 * 类描述：测试推荐模块中的一些功能
 * 类名称：com.lyu.shopping.recommendate.test.RecommendateTest
 * @author 曲健磊
 * 2018年3月26日.下午6:57:01
 * @version V1.0
 */
public class RecommendateTest {

	private ClassPathXmlApplicationContext application;
	
	@Before
	public void init() {
		application = new ClassPathXmlApplicationContext("applicationContext.xml");
	}
	
	/**
	 * 测试列出所有用户的购买行为的方法
	 */
	@Test
	public void testListAllUserActive() {
		UserActiveService userActiveService = (UserActiveService) application.getBean("userActiveService");
		
		List<UserActiveDTO> userActiveDTOList = userActiveService.listAllUserActive();
		
		for (UserActiveDTO userActiveDTO : userActiveDTOList) {
			System.out.println(userActiveDTO.getUserId() + "\t" + userActiveDTO.getCategory2Id() + "\t" + userActiveDTO.getHits());
		}
		
	}
	
	/**
	 * 测试组装用户行为数据的方法
	 */
	@Test
	public void testAssembleUserBehavior() {
		UserActiveService userActiveService = (UserActiveService) application.getBean("userActiveService");
		
		List<UserActiveDTO> userActiveDTOList = userActiveService.listAllUserActive();
		
		ConcurrentHashMap<Long, ConcurrentHashMap<Long, Long>> activeMap = RecommendUtils.assembleUserBehavior(userActiveDTOList);
		
		System.out.println(activeMap.size());
		
	}
	
	/**
	 * 计算用户的相似度
	 */
	@Test
	public void testCalcSimilarityBetweenUser() {
		UserActiveService userActiveService = (UserActiveService) application.getBean("userActiveService");
		// 查询出所有用户对不同二级类目的点击行为
		List<UserActiveDTO> userActiveDTOList = userActiveService.listAllUserActive();
		// 将每个用户对每个二级类目的点击行为封装成一个个map
		ConcurrentHashMap<Long, ConcurrentHashMap<Long, Long>> activeMap = RecommendUtils.assembleUserBehavior(userActiveDTOList);
		
		// System.out.println(activeMap.size());
		// 计算出这个map中用户与用户之间的相似度
		List<UserSimilarityDTO> similarityList = RecommendUtils.calcSimilarityBetweenUsers(activeMap);
		
		// System.out.println(similarityList.size());
		// 打印结果    -- 
		for (UserSimilarityDTO usim : similarityList) {
			System.out.println(usim.getUserId() + "\t" + usim.getUserRefId() + "\t" + usim.getSimilarity());
		}
	}
	
}