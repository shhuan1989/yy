package com.yijia.yy;

import com.yijia.yy.config.Constants;
import com.yijia.yy.config.DefaultProfileUtil;
import com.yijia.yy.config.JHipsterProperties;
import com.yijia.yy.service.DbInitializeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.MetricFilterAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.MetricRepositoryAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import reactor.bus.EventBus;
import reactor.fn.Consumer;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;

import static reactor.bus.selector.Selectors.$;

@ComponentScan
@EnableAutoConfiguration(exclude = { MetricFilterAutoConfiguration.class, MetricRepositoryAutoConfiguration.class })
@EnableConfigurationProperties({ JHipsterProperties.class, LiquibaseProperties.class })
public class yyOaApp {

    private static final Logger log = LoggerFactory.getLogger(yyOaApp.class);

    @Inject
    private Environment env;

    @Inject
    private DbInitializeService initializeService;

    @Bean
    reactor.Environment reactorEnv() {
        return reactor.Environment.initializeIfEmpty()
            .assignErrorJournal();
    }

    @Bean
    EventBus createEventBus(reactor.Environment reactorEnv) {
        return EventBus.create(reactorEnv, reactor.Environment.THREAD_POOL);
    }

//    @Bean
//    public HttpMessageConverters customConverters() {
//        ByteArrayHttpMessageConverter arrayHttpMessageConverter = new ByteArrayHttpMessageConverter();
//        return new HttpMessageConverters(arrayHttpMessageConverter);
//    }

    @Inject
    private EventBus eventBus;

    @Inject
    @Qualifier("project")
    private Consumer projectEventReceiver;

    @Inject
    @Qualifier("approval")
    private Consumer approvalEventReceiver;

    /**
     * Initializes yyOA.
     * <p>
     * Spring profiles can be configured with a program arguments --spring.profiles.active=your-active-profile
     * <p>
     * You can find more information on how profiles work with JHipster on <a href="http://jhipster.github.io/profiles/">http://jhipster.github.io/profiles/</a>.
     */
    @PostConstruct
    public void initApplication() {
        log.info("Running with Spring profile(s) : {}", Arrays.toString(env.getActiveProfiles()));
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.contains(Constants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(Constants.SPRING_PROFILE_PRODUCTION)) {
            log.error("You have misconfigured your application! It should not run " +
                "with both the 'dev' and 'prod' profiles at the same time.");
        }
        if (activeProfiles.contains(Constants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(Constants.SPRING_PROFILE_CLOUD)) {
            log.error("You have misconfigured your application! It should not" +
                "run with both the 'dev' and 'cloud' profiles at the same time.");
        }

        try {
            initializeService.loadDataIntoDB();
        } catch (Exception e) {
            log.warn("Error while loading data", e);
        }

        eventBus.on($("project"), projectEventReceiver);
        eventBus.on($("task"), projectEventReceiver);
        eventBus.on($("comment"), projectEventReceiver);
        eventBus.on($("rate"), projectEventReceiver);
        eventBus.on($("approval-item"), approvalEventReceiver);

    }

    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments
     * @throws UnknownHostException if the local host name could not be resolved into an address
     */
    public static void main(String[] args) throws UnknownHostException {
        SpringApplication app = new SpringApplication(yyOaApp.class);
        DefaultProfileUtil.addDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();
        log.info("\n----------------------------------------------------------\n\t" +
                "Application '{}' is running! Access URLs:\n\t" +
                "Local: \t\thttp://127.0.0.1:{}\n\t" +
                "External: \thttp://{}:{}\n----------------------------------------------------------",
            env.getProperty("spring.application.name"),
            env.getProperty("server.port"),
            InetAddress.getLocalHost().getHostAddress(),
            env.getProperty("server.port"));

    }
}
