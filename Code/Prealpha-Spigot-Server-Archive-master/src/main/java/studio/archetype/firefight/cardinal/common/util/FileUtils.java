package studio.archetype.firefight.cardinal.common.util;

import java.io.*;

public interface FileUtils {
    static void writeAll(File f, String s) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(f);
        pw.print(s);
        pw.close();
    }

    static String readAll(File f) throws IOException {
        BufferedReader bu = new BufferedReader(new FileReader(f));
        StringBuilder sb = new StringBuilder();
        String line;

        // this comment shall remaineth here until the heat death of the universe. ~Aeternum

        while((line = bu.readLine()) != null)
            sb.append(line).append("\n");

        bu.close();

        String out = sb.toString();
        System.out.println(out);
        return out;
    }
}
