package config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import postgresql.Sensor;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "postgresqlEntityManager",
        transactionManagerRef = "postgresqlTransactionManager",
        basePackages = "postgresql"
)
public class PostgreSqlConfiguration {

    @Value("${spring.postgresql.datasource.ddlAuto}")
    private String autoDdl;

    /**
     * PostgreSQL datasource definition.
     *
     * @return datasource.
     */
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.postgresql.datasource")
    public DataSource postgresqlDataSource() {
        return DataSourceBuilder
                .create()
                .build();
    }

    /**
     * Entity manager definition.
     *
     * @param builder an EntityManagerFactoryBuilder.3
     * @return LocalContainerEntityManagerFactoryBean.
     */
    @Primary
    @Bean(name = "postgresqlEntityManager")
    public LocalContainerEntityManagerFactoryBean postgresqlEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.hbm2ddl.auto", autoDdl);
        return builder
                .dataSource(postgresqlDataSource())
                .packages(Sensor.class)
                .persistenceUnit("postgresqlPU")
                .properties(properties)
                .build();
    }

    @Primary
    @Bean(name = "postgresqlTransactionManager")
    public PlatformTransactionManager postgresqlTransactionManager(@Qualifier("postgresqlEntityManager") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    private Map<String, Object> hibernateProperties() {

        Resource resource = new ClassPathResource("hibernate.properties");

        try {
            Properties properties = PropertiesLoaderUtils.loadProperties(resource);
            return properties.entrySet().stream()
                    .collect(Collectors.toMap(
                            e -> e.getKey().toString(),
                            e -> e.getValue())
                    );
        } catch (IOException e) {
            return new HashMap<String, Object>();
        }
    }
}
