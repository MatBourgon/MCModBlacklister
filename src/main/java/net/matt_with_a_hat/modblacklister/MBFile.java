package net.matt_with_a_hat.modblacklister;

import javax.annotation.Nullable;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MBFile {
    private final List<String> blacklistedModsList = new ArrayList<>();
    private int fileNameIndex = 0;

    @Nullable String getNextFilename()
    {
        if (!isDone())
        {
            ++fileNameIndex;
            return blacklistedModsList.get(fileNameIndex - 1);
        }
        return null;
    }

    public boolean isDone()
    {
        return fileNameIndex == blacklistedModsList.size();
    }

    public MBFile(File file) throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(file));

        String[] EXCLUDED_MODS = {
                "minecraft",        // Required to run
                "mcp",              // Required for mod dev
                "FML",              // Forge
                "forge",            // Forge
                "modblacklister"    // Why would you disable the mod itself?
        };
        List<String> excludedList = Arrays.asList(EXCLUDED_MODS);

        while (true)
        {
            String line = br.readLine();
            if (line == null)
            {
                break;
            }

            if (!line.startsWith("\n") && !line.startsWith("\r") && !line.startsWith("#") && !excludedList.contains(line))
            {
                blacklistedModsList.add(line);
            }
        }
        br.close();
        Collections.sort(blacklistedModsList);
    }
}
