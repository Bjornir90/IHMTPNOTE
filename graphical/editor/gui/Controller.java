package graphical.editor.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Controller {

	private RadioButton currentlySelected;
	private Paint currentPaint;
	private Shape currentShape;
	private boolean justChanged;

	@FXML
	RadioButton rectangleButton;
	@FXML
	RadioButton ellipsisButton;
	@FXML
	RadioButton selectButton;
	@FXML
	RadioButton lineButton;
	@FXML
	Button deleteButton;
	@FXML
	Button cloneButton;
	@FXML
	ColorPicker colorPicker;
	@FXML
	Pane drawingPane;
	@FXML
	ToggleGroup action;

	@FXML
	public void initialize(){
		deleteButton.setDisable(true);
		cloneButton.setDisable(true);
		action.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
			public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {

				if (action.getSelectedToggle() != null) {

					RadioButton selected = (RadioButton) action.getSelectedToggle();
					System.out.println("selected.getId() = " + selected.getId());
					setCurrentlySelected(selected);
					if (selected.getId().equals("selectButton")){
						deleteButton.setDisable(false);
						cloneButton.setDisable(false);
					} else {
						deleteButton.setDisable(true);
						cloneButton.setDisable(true);
					}


				}

			}
		});

		drawingPane.setOnMouseClicked(event -> {
			System.out.println("Click detected : "+ event.getSceneX() +" "+ event.getSceneY() + "Source : "+event.getSource());
			if(currentlySelected.getId().equals("selectButton") && currentShape != null && !justChanged) {
				System.out.println("Moving currentShape = " + currentShape);
				currentShape.relocate(event.getSceneX() - drawingPane.getLayoutX(), event.getSceneY() - drawingPane.getLayoutY());
				currentShape = null;
			}
			if(justChanged) justChanged = false;//To ignore the click that selected the shape
			spawnShape(event.getSceneX(), event.getSceneY());
		});

		colorPicker.setOnAction(event -> {
			setCurrentPaint(colorPicker.getValue());
		});

		deleteButton.setOnMouseClicked(event -> {
			if(currentShape != null){
				drawingPane.getChildren().remove(currentShape);
			}
		});

		cloneButton.setOnMouseClicked(event -> {
			if(currentShape != null){

			}
		});
	}

	public void setCurrentlySelected(RadioButton currentlySelected) {
		this.currentlySelected = currentlySelected;
	}

	public void setCurrentPaint(Paint currentPaint) {
		this.currentPaint = currentPaint;
	}

	public void setCurrentShape(Shape currentShape) {
		this.currentShape = currentShape;
	}

	private void spawnShape(double x, double y){
		switch(currentlySelected.getId()){
			case "lineButton":
				Line line = new Line();
				line.setStartX(x - line.getLayoutBounds().getMinX() - drawingPane.getLayoutX());
				line.setStartY(y - line.getLayoutBounds().getMinY() - drawingPane.getLayoutY());
				line.setEndX(x+30 - line.getLayoutBounds().getMinX() - drawingPane.getLayoutX());
				line.setEndY(y - line.getLayoutBounds().getMinY() - drawingPane.getLayoutY());
				line.setStroke(currentPaint);
				line.setOnMouseClicked(this::selectShape);
				drawingPane.getChildren().add(line);
				break;
			case "rectangleButton":
				System.out.println("Spawning rectangle");
				Rectangle rectangle = new Rectangle();
				rectangle.setHeight(35);
				rectangle.setWidth(60);
				rectangle.setLayoutX(x - rectangle.getLayoutBounds().getMinX() - drawingPane.getLayoutX());
				rectangle.setLayoutY(y - rectangle.getLayoutBounds().getMinY() - drawingPane.getLayoutY());
				rectangle.setFill(currentPaint);
				rectangle.setOnMouseClicked(this::selectShape);
				drawingPane.getChildren().add(rectangle);
				break;
			case "ellipsisButton":
				Ellipse ellipse = new Ellipse();
				ellipse.setCenterX(x - ellipse.getLayoutBounds().getMinX() - drawingPane.getLayoutX());
				ellipse.setCenterY(y - ellipse.getLayoutBounds().getMinY() - drawingPane.getLayoutY());
				ellipse.setRadiusX(30);
				ellipse.setRadiusY(20);
				ellipse.setFill(currentPaint);
				ellipse.setOnMouseClicked(this::selectShape);
				drawingPane.getChildren().add(ellipse);
				break;
		}
	}

	public void selectShape(MouseEvent event){
		Shape shape = (Shape) event.getSource();
		shape.setFill(currentPaint);
		setCurrentShape(shape);
		justChanged = true;
		System.out.println("shape = " + shape);
	}
}
