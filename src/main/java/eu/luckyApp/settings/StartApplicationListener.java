package eu.luckyApp.settings;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import eu.luckyApp.settings.security.Role;
import eu.luckyApp.settings.security.User;
import eu.luckyApp.settings.security.UserRepository;

/**
 * ,ApplicationListener<ContextRefreshedEvent>
 * @author lmochel This is the class which wrote text when application is
 *         started (when is initialized) EmbeddedServletContainerInitializedEven
 */
@Component
public class StartApplicationListener implements ApplicationListener<ContextRefreshedEvent>  {

	private static final Logger LOG = Logger.getLogger(StartApplicationListener.class);

	@Autowired
	UserRepository userRepository;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		Date startDate = new Date(event.getTimestamp());
		LOG.warn("Aplikacja wystartowala! " + startDate);
		createInitialUsersAndRoles();

	}

	private void createInitialUsersAndRoles() {
		if (userRepository.findAll().isEmpty()) {
			User admin = new User();
			admin.setUserName("admin");
			admin.setUserPassword("admin123");
			admin.setEnabled(true);
			admin.getRoles().add(Role.ROLE_ADMIN);
			if (userRepository.save(admin) != null) {
				LOG.info("Initial admin is created.\nLogin: admin\nPassword:admin123");
			}


			User user = new User();
			user.setUserName("user");
			user.setUserPassword("start123");
			user.setEnabled(true);
			user.getRoles().add(Role.ROLE_USER);
			if (userRepository.save(user) != null) {
				LOG.info("Initial user is created.\nLogin: user\nPassword: start123");
			}

		}

	}

}
