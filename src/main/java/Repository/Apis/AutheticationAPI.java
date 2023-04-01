package Repository.Apis;

import Entidades.User;
import Repository.local.LocalRepository;
import RepositoryInterface.local.ILocalRepository;
import Util.PreferencesLocal;
import com.google.gson.JsonObject;
import global.Global;
import global.HeaderHttp;
import global.Host;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AutheticationAPI {
    HttpClient httpclient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
    ILocalRepository localRepository = new LocalRepository();
    final URI host = URI.create(Host.HOST_LOCAL);
    public User loguear(String nickname, String contrasenia) {
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
            localRepository.saveCSRFToken(response.headers().allValues("set-cookie").get(0).substring(10, 42));
            localRepository.saveSession(response.headers().allValues("set-cookie").get(0).substring(0, 43)
                    + " " + response.headers().allValues("set-cookie").get(1).substring(0, 42));
            return User.fromJson(response.body());
        }
        } catch (IOException | InterruptedException e) {
            return null;
        }
        return null;
    }

    public User getCurrrentUser(){
        String session = PreferencesLocal.prefs.get(Global.SESSION,"");
        JsonObject jsonRequest = new JsonObject();
        jsonRequest.addProperty("session", session);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequest.toString()))
                .uri(host.resolve("session/getUser"))
                .setHeader(HeaderHttp.CONTENT_TYPE,HeaderHttp.APLICATION_JSON)
                .build();
        try {
            HttpResponse<String> response = httpclient.send(request, HttpResponse.BodyHandlers.ofString());
            User user= response.statusCode()==200? User.fromJson(response.body()) : null;
            return user;
        } catch (IOException | InterruptedException e) {
            return null;
        }
    }

}
