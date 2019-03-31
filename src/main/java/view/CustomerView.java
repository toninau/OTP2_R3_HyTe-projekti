package view;

import java.io.IOException;
import java.util.HashMap;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import model.WeatherAPICall;

public class CustomerView extends ViewChanger {
	
	@FXML Text weatherCity;
	@FXML Text weatherState;
	@FXML Text weatherCelsius;
	@FXML TextArea notification;
	@FXML Text welcomeText;
	@FXML Button logout;
	@FXML Button mondaybutton;
	@FXML Button tuesdaybutton;
	@FXML Button wednesdaybutton;
	@FXML Button thursdaybutton;
	@FXML Button fridaybutton;
	@FXML Button saturdaybutton;
	@FXML Button sundaybutton;
	@FXML HBox mondaytextarea;
	@FXML HBox tuesdaytextarea;
	@FXML HBox wednesdaytextarea;
	@FXML HBox thursdaytextarea;
	@FXML HBox fridaytextarea;
	@FXML HBox saturdaytextarea;
	@FXML HBox sundaytextarea;
	
	private String previoustextarea = "jotain";
	private HashMap<Button, HBox> map = new HashMap<Button, HBox>();
	/**public void setCelcius() {
		this.celcius.setText(null);
	}**/
	
	public void logout(MouseEvent event) throws IOException {
		String fxml = "/LoginView.fxml";
		String title  = "Login";
		sceneContent(fxml, event, title);
	}
	
	public void openbox(MouseEvent event) throws IOException {
		final Button btn = (Button) event.getSource();
		new AnimationTimer() {
			private long sleepNanoseconds = 15 * 1000000;
            private long prevTime = 0;
            public void handle(long currentNanoTime) {

                if ((currentNanoTime - prevTime) < sleepNanoseconds) {
                    return;
                }
                for (HBox hbox: map.values()) {
        			if (previoustextarea.equals(hbox.getId())) {
                        hbox.setPrefWidth(hbox.getPrefWidth()-10);
        			}
        		}
                map.get(btn).setPrefWidth(map.get(btn).getPrefWidth()+10);
                if (map.get(btn).getPrefWidth()>=400) {
                	previoustextarea = map.get(btn).getId();
                	stop();
                }
                prevTime = currentNanoTime;
            }
		}.start();
	}
	
	public void createHashMap() {
		//if (map==null) {
			map.put(mondaybutton, mondaytextarea);
			map.put(tuesdaybutton, tuesdaytextarea);
			map.put(wednesdaybutton, wednesdaytextarea);
			map.put(thursdaybutton, thursdaytextarea);
			map.put(fridaybutton, fridaytextarea);
			map.put(saturdaybutton, saturdaytextarea);
			map.put(sundaybutton, sundaytextarea);
			
		//}
	}
	
	public void initialize() {
		createHashMap();
		try {
			WeatherAPICall weather = new WeatherAPICall();
			weatherState.setText(weather.getState());
			weatherCelsius.setText(weather.getCelsius());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}
