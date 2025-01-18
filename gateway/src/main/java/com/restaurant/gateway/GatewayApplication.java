package com.restaurant.gateway;

//import com.restaurant.gateway.auth.AuthenticationService;
import com.restaurant.gateway.auth.RegisterRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import static com.restaurant.gateway.user.Role.ADMIN;
import static com.restaurant.gateway.user.Role.MANAGER;

@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	/*@Bean
	public CommandLineRunner commandLineRunner(
			AuthenticationService service,
			@Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping mapping
	) {
		return args -> {
			var admin = RegisterRequest.builder()
					.firstname("Admin")
					.lastname("Admin")
					.email("admin@mail.com")
					.password("password")
					.role(ADMIN)
					.build();
			System.out.println("Admin token: " + service.register(admin).getToken());

			var manager = RegisterRequest.builder()
					.firstname("Manager")
					.lastname("Manager")
					.email("manager@mail.com")
					.password("password")
					.role(MANAGER)
					.build();
			System.out.println("Manager token: " + service.register(manager).getToken());

			System.out.println("Listing all endpoints: ");
			mapping.getHandlerMethods().forEach((requestMappingInfo, handlerMethod) -> {
				System.out.println(requestMappingInfo + " : " + handlerMethod);
			});
		};
	}*/
}
