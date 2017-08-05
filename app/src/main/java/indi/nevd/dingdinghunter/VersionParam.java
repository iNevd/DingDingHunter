package indi.nevd.dingdinghunter;

/**
 * Created by nevd on 8/1/2017.
 */

public class VersionParam {
    public static String MessageData = "";

    public static void init(String version) {
        switch (version) {
            case "3.3.0":
            case "3.3.1":
                MessageData = "cuw";
                break;
            case "3.3.3":
            case "3.3.5":
                MessageData = "cwz";
                break;
            case "3.4.0":
                MessageData = "cuy";
                break;
            case "3.4.6":
                MessageData = "cxw";
                break;
            case "3.4.8":
            case "3.4.10":
                MessageData = "dey";
                break;
            case "3.5.0":
                MessageData = "dtk";
                break;
            case "3.5.1":
                MessageData = "ekb";
                break;
            case "3.5.2":
                MessageData = "eli";
                break;
            default:
                break;
        }
    }
}
