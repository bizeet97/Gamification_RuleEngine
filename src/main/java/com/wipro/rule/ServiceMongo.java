package com.wipro.rule;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.wipro.rtvs.Rtvs;
import com.wipro.rtvs.Scm;
import com.wipro.rule.Team;;

@Service
public class ServiceMongo {
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
    ServiceMongo mongoService;
	
	@Autowired
	private TeamRepository teamRepository;
	
	@Autowired
    ResourceLoader resourceLoader;
	
	public Rtvs updateRtvs(String tname)
	{
		Rtvs rtvs=new Rtvs();
		Query query = new Query(Criteria.where("rigletName").is(tname));
        rtvs = mongoTemplate.findOne(query, Rtvs.class);
        //System.out.println(rtvs.getAlm());
        return rtvs;
	}
	public Team removeAchievements(String tname)
	{
		//Query query = Query.query(Criteria.where("achievements").elemMatch(Criteria.where("reward").exists(true)));
		
		Query query2 = new Query();
		query2.addCriteria(
		    new Criteria().andOperator(
		    	Criteria.where("name").is(tname),
		        Criteria.where("achievements").elemMatch(Criteria.where("reward").exists(true))
		    )
		);

		Update update = 
				new Update().pull("achievements", 
						new BasicDBObject());
//"reward", 10
		mongoTemplate.updateMulti(query2, update, Team.class);

		return null;		
	}
	public Team updateScmReward(String tname)
	{
		System.out.println("updateScmReward");
		Team team=new Team();
		Query query = new Query(Criteria.where("name").is(tname));
        team = mongoTemplate.findOne(query, Team.class);
       // System.out.println(team.getAchievements().toString());
        team.setPoints(0);
        mongoTemplate.save(team);
		return team;
	}
	public String callTemplate() throws IOException
	{
		//String rule,String toolName,String Rtv,String metricName,String operator,int threshold,int reward,String MetricNname  
		//Resource resource = resourceLoader.getResource("classpath:Template.txt");
		Resource resource = new ClassPathResource("Template.txt");
		InputStream input = resource.getInputStream();
		File file = resource.getFile();
		
		
		 BufferedReader br = new BufferedReader(new FileReader(file)); 	  
		  String st; 
		  while ((st = br.readLine()) != null) 
		    System.out.println(st); 
		  
		  appendFiles(file);
		  
		return "Jai Kishan";  
		
		
	}
	
	public static void appendFiles(File file) throws IOException {
		Resource resource = new ClassPathResource("Template.txt");
		InputStream input = resource.getInputStream();
		File file2 = resource.getFile();
		
		
		 BufferedWriter br = new BufferedWriter(new FileWriter(file2)); 	  
		  br.append('B');
	}
}
