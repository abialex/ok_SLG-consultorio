package Util;
import global.Global;

import java.util.prefs.Preferences;
public class PreferencesLocal {
    public static Preferences prefs = Preferences.userRoot().node(Global.LOCAL);

}
