package eu.luckyApp.settings;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class PersistenceContext {

	@Bean(name = "dataSource", destroyMethod = "close")
	DataSource dataSource(Environment env) {

		HikariConfig dataSoruceConfig = new HikariConfig();
		dataSoruceConfig.setDriverClassName(env
				.getRequiredProperty("db.driver"));

		dataSoruceConfig.setJdbcUrl(env.getRequiredProperty("db.url"));
		dataSoruceConfig.setUsername(env.getRequiredProperty("db.username"));
		dataSoruceConfig.setPassword(env.getRequiredProperty("db.password"));
		return new HikariDataSource(dataSoruceConfig);
	}

	/*
	 * @Bean(name="customerEntityManagerFactory")
	 * LocalContainerEntityManagerFactoryBean entityManagerFactory( DataSource
	 * dataSource, Environment env) { LocalContainerEntityManagerFactoryBean
	 * entityMangerFactory = new LocalContainerEntityManagerFactoryBean();
	 * entityMangerFactory.setDataSource(dataSource(env)); entityMangerFactory
	 * .setJpaVendorAdapter(new HibernateJpaVendorAdapter());
	 * entityMangerFactory.setPackagesToScan("eu.luckyApp.model");
	 * 
	 * Properties jpaProperties = new Properties();
	 * jpaProperties.put("hibernate.dialect",
	 * env.getRequiredProperty("hibernate.dialect"));
	 * 
	 * // Specifies the action that is invoked to the database when the //
	 * Hibernate // SessionFactory is created or closed.
	 * jpaProperties.put("hibernate.hbm2ddl.auto",
	 * env.getRequiredProperty("hibernate.hbm2ddl.auto"));
	 * 
	 * jpaProperties.put("hibernate.ejb.naming_strategy",
	 * env.getRequiredProperty("hibernate.ejb.naming_strategy"));
	 * 
	 * jpaProperties.put("hibernate.show_sql",
	 * env.getRequiredProperty("hibernate.show_sql"));
	 * 
	 * jpaProperties.put("hibernate.format_sql",
	 * env.getRequiredProperty("hibernate.format_sql"));
	 * 
	 * entityMangerFactory.setJpaProperties(jpaProperties);
	 * 
	 * return entityMangerFactory; }
	 * 
	 * @Bean(name="customerTransactionManager") PlatformTransactionManager
	 * transactionManager(EntityManagerFactory emf) { JpaTransactionManager
	 * txManager = new JpaTransactionManager();
	 * txManager.setEntityManagerFactory(emf); return txManager; }
	 */
}
