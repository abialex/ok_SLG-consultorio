package RepositoryInterface.remote;

import Entidades.User;

public interface IAutheticationRepository {
    public  User getUser();
    public  User login(String username, String password);
    public boolean logout();
}
