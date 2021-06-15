package by.barsnik96.HeadHunter_JSP;

//import org.springframework.boot.SpringApplication;
import by.barsnik96.HeadHunter_JSP.view.HeadHunterJSP_App_MainFrame;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.awt.*;

@EnableJpaRepositories(basePackages = "by.barsnik96.HeadHunter_JSP.repository")
@SpringBootApplication
public class HeadHunterJspApplication {

	public static void main(String[] args)
	{
		//SpringApplication.run(HeadHunterJspApplication.class, args);
		var ctx = new SpringApplicationBuilder(HeadHunterJspApplication.class)
				.headless(false).run(args);

		EventQueue.invokeLater(() -> {
			ctx.getBean(HeadHunterJspApplication.class);
			HeadHunterJSP_App_MainFrame hh_jsp_app_main_frame = new HeadHunterJSP_App_MainFrame();
			hh_jsp_app_main_frame.setVisible(true);
		});
	}
}