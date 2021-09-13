import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class GUI extends Application {

    //Globaalsed muutujad mida on vaja mitmest erinevast kohast kätte saada
    static int[] lahendatavJärjend = null;

    static String meetodiNimi = "";
    static String nimi = "Sisesta oma nimi";

    static List<int[]> salvestus = new ArrayList<>();

    static int tooEtte = 0;


    public static void main(String[] args) {
        launch(args);
    }


    /*
     Meetod, mis muudab failist loetud järjendi täisarvujärjendiks
     */
    static int[] ridaMassiiviks(String rida) {
        String[] tükid = rida.split("[, \\[\\]]");
        List<Integer> ints = new ArrayList<>();
        for (String s : tükid) {
            if (!s.equals("")) {
                ints.add(Integer.parseInt(s));
            }
        }
        int[] massiiv = new int[ints.size()];
        for (int i = 0; i < massiiv.length; i++) {
            massiiv[i] = ints.get(i);
        }
        return massiiv;
    }

    /*
     Meetod, mis saab ette faili kausttee ning valib sealt ühe suvalise rea
     ning muudab selle täisarvujärjendiks
     */
    static int[] valiSuvalineJärjendFailist(String kaustTee) {
        List<String> read = new ArrayList<>();
        try (FileReader reader = new FileReader(kaustTee);
             BufferedReader br = new BufferedReader(reader)) {
            String rida;
            while ((rida = br.readLine()) != null) {
                read.add(rida);
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
        return ridaMassiiviks(read.get(new Random().nextInt(read.size())));
    }

    /*
     Erijuhtum olukorrale, kus ülesanne on kiirmeetodil ette toomisena. Kuna sellisel juhul ei sorteerita järjendit
     vaid tuuakse ette mingi kindel arv väiksemaid arve, peab selle arvu ka kuidagi kätte saama.
     Siin on eeldatud, et failis on read kujul [järjend];Täisarv
     */
    static String ridaFailistTooEtte(String kaustTee) {
        List<String> read = new ArrayList<>();
        try (FileReader reader = new FileReader(kaustTee);
             BufferedReader br = new BufferedReader(reader)) {
            String rida;
            while ((rida = br.readLine()) != null) {
                read.add(rida);
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
        int random = new Random().nextInt(read.size());
        System.out.println(random);
        return read.get(random);
    }

    /*
     Meetod, mis saab kasutaja käest kätte meetodinime, ning seejärel valib õigest failist ühe suvalise järjendi
     */
    static int[] looJärjend(String meetodiNimi) {
        switch (meetodiNimi) {
            case "Mullimeetod":
                return valiSuvalineJärjendFailist("järjendid.txt");
            case "Pistemeetod":
                return valiSuvalineJärjendFailist("järjendid.txt");
            case "Valikmeetod":
                return valiSuvalineJärjendFailist("järjendid.txt");
            case "Kuhjameetod":
                return valiSuvalineJärjendFailist("järjendid.txt");
            case "Kuhjasta":
                return valiSuvalineJärjendFailist("järjendid.txt");
            case "Kiirmeetod":
                return valiSuvalineJärjendFailist("järjendid.txt");
            case "Too ette kiirmeetodil":
                String rida = ridaFailistTooEtte("tooette.txt");
                String[] tükid = rida.split(";");
                tooEtte = Integer.parseInt(tükid[1]);
                return ridaMassiiviks(tükid[0]);
            case "Põimemeetod":
                return valiSuvalineJärjendFailist("järjendid.txt");
            case "Alt ülesse põimemeetod":
                return valiSuvalineJärjendFailist("järjendid.txt");
            default:
                break;
        }
        return new int[]{};
    }

    /*
     Meetod, mis viib programmi tagasi algseisu.
     Nime ei taastata, kuna siis peaks seda kogu aeg uuesti kirjutama
     */
    static void taastaAlgSeisund(String string) {
        lahendatavJärjend = null;

        meetodiNimi = "";
        nimi = string;

        salvestus = new ArrayList<>();

        tooEtte = 0;
    }

    /*
     Meetod, mis viib ette antud täisarvumassiivi graafilisele kujule.
     */
    static GridPane looJärjend(int[] massiiv) {
        GridPane järjend = new GridPane();
        järjend.setHgap(30);
        for (int i = 0; i < massiiv.length; i++) {
            ColumnConstraints column = new ColumnConstraints(50);
            järjend.getColumnConstraints().add(column);
            TextField gap = new TextField("" + massiiv[i]);
            gap.setEditable(false);
            gap.setAlignment(Pos.CENTER);
            järjend.add(gap, i, 0);
        }
        return järjend;
    }

    /*
     Meetod, mis teeb tühjad lüngad kuhu saab sisse kirjutada
     */
    static GridPane sisendLüngad(int pikkus) {
        GridPane lüngad = new GridPane();
        lüngad.setHgap(30);
        for (int i = 0; i < pikkus; i++) {
            ColumnConstraints column = new ColumnConstraints(50);
            lüngad.getColumnConstraints().add(column);
            TextField gap = new TextField("");
            gap.setAlignment(Pos.CENTER);
            lüngad.add(gap, i, 0);
        }
        return lüngad;
    }

    /*
     Meetod, mis muudab programmi vaadet selliseks, et kasutaja saaks sinna lahendust kirjutada
     */
    static VBox looKasutajaSisendVaade(int[] sisend, GridPane lukud) {
        VBox vBox = new VBox();
        vBox.setSpacing(15);

        Text text = new Text("Järjendi seisund: ");
        Text text2 = new Text("Sisesta järgmine samm algoritmis: ");

        GridPane algJärjend = looJärjend(sisend);
        lukud.setPadding(new Insets(-10, 0, 0, 12));
        GridPane kasutajaSisend = sisendLüngad(sisend.length);
        lukud.setPickOnBounds(false);
        vBox.getChildren().addAll(text, algJärjend, text2, kasutajaSisend, lukud);

        return vBox;

    }

    /*
     Meetod, täidab kasutaja eest ära kõik tühjad lahtrid kopeerides need järjendi hetkeseisust.
     */
    static void kopeeriTühjadLahtrid(GridPane grid1, GridPane grid2) {
        for (int i = 0; i < grid1.getChildren().size(); i++) {
            TextField firstGridCell = (TextField) grid1.getChildren().get(i);
            TextField secondGridCell = (TextField) grid2.getChildren().get(i);
            if (secondGridCell.getText().equals("")) {
                secondGridCell.setText(firstGridCell.getText());
            }
        }
    }

    /*
     Meetod, mis muudab graafilisel kujul oleva järjendi täisarvumassiiviks.
     */
    static int[] sisendJärjendiks(GridPane gridPane) {
        int[] järjend = new int[gridPane.getChildren().size()];
        for (int i = 0; i < järjend.length; i++) {
            TextField text = (TextField) gridPane.getChildren().get(i);
            järjend[i] = Integer.parseInt(text.getText());
        }
        return järjend;
    }

    /*
     Meetod mis lisab GridPane kindla suurusega vahed.
     Kasutatakse luku nuppude paigutamiseks
     */
    static void lisaVahed(GridPane gridPane, int lahtreid) {
        for (int i = 0; i < lahtreid; i++) {
            ColumnConstraints column = new ColumnConstraints(80);
            gridPane.getColumnConstraints().add(column);
        }
    }

    /*
     Meetod, mis teeb nupud millega saab kasutaja lahtreid ükshaaval lukku panna ja lahti teha.
     */
    GridPane lukud(int a) {
        GridPane grid = new GridPane();
        for (int i = 0; i < a; i++) {
            Button button = new Button("");
            button.setMinSize(25, 25);
            button.setMaxSize(25, 25);
            button.setAlignment(Pos.CENTER);
            Image img = new Image(getClass().getResource("/images/lock.png").toExternalForm());
            ImageView view = new ImageView(img);
            view.setFitHeight(25);
            view.setPreserveRatio(true);button.setGraphic(view);
            grid.add(button, i, 0);

        }
        return grid;
    }


    /*
     Meetod, mis loob tulemusfaili kasutaja tehtud tööst.
     Failis esimene rida on kujul nimi;meetodinimi;algjärjend(;tooEtte)
     Ning järgnevad read on kasutaja sisestatud algoritmi sammud.
     */
    static void looTulemusFail(String nimi, String meetodiNimi) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(nimi + " - " + meetodiNimi + ".txt"));
        if (meetodiNimi.equals("Too ette kiirmeetodil"))
            writer.write(nimi + ";" + meetodiNimi + ";" + Arrays.toString(lahendatavJärjend) + ";" + tooEtte);
        else writer.write(nimi + ";" + meetodiNimi + ";" + Arrays.toString(lahendatavJärjend));
        for (int[] ints : salvestus) {
            writer.write(Arrays.toString(ints));
        }
        writer.close();
    }

    @Override
    public void start(Stage primaryStage) {

        //Nupud
        Button alusta = new Button("Alusta");
        Button tagasi = new Button("Tagasi");
        tagasi.setDisable(true);
        Button lukusta = new Button("Lukusta väljad");
        Button sisesta = new Button("Järgmine samm");
        Button eelmine = new Button("Eelmine samm");
        Button vabasta = new Button("Vabasta väljad");
        Button lõpeta = new Button("Lõpeta");
        Button kopeeriTühjad = new Button("Kopeeri tühjad lahtrid");

        HBox nupuGrupp = new HBox();
        HBox nupuGrupp2 = new HBox();
        nupuGrupp.getChildren().addAll(kopeeriTühjad, lukusta, vabasta);
        nupuGrupp2.getChildren().addAll(eelmine, sisesta);
        nupuGrupp.setSpacing(15);
        nupuGrupp2.setSpacing(15);


        //Radiobuttonid

        ToggleGroup toggleGroup = new ToggleGroup();

        RadioButton mullimeetod = new RadioButton("Mullimeetod");
        RadioButton kiirmeetod = new RadioButton("Kiirmeetod");
        RadioButton valikmeetod = new RadioButton("Valikmeetod");
        RadioButton kuhjameetod = new RadioButton("Kuhjameetod");
        RadioButton kuhjasta = new RadioButton("Kuhjasta");
        RadioButton pistemeetod = new RadioButton("Pistemeetod");
        RadioButton põimemeetod = new RadioButton("Põimemeetod");
        RadioButton altÜlesse = new RadioButton("Alt ülesse põimemeetod");
        RadioButton tooEtte = new RadioButton("Too ette kiirmeetodil");

        mullimeetod.setToggleGroup(toggleGroup);
        kiirmeetod.setToggleGroup(toggleGroup);
        valikmeetod.setToggleGroup(toggleGroup);
        kuhjameetod.setToggleGroup(toggleGroup);
        kuhjasta.setToggleGroup(toggleGroup);
        pistemeetod.setToggleGroup(toggleGroup);
        põimemeetod.setToggleGroup(toggleGroup);
        altÜlesse.setToggleGroup(toggleGroup);
        tooEtte.setToggleGroup(toggleGroup);
        mullimeetod.setSelected(true);


        //Avavaate loomine
        HBox root = new HBox();
        VBox avaVaade = new VBox();
        HBox avaNupud = new HBox();
        avaNupud.getChildren().addAll(alusta, tagasi);
        avaNupud.setSpacing(15);
        avaNupud.setPadding(new Insets(15, 0, 0, 0));
        TextField nimeVäli = new TextField(nimi);
        mullimeetod.setPadding(new Insets(15, 0, 0, 0));
        avaVaade.getChildren().addAll(nimeVäli, mullimeetod, valikmeetod, pistemeetod, kuhjameetod, kuhjasta, põimemeetod, altÜlesse, kiirmeetod, tooEtte, avaNupud);
        avaVaade.setSpacing(5);
        avaVaade.setPadding(new Insets(15, 0, 0, 15));
        root.getChildren().addAll(avaVaade);


        Scene scene = new Scene(root, 200, 330);
        primaryStage.setY(200);
        primaryStage.setX(600);
        primaryStage.setTitle("Automaatkontroll");
        primaryStage.setScene(scene);
        primaryStage.show();


        GridPane lukuNupud = new GridPane();


        /*
         Funktsioon mis loob kasutajale sisestus vaate.
         Annab ka globaalsetele muutujatele korrektsed väärtused
         */
        alusta.setOnAction(actionEvent -> {
            try {
                RadioButton meetod = (RadioButton) toggleGroup.getSelectedToggle();
                meetodiNimi = meetod.getText();
                nimi = nimeVäli.getText();
                lahendatavJärjend = looJärjend(meetodiNimi);
                primaryStage.setWidth(lahendatavJärjend.length * 80 + 200);
                VBox looKasutajaSisend = looKasutajaSisendVaade(lahendatavJärjend, lukuNupud);
                GridPane lukud = lukud(lahendatavJärjend.length);
                lukuNupud.getChildren().addAll(lukud.getChildren());
                lisaVahed(lukuNupud, lahendatavJärjend.length);
                looKasutajaSisend.setPadding(new Insets(15, 0, 0, 15));
                looKasutajaSisend.getChildren().addAll(nupuGrupp, nupuGrupp2, lõpeta);
                root.getChildren().add(looKasutajaSisend);
                alusta.setDisable(true);
                tagasi.setDisable(false);
                toggleGroup.getToggles().forEach(toggle -> {
                    Node node = (Node) toggle;
                    node.setDisable(true);
                });
            }
            //Kui kasutajal ei ole vajalikke faile antakse talle sellest dialoogiga teada
            catch (Exception e) {
                System.out.println(e);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Viga");
                alert.setHeaderText(null);
                alert.setContentText("Vajalik tekstifail järjenditega puudub");
                alert.showAndWait();

            }

        });

        /*
         Funktsioon mis laseb kasutajal taastada programmi algseisu ilma tulemusfaili loomata
         */
        tagasi.setOnAction(e -> {
            taastaAlgSeisund(nimeVäli.getText());
            GUI app = new GUI();
            app.start(primaryStage);
            primaryStage.setWidth(200);
        });

        /*
         Funktsioon mis kopeerib kõik tühjad lahtrid kasutajale järjendi hetkeseisust
         */
        kopeeriTühjad.setOnAction(actionEvent -> {
            VBox sisendiVaade = (VBox) root.getChildren().get(1);
            GridPane järjend = (GridPane) sisendiVaade.getChildren().get(1);
            GridPane kasutajaLahtrid = (GridPane) sisendiVaade.getChildren().get(3);
            kopeeriTühjadLahtrid(järjend, kasutajaLahtrid);
        });

        /*
         Funktsioon, mis laseb kasutajal sisesta algoritmi sammu,
         Kontrollib kas kasutaja on jätnud lünga tühjaks, kirjutanud sinna sümboli mis ei ole number, või kirjutanud
         sinna numbri, mida algses järjendis ei leidu, ning teavitab teda sellest.
         */
        sisesta.setOnAction(actionEvent -> {
            try {
                VBox sisendiVaade = (VBox) root.getChildren().get(1);
                GridPane järjendiSeis = (GridPane) sisendiVaade.getChildren().get(1);
                GridPane kasutajaLahtrid = (GridPane) sisendiVaade.getChildren().get(3);
                int[] järjend = sisendJärjendiks(järjendiSeis);
                int[] kasutajaSisend = sisendJärjendiks(kasutajaLahtrid);

                int[] võrlde1 = järjend.clone();
                int[] võrlde2 = kasutajaSisend.clone();
                Arrays.sort(võrlde1);
                Arrays.sort(võrlde2);
                if (!Arrays.equals(võrlde1, võrlde2)) {
                    throw new IllegalArgumentException();
                }

                GridPane uuenda = looJärjend(kasutajaSisend);
                sisendiVaade.getChildren().remove(1);
                sisendiVaade.getChildren().add(1, uuenda);

                for (Node child : kasutajaLahtrid.getChildren()) {
                    TextField text = (TextField) child;
                    if (!text.isDisabled()) {
                        text.setText("");
                    }
                }
                kasutajaLahtrid.getChildren().get(0).requestFocus();
                salvestus.add(kasutajaSisend);
            } catch (NumberFormatException a) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Viga");
                alert.setHeaderText(null);
                alert.setContentText("Vigane sisend lahtris");
                alert.showAndWait();
            } catch (IllegalArgumentException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Viga");
                alert.setHeaderText(null);
                alert.setContentText("Sisestati number mida järjendis ei leidu");
                alert.showAndWait();
            }
        });

        /*
         Funktsioon, mis laseb kasutajal minna tagasi, kui ta märkab, et ta on teinud vea
         */
        eelmine.setOnAction(actionEvent -> {
            VBox sisendiVaade = (VBox) root.getChildren().get(1);
            GridPane järjendiSeis = (GridPane) sisendiVaade.getChildren().get(1);
            GridPane kasutajaLahtrid = (GridPane) sisendiVaade.getChildren().get(3);

            try {
                int[] viimane = salvestus.get(salvestus.size() - 2);
                for (int i = 0; i < viimane.length; i++) {
                    TextField lahter = (TextField) järjendiSeis.getChildren().get(i);
                    lahter.setText(String.valueOf(viimane[i]));
                }
                for (Node child : kasutajaLahtrid.getChildren()) {
                    TextField text = (TextField) child;
                    text.setText("");
                }
                salvestus.remove(salvestus.size() - 1);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Viga");
                alert.setHeaderText(null);
                alert.setContentText("Eelmist sammu ei leidu");
                alert.showAndWait();
            }


        });

        /*
         Funktsioon, mis lukustab kõik lahtrid kuhu on hetkel midagi sisse kirjutatud
         */
        lukusta.setOnAction(actionEvent -> {
            VBox sisendiVaade = (VBox) root.getChildren().get(1);

            GridPane kasutajaLahtrid = (GridPane) sisendiVaade.getChildren().get(3);
            for (Node child : kasutajaLahtrid.getChildren()) {
                TextField text = (TextField) child;
                if (!text.getText().equals("")) {
                    text.setDisable(true);
                }
            }
        });

        /*
         Funktsioon, mis laseb lahtreid ükshaaval lukku panna ja lukust lahti teha
         */
        lukuNupud.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
            VBox sisendiVaade = (VBox) root.getChildren().get(1);
            GridPane kasutajaLahtrid = (GridPane) sisendiVaade.getChildren().get(3);
            String clickedClass = mouseEvent.getTarget().getClass().getTypeName();
            if (clickedClass.equals("javafx.scene.control.Button")) {
                Node node = (Node) mouseEvent.getTarget();
                int indeks = GridPane.getColumnIndex(node);
                TextField textField = (TextField) kasutajaLahtrid.getChildren().get(indeks);
                textField.setDisable(!textField.isDisabled());
            }
        });

        /*
         Funktsioon, mis teeb kõik lahtrid lukust lahti
         */
        vabasta.setOnAction(actionEvent -> {
            VBox sisendiVaade = (VBox) root.getChildren().get(1);
            GridPane kasutajaLahtrid = (GridPane) sisendiVaade.getChildren().get(3);
            for (Node child : kasutajaLahtrid.getChildren()) {
                TextField text = (TextField) child;
                text.setDisable(false);
            }
        });

        /*
         Funktsioon, mis loob tulemusfaili ning viib programmi tagasi algkujule.
         Nime ei taastata.
         */
        lõpeta.setOnAction(actionEvent -> {
            try {
                looTulemusFail(nimi, meetodiNimi);
            } catch (IOException e) {
                e.printStackTrace();
            }
            GUI app = new GUI();
            app.start(primaryStage);
            taastaAlgSeisund(nimeVäli.getText());
            primaryStage.setWidth(200);
        });

    }

}


