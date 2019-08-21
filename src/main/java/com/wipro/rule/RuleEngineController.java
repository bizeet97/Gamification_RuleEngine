package com.wipro.rule;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wipro.rtvs.Alm;
import com.wipro.rtvs.Ci;
import com.wipro.rtvs.Rtvs;
import com.wipro.rtvs.Scm;
import com.wipro.rtvs.Sonar;


@RestController
public class RuleEngineController {
	@Autowired
	private KieSession session;
	
	@Autowired
	private TeamRepository teamRepository;
	
	@Autowired
    MongoTemplate mongoTemplate;
	
	@Autowired
    ServiceMongo mongoService;
	
	
	
	@RequestMapping(value = "/checkrule", method = RequestMethod.GET)
	//@Scheduled(cron = "0 0 12 * * ?")
      //  @Scheduled(cron = "0 */10 * * * ?")
	
	public List<Team> ruleCheck() throws IOException {
		//session.startProcess("com.wipro.rule.DroolConfig");
		
//		BufferedReader r = new BufferedReader( new FileReader( "/RIG_RULE_ENGINE/src/main/java/com/wipro/rule/Template.txt" ) );
	
		System.out.println("Hello started");
		List<Team> teams=teamRepository.findAll();
		for (Team team : teams) {
				String tname = team.getName();
				Rtvs x=mongoService.updateRtvs(tname);
				if(x!=null && tname.equalsIgnoreCase(x.getRigletName()))
				{
		mongoService.removeAchievements(tname);
		//KieSession session=new DroolConfig().getKieSession();
		Team p=mongoService.updateScmReward(tname);
		session.insert(p);
		System.out.println(p.getName());
		List<Scm> y=x.getScm();
		List<Sonar> sonars=x.getSonar();
		List<Ci> ci1=x.getCi();
		List<Alm> alm1=x.getAlm();
		session.insert(x);
		for (Scm scm : y) {
			session.insert(scm);
		}
		for (Sonar sonar : sonars) {
			session.insert(sonar);
		}
		for (Ci ci : ci1) {
			session.insert(ci);
		}
		for (Alm alm : alm1) {
			session.insert(alm);
		}
		
		System.out.println(p.getAchievements());
		//p.setAchievements(achievment);
		session.fireAllRules();
		session.dispose();
		mongoTemplate.save(p);
		}
		}
		return teams;
	}
	@RequestMapping(value = "/team", method = RequestMethod.GET)
	public List<Team> getAllTeam() {
		System.out.println("Nimisha");
	  return teamRepository.findAll();
	}
	@RequestMapping(value = "/Oneteam", method = RequestMethod.GET)
	public Team getOneTeam() {
		System.out.println("Nimisha");
		Team team=new Team();
		Query query = new Query(Criteria.where("name").is("EDN141"));
        team = mongoTemplate.findOne(query, Team.class);
        team.setName("subha");
        mongoTemplate.save(team);
		return team;
	}
	


}
