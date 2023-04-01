package RepositoryInterface.local;

public interface ILocalRepository {
    public void saveSession(String session);
    public void saveCSRFToken(String CSRFToken);
    public void  saveUser(String username);
    public void saveUserName(String username);
}
