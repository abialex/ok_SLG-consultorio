package Repository.local;

import RepositoryInterface.local.ILocalRepository;
import Util.PreferencesLocal;
import global.Global;
import global.HeaderHttp;

public class LocalRepository implements ILocalRepository {
    @Override
    public void saveSession(String session) {
        PreferencesLocal.prefs.put(HeaderHttp.COOKIE,session);
    }

    @Override
    public void saveCSRFToken(String CSRFToken) {
        PreferencesLocal.prefs.put(Global.CSRFToken,CSRFToken);

    }
    @Override
    public void saveUser(String username) {
        PreferencesLocal.prefs.put(Global.USERNAME,username);
    }

    @Override
    public void saveUserName(String username) {
        PreferencesLocal.prefs.put(Global.CSRFToken,username);
    }
}
