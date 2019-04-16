package sovellus;

import javafx.application.Application;
import javafx.scene.Scene;
import static javafx.application.Application.launch;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SaastolaskuriSovellus extends Application {
    
    @Override
    public void start(Stage ikkuna){
        
        Insets insets = new Insets(15);        
        BorderPane ylatasonAsettelu = new BorderPane();        
        VBox ylakerta = new VBox();
        ylakerta.setSpacing(15);
        
        // kuukausittainen tallennus
        BorderPane tallennus = new BorderPane();        
        tallennus.setLeft(new Label("Kuukausittainen tallennus"));
        
        Slider tallennusSlider = new Slider(25, 250, 0);
        tallennusSlider.setShowTickMarks(true);
        tallennusSlider.setShowTickLabels(true);
        tallennusSlider.setMajorTickUnit(25f);
        tallennusSlider.setBlockIncrement(5f);
 
        tallennus.setCenter(tallennusSlider);
        Label tallennusLabel = new Label("0.0");
        tallennus.setRight(tallennusLabel);        
        
        // vuosittainen korko
        BorderPane korko = new BorderPane();
        korko.setLeft(new Label("Vuosittainen korko"));
        
        Slider korkoSlider = new Slider(0, 10, 0);
        korkoSlider.setShowTickMarks(false);
        korkoSlider.setShowTickLabels(true);
        korkoSlider.setMajorTickUnit(10f);
        korkoSlider.setBlockIncrement(0.1f);
        
        korko.setCenter(korkoSlider);
        Label korkoLabel = new Label("0");
        korko.setRight(korkoLabel);
        
        ylakerta.getChildren().addAll(tallennus, korko);
                
        ylatasonAsettelu.setTop(ylakerta);
        BorderPane.setMargin(ylakerta, insets);
        
        NumberAxis xAkseli = new NumberAxis(0, 30, 1);
        NumberAxis yAkseli = new NumberAxis();
        yAkseli.setAutoRanging(true);
        LineChart<Number, Number> viivakaavio = new LineChart<>(xAkseli, yAkseli);
        viivakaavio.setLegendVisible(false);
        viivakaavio.setTitle("Säästölaskuri");
        
        XYChart.Series savings = new XYChart.Series();
        XYChart.Series korkoineen = new XYChart.Series();
        
        tallennusSlider.setOnMouseReleased(e -> {
            tallennusLabel.setText(String.format("%.0f", tallennusSlider.getValue()));
            laskeArvot(tallennusSlider.getValue(), korkoSlider.getValue(), savings, korkoineen);
        });

        korkoSlider.setOnMouseReleased(e -> {
            korkoLabel.setText(String.format("%.2f", korkoSlider.getValue()));
            laskeArvot(tallennusSlider.getValue(), korkoSlider.getValue(), savings, korkoineen);
        });

        viivakaavio.getData().add(savings);
        viivakaavio.getData().add(korkoineen);
        
        ylatasonAsettelu.setCenter(viivakaavio);
        
        Scene nakyma = new Scene(ylatasonAsettelu);
        ikkuna.setScene(nakyma);
        ikkuna.setMinWidth(700);
        ikkuna.show();
    }

    public static void main(String[] args) {
        launch(SaastolaskuriSovellus.class);
    }
    
    private void laskeArvot(double kktalletus, double korko, XYChart.Series savings, XYChart.Series korkoineen) {
        savings.getData().clear();
        korkoineen.getData().clear();

        savings.getData().add(new XYChart.Data(0, 0));
        korkoineen.getData().add(new XYChart.Data(0, 0));

        double edellinen = 0;

        for (int i = 1; i <= 30; i++) {
            savings.getData().add(new XYChart.Data(i, i * kktalletus * 12));

            edellinen += kktalletus * 12;
            edellinen *= (1 + korko / 100.0);

            korkoineen.getData().add(new XYChart.Data(i, edellinen));
        }
    }
}