package Repository.local;

import RepositoryInterface.local.ILocalRepository;
import global.Global;
import global.HeaderHttp;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class LocalRepository implements ILocalRepository {
    public  Preferences preferences = Preferences.userRoot().node(Global.LOCAL);
    @Override
    public void saveCSRFToken(String CSRFToken) {
        preferences.put(HeaderHttp.CSRFToken,CSRFToken);

    }
    @Override
    public void saveCokkie(String Cokkie) {
        preferences.put(HeaderHttp.COOKIE,Cokkie);
    }

    @Override
    public void saveUser(String username) {
        preferences.put(Global.USERNAME,username);
    }

    @Override
    public void saveUserName(String username) {
        preferences.put(Global.USERNAME,username);
    }

    @Override
    public String getUserName() {
        return preferences.get(Global.USERNAME,"");
    }

    @Override
    public String getCSRFToken() {
        return preferences.get(HeaderHttp.CSRFToken,"");
    }

    @Override
    public String getCookie() {
       return preferences.get(HeaderHttp.COOKIE,"");
    }

    @Override
    public boolean clear() {
        try {
            preferences.clear();
            return true;
        } catch (BackingStoreException e) {
            return false;
        }
    }

    @Override
    public void closeSession() {
        preferences.put(HeaderHttp.CSRFToken,"");
        preferences.put(HeaderHttp.COOKIE,"");
    }
}
