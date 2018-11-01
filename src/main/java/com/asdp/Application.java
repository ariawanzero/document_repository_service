package com.asdp;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import com.asdp.service.MateriQuizService;
import com.asdp.service.MateriQuizServiceImpl;
import com.asdp.service.MenuService;
import com.asdp.service.MenuServiceImpl;
import com.asdp.service.QuestionService;
import com.asdp.service.QuestionServiceImpl;
import com.asdp.service.QuizService;
import com.asdp.service.QuizServiceImpl;
import com.asdp.service.ResponseMappingDaoService;
import com.asdp.service.ResponseMappingDaoServiceImpl;
import com.asdp.service.ResultQuizService;
import com.asdp.service.ResultQuizServiceImpl;
import com.asdp.service.UserService;
import com.asdp.service.UserServiceImpl;
import com.asdp.util.CommonResponseGenerator;
import com.asdp.util.RequestContext;
import com.asdp.util.ResponseMapping;
import com.asdp.util.ResponseMappingDBImpl;



@EnableResourceServer
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	/* SERVICE */
	@Bean
	public UserService userService() {
		return new UserServiceImpl();
	}
	
	@Bean
	public MenuService menuDaoService() {
		return new MenuServiceImpl();
	}
	
	@Bean
	public MateriQuizService materiQuizService() {
		return new MateriQuizServiceImpl();
	}
	
	@Bean
	public QuizService quizService() {
		return new QuizServiceImpl();
	}
	
	@Bean
	public ResultQuizService resutQuizService() {
		return new ResultQuizServiceImpl();
	}
	
	@Bean
	public QuestionService questionService() {
		return new QuestionServiceImpl();
	}
	
	/* UTIL */
	@Bean
	public CommonResponseGenerator commonResponseGenerator() {
		return new CommonResponseGenerator();
	}
    
    @Bean
    public RequestContext requestContext() {
    	return new RequestContext();
    }
    
    @Bean
   	public ResponseMappingDaoService responseMappingDaoService() {
   		return new ResponseMappingDaoServiceImpl();
   	}
       
    @Bean
   	public ResponseMapping responseMapping() {
   		return new ResponseMappingDBImpl(this.responseMappingDaoService());
   	}
    
	@Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            System.out.println("Let's inspect the beans provided by Spring Boot:");
            
            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }

        };
    }
	
	
}
