package RepositoryInterface.local;

public interface ILocalRepository {
    public void saveCSRFToken(String CSRFToken);
    public void saveCokkie(String Cokkie);
    public void  saveUser(String username);
    public void saveUserName(String username);
    public String getUserName();
    public String getCSRFToken();
    public String getCookie();
    public boolean clear();
    public void closeSession();
}
