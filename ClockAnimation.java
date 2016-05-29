import javafx.application.Application;
import javafx.stage.Stage;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;
import java.io.IOException;
import java.util.Calendar; 
import java.util.GregorianCalendar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
public class ClockAnimation extends Application {
	@Override
	public void start(Stage primaryStage) throws IOException {
		ClockPane clock = new ClockPane();
		EventHandler<ActionEvent> eventHandler = e -> {
			clock.setCurrentTime();
		};
		Timeline animation = new Timeline(new KeyFrame(Duration.millis(1000), eventHandler));
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.play();
		Button cloc = new Button("clock");
		Button time = new Button("time");
		HBox hBox = new HBox(cloc, time);
		cloc.setOnAction(e -> primaryStage.setScene(new Scene(new BorderPane(clock, null, null, hBox, null),200,200)));
		time.setOnAction(e -> primaryStage.setScene(new Scene(new BorderPane(new DigitalClock(), null, null, hBox, null),200,200)));
		BorderPane borderPane = new BorderPane(clock, null, null, hBox, null);
		primaryStage.setScene(new Scene(borderPane,200,200));
		primaryStage.setTitle("ClockAnimation");
		primaryStage.show();
	}
	public static void main(String[] args) {
		launch(args);
	}
}
class ClockPane extends Pane {
	private int hour;
	private int minute;
	private int second;
	public ClockPane() {
		setCurrentTime();
	}
	public ClockPane(int hour, int minute, int second) {
		this.hour = hour;
		this.minute = minute;
		this.second = second;
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
		paintClock();
	}
	public int getMinute() {
		return minute;
	}
	public void setMinute(int minute) {
		this.minute = minute;
		paintClock();
	}
	public int getSecond() {
		return second;
	}
	public void setSecond(int second) {
		this.second = second;
		paintClock();
	}
	public void setCurrentTime() {
		Calendar calendar = new GregorianCalendar();
		this.hour = calendar.get(Calendar.HOUR_OF_DAY);
		this.minute = calendar.get(Calendar.MINUTE);
		this.second = calendar.get(Calendar.SECOND);
	    paintClock();
	}
	private void paintClock() {
		double clockRadius = Math.min(getWidth(), getHeight()) * 0.8 * 0.5;
		double centerX = getWidth() / 2;
		double centerY = getHeight() / 2;
		Circle circle = new Circle(centerX, centerY, clockRadius);
		circle.setFill(Color.WHITE);
		circle.setStroke(Color.BLACK);
		Text t1 = new Text(centerX - 5, centerY - clockRadius + 12, "12");
		Text t2 = new Text(centerX - clockRadius + 3, centerY + 5, "9");
		Text t3 = new Text(centerX + clockRadius - 10, centerY + 3, "3");
		Text t4 = new Text(centerX - 3, centerY + clockRadius - 3, "6");
		double sLength = clockRadius * 0.8;
		double secondX = centerX + sLength * Math.sin(second * (2 * Math.PI / 60));
	    double secondY = centerY - sLength * Math.cos(second * (2 * Math.PI / 60));
	    Line sLine = new Line(centerX, centerY, secondX, secondY);
	    sLine.setStroke(Color.RED);
	    double mLength = clockRadius * 0.65;
	    double xMinute = centerX + mLength * Math.sin(minute * (2 * Math.PI / 60));
	    double minuteY = centerY - mLength * Math.cos(minute * (2 * Math.PI / 60));
	    Line mLine = new Line(centerX, centerY, xMinute, minuteY);
	    mLine.setStroke(Color.BLUE);
	    double hLength = clockRadius * 0.5;
	    double hourX = centerX + hLength * Math.sin((hour % 12 + minute / 60.0) * (2 * Math.PI / 12));
	    double hourY = centerY - hLength * Math.cos((hour % 12 + minute / 60.0) * (2 * Math.PI / 12));
	    Line hLine = new Line(centerX, centerY, hourX, hourY);
	    hLine.setStroke(Color.GREEN);
	    getChildren().clear();  
	    getChildren().addAll(circle, t1, t2, t3, t4, sLine, mLine, hLine);
	}
	@Override
	public void setWidth(double width) {
		super.setWidth(width);
		paintClock();
	}
	@Override
	public void setHeight(double height) {
		super.setHeight(height);
		paintClock();
	}
}
class DigitalClock extends Label {
	public DigitalClock() {
		bindToTime();
	}
	private void bindToTime() {
		Timeline timeline = new Timeline(
			new KeyFrame(Duration.seconds(0),
				new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent actionEvent) {
							Calendar time = Calendar.getInstance();
							String hourString = StringUtilities.pad(2, ' ', time.get(Calendar.HOUR) == 0 ? "12" : time.get(Calendar.HOUR) + "");
							String minuteString = StringUtilities.pad(2, '0', time.get(Calendar.MINUTE) + "");
							String secondString = StringUtilities.pad(2, '0', time.get(Calendar.SECOND) + "");
							String ampmString = time.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";
							setText(hourString + ":" + minuteString + ":" + secondString + " " + ampmString);
						}
				}
			),
			new KeyFrame(Duration.seconds(1))
		);
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}
}
class StringUtilities {
	public static String pad(int fieldWidth, char padChar, String s) {
		StringBuilder sb = new StringBuilder();
		for (int i = s.length(); i < fieldWidth; i++) {
			sb.append(padChar);
		}
		sb.append(s);
		return sb.toString();
	}
}