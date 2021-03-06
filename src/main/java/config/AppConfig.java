package config;


import formatter.ClassesFormatter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.format.FormatterRegistry;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import service.ClassesServiceImpl;
import service.StudentServiceImpl;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Configuration // đánh đấu đây là file cấu hình dự án Spring
@EnableWebMvc // đánh dấu dự án này hỗ trợ mô hình MVC
@EnableTransactionManagement // đánh dấu dự án có hỗ trợ transaction
@EnableJpaRepositories("repository") // đánh dấu dự án có sử dụng jpa repository và đường dẫn
@ComponentScan("controller")// cho Spring biết phải tìm controller ở đâu
public class AppConfig implements WebMvcConfigurer, ApplicationContextAware {
    private ApplicationContext applicationContext; // khai báo 1 Spring Container

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    //3 hàm tiếp theo cấu hình Thymleaf:
    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/views"); // tiền tố
        templateResolver.setSuffix(".html"); // hậu tố
        templateResolver.setTemplateMode(TemplateMode.HTML); // kiểu views
        templateResolver.setCharacterEncoding("UTF-8"); // định dạng chữ
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        return templateEngine;
    }

    @Bean
    public ThymeleafViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setCharacterEncoding("UTF-8");// định dạng chữ
        return viewResolver;
    }

    //5 hàm tiếp theo cấu hình JPA
    @Bean
    @Qualifier(value = "entityManager")
    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.createEntityManager();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("model"); // cung cấp vị trí các model mà EntityManager cần tạo

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());
        return em;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver"); // loại driver đang dùng
        dataSource.setUrl("jdbc:mysql://localhost:3306/manager_student"); // csdl đang dùng
        dataSource.setUsername("root"); // tài khoản sql
        dataSource.setPassword("123456"); // mật khẩu sql
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    public Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update"); // hỗ trợ upload cấu trúc bảng
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect"); // loại csdl là MySQL5
        return properties;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**") //đường dẫn ảo thay thế cho đường dẫn thật bên dưới (ngắn hơn)
                .addResourceLocations("file:" + "E:/images/");
    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver getResolver() throws IOException {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setMaxUploadSizePerFile(52428800); //kích thước tối đa
        return resolver;
    }

    @Bean
    public StudentServiceImpl studentService() {
        return new StudentServiceImpl();
    }

    @Bean
    public ClassesServiceImpl classesService() {
        return new ClassesServiceImpl();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new ClassesFormatter(applicationContext.getBean(ClassesServiceImpl.class)));
    }
}
