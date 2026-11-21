package br.edu.atitus.authapi.configs;

import br.edu.atitus.authapi.entities.UserEntity;
import br.edu.atitus.authapi.entities.UserType;
import br.edu.atitus.authapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SellerAccountInitializer {
    @Bean
    CommandLineRunner initializeSeller(UserRepository repository, PasswordEncoder encoder,
            @Value("${LETTERA_ADMIN_EMAIL:admin@admin.dev}") String configuredEmail,
            @Value("${LETTERA_ADMIN_PASSWORD}") String configuredPassword,
            @Value("${LETTERA_DEMO_BUYER_PASSWORD}") String buyerPassword) {
        return args -> {
            if (configuredPassword == null || configuredPassword.length() < 8)
                throw new IllegalStateException("LETTERA_ADMIN_PASSWORD precisa ter pelo menos 8 caracteres");
            if (buyerPassword == null || buyerPassword.length() < 8)
                throw new IllegalStateException("LETTERA_DEMO_BUYER_PASSWORD precisa ter pelo menos 8 caracteres");
            String email = configuredEmail.trim().toLowerCase();
            UserEntity seller = repository.findByEmail(email).orElseGet(UserEntity::new);
            seller.setEmail(email);
            seller.setName(seller.getName() == null ? "Lettera Seller" : seller.getName());
            seller.setType(UserType.Admin);
            seller.setPassword(encoder.encode(configuredPassword));
            repository.save(seller);

            String buyerEmail = "comprador@teste.com";
            if (repository.findByEmail(buyerEmail).isEmpty()) {
                UserEntity buyer = new UserEntity();
                buyer.setEmail(buyerEmail);
                buyer.setName("Comprador de Teste");
                buyer.setType(UserType.Common);
                buyer.setPassword(encoder.encode(buyerPassword));
                repository.save(buyer);
            }
        };
    }
}
