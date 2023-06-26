package net.matt_with_a_hat.modblacklister;

import javax.annotation.Nullable;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MBFile {
    protected static class ModInfo implements Comparable<ModInfo>
    {
        public String modid;
        public String comment;
        public ModInfo(String line)
        {
            if (line.contains("#"))
            {
                modid = line.substring(0, line.indexOf('#')).trim();
                comment = line.substring(line.indexOf('#') + 1).trim();
            }
            else
            {
                modid = line.trim();
                comment = "This mod has been blacklisted.";
            }
        }

        @Override
        public int compareTo(ModInfo o) {
            return this.modid.compareTo(o.modid);
        }
    }
    private final List<ModInfo> blacklistedModsList = new ArrayList<>();
    private int listIndex = 0;

    @Nullable ModInfo getNextMod()
    {
        if (!isDone())
        {
            ++listIndex;
            return blacklistedModsList.get(listIndex - 1);
        }
        return null;
    }

    public boolean isDone()
    {
        return listIndex == blacklistedModsList.size();
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

            line = line.trim();

            if (!line.isEmpty() && !line.startsWith("#") && !excludedList.contains(line))
            {
                blacklistedModsList.add(new ModInfo(line));
            }
        }
        br.close();
        Collections.sort(blacklistedModsList);
    }
}
