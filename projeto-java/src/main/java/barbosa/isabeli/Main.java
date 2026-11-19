package barbosa.isabeli;

import javax.swing.SwingUtilities;

import barbosa.isabeli.api.Api;
import io.javalin.Javalin;

public class Main {
	public static void main(String args[]) {
		
	Javalin app = Javalin.create(config ->{
    	config.bundledPlugins.enableCors(cors ->{
    		cors.addRule(it -> {
    			it.anyHost();
    		});
    	});
    }).start(7000);
	
    Api.web(app);
    
    SwingUtilities.invokeLater(() -> {
        new TelaSistema().setVisible(true);
    });

	}
}