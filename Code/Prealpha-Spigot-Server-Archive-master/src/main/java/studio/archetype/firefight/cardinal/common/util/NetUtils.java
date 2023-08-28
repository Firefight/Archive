package studio.archetype.firefight.cardinal.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class NetUtils {
    private static String cachedIp = null;
    private static URL ipCheckUrl;

    static {
        try {
            ipCheckUrl = new URL("https://checkip.amazonaws.com");
        } catch (MalformedURLException ignored) {} // fuck you my url is right
    }

    public static String getIp() throws IOException {
        if (cachedIp != null) return cachedIp;
        else try (BufferedReader in = new BufferedReader(new InputStreamReader(ipCheckUrl.openStream()))) {
            cachedIp = in.readLine();
            return cachedIp;
        }
    }
}
