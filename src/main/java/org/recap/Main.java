package org.recap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import brave.sampler.Sampler;

@SpringBootApplication
@EnableJpaRepositories
@EnableAutoConfiguration(exclude = {WebMvcAutoConfiguration.class})
@PropertySource("classpath:application.properties")
public class Main {

	/**
	 * The entry point of application.
	 *
	 * @param args the input arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
	
    @Bean
    public Sampler defaultSampler() {
          return Sampler.ALWAYS_SAMPLE;
    }
}
