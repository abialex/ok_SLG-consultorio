package Repository.Apis;

import Entidades.User;
import Repository.local.LocalRepository;
import RepositoryInterface.local.ILocalRepository;
import Util.AlertMessage;
import com.google.gson.JsonObject;
import global.HeaderHttp;
import global.Host;
import javafx.application.Platform;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AutheticationAPI {
    HttpClient httpclient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
    ILocalRepository IlocalRepository = new LocalRepository();
    final URI host = URI.create(Host.HOST_LOCAL);
    public User login(String nickname, String contrasenia) {
        try {

        JsonObject jsonRequest = new JsonObject();
        jsonRequest.addProperty("username", nickname);
        jsonRequest.addProperty("password", contrasenia);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequest.toString()))
                .uri(host.resolve("session/loguear"))
                .setHeader(HeaderHttp.CONTENT_TYPE, HeaderHttp.APLICATION_JSON)
                .build();

        HttpResponse<String> response = httpclient.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() == 200){
            IlocalRepository.saveCSRFToken(response.headers().allValues("set-cookie").get(0).substring(10, 42));
            IlocalRepository.saveCokkie(response.headers().allValues("set-cookie").get(0).substring(0, 43)
                    + " " + response.headers().allValues("set-cookie").get(1).substring(0, 42));
            return User.fromJson(response.body());
        }
        } catch (IOException | InterruptedException e) {
            Platform.runLater(() -> {
                AlertMessage.mostrar_alerta_error("Conexión", "Sin conexión al servidor");
            });
            return null;
        }
        return null;
    }
    public boolean logout() {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(host.resolve("session/CerrarSesion"))
                .setHeader(HeaderHttp.CONTENT_TYPE,HeaderHttp.APLICATION_JSON)
                .setHeader(HeaderHttp.CSRFToken,IlocalRepository.getCSRFToken())
                .setHeader(HeaderHttp.COOKIE,IlocalRepository.getCookie())
                .build();
        try {
            HttpResponse<String> response = httpclient.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() == 200) {
                IlocalRepository.closeSession();
            }
            return response.statusCode() == 200;
        }
        catch (IOException | InterruptedException e) {
            Platform.runLater(() -> {
                AlertMessage.mostrar_alerta_error("Conexión", "Sin conexión al servidor");
            });
            return false;
        }
    }
    public User getCurrrentUser(){
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(host.resolve("session/getUser"))
                .setHeader(HeaderHttp.CONTENT_TYPE,HeaderHttp.APLICATION_JSON)
                .setHeader(HeaderHttp.CSRFToken,IlocalRepository.getCSRFToken())
                .setHeader(HeaderHttp.COOKIE,IlocalRepository.getCookie())
                .build();
        try {
            HttpResponse<String> response = httpclient.send(request, HttpResponse.BodyHandlers.ofString());
            User user= response.statusCode()==200? User.fromJson(response.body()) : null;
            return user;
        } catch (IOException | InterruptedException e) {
            Platform.runLater(() -> {
                AlertMessage.mostrar_alerta_error("Conexión", "Sin conexión al servidor");
            });
            return null;
        }
    }

}
