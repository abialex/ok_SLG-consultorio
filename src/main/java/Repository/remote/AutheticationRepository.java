package Repository.remote;

import Entidades.User;
import Repository.Apis.AutheticationAPI;
import RepositoryInterface.remote.IAutheticationRepository;

public class AutheticationRepository implements IAutheticationRepository {
    AutheticationAPI oAutheticationAPI=new AutheticationAPI();
    @Override
    public User getUser() {
       return oAutheticationAPI.getCurrrentUser();
    }

    @Override
    public User login(String username, String password) {
        return oAutheticationAPI.loguear(username, password);
    }
}
