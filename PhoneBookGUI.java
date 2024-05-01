import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.*;

public class PhoneBookGUI extends Application {
    private Map<String, List<String>> phoneBook;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        phoneBook = new HashMap<>();

        // Создание элементов интерфейса
        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        Label numberLabel = new Label("Number:");
        TextField numberField = new TextField();
        Button addButton = new Button("Add Contact");
        Button viewNamesButton = new Button("View All Names");
        Button viewNumbersButton = new Button("View All Numbers");
        ListView<String> listView = new ListView<>();

        // Добавление действий кнопке "Add Contact"
        addButton.setOnAction(event -> {
            String name = nameField.getText();
            String number = numberField.getText();
            if (!name.isEmpty() && !number.isEmpty()) {
                addContact(name, number);
                updateListView(listView);
                nameField.clear();
                numberField.clear();
            }
        });

        // Добавление действий кнопке "View All Names"
        viewNamesButton.setOnAction(event -> {
            showAllNames();
        });

        // Добавление действий кнопке "View All Numbers"
        viewNumbersButton.setOnAction(event -> {
            String selectedName = listView.getSelectionModel().getSelectedItem();
            if (selectedName != null) {
                String name = selectedName.split(":")[0].trim();
                showAllNumbers(name);
            }
        });

        // Настройка списка контактов
        updateListView(listView);

        // Расположение элементов в вертикальном контейнере
        VBox root = new VBox(10, nameLabel, nameField, numberLabel, numberField, addButton, viewNamesButton, viewNumbersButton, listView);
        root.setPadding(new Insets(10));
        Scene scene = new Scene(root, 300, 300);

        // Отображение окна приложения
        primaryStage.setScene(scene);
        primaryStage.setTitle("Phone Book");
        primaryStage.show();
    }

    // Метод для добавления контакта
    private void addContact(String name, String number) {
        phoneBook.computeIfAbsent(name, k -> new ArrayList<>()).add(number);
    }

    // Метод для обновления списка контактов
    private void updateListView(ListView<String> listView) {
        listView.getItems().clear();
        List<Map.Entry<String, List<String>>> sortedEntries = new ArrayList<>(phoneBook.entrySet());
        sortedEntries.sort(Comparator.comparing(entry -> -entry.getValue().size())); // Сортировка по убыванию числа телефонов
        for (Map.Entry<String, List<String>> entry : sortedEntries) {
            listView.getItems().add(entry.getKey());
        }
    }

    // Метод для просмотра всех имен
    private void showAllNames() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("All Names");
        alert.setHeaderText(null);
        StringBuilder content = new StringBuilder();
        for (String name : phoneBook.keySet()) {
            content.append(name).append("\n");
        }
        alert.setContentText(content.toString());
        alert.showAndWait();
    }

    // Метод для просмотра всех номеров выбранного имени
    private void showAllNumbers(String name) {
        List<String> numbers = phoneBook.get(name);
        if (numbers != null && !numbers.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("All Numbers for " + name);
            alert.setHeaderText(null);
            StringBuilder content = new StringBuilder();
            for (String number : numbers) {
                content.append(number).append("\n");
            }
            alert.setContentText(content.toString());
            alert.showAndWait();
        }
    }
}
