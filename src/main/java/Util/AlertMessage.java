package Util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class AlertMessage {
    public static void mostrar_alerta_warning(String title,String mensaje) {
        Alert alertWarning = new Alert(Alert.AlertType.WARNING);
        alertWarning.setHeaderText(null);
        alertWarning.setTitle(title);
        alertWarning.setContentText(mensaje);
        alertWarning.showAndWait();
    }

    public static void mostrar_alerta_success(String title,String mensaje) {
        Alert alertWarning = new Alert(Alert.AlertType.INFORMATION);
        alertWarning.setHeaderText(null);
        alertWarning.setTitle(title);
        alertWarning.setContentText(mensaje);
        alertWarning.showAndWait();
    }

    public static void mostrar_alerta_error(String title,String mensaje) {
        Alert alertWarning = new Alert(Alert.AlertType.ERROR);
        alertWarning.setHeaderText(null);
        alertWarning.setTitle(title);
        alertWarning.setContentText(mensaje);
        alertWarning.showAndWait();
    }

    public static Optional<ButtonType> mostrar_confirmación(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);

        return alert.showAndWait();
    }
}
