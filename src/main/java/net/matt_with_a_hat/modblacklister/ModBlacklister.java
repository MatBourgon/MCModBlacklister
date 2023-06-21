package net.matt_with_a_hat.modblacklister;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

@Mod(modid = ModBlacklister.MODID, name = ModBlacklister.NAME, version = ModBlacklister.VERSION)
public class ModBlacklister
{
    public static final String MODID = "modblacklister";
    public static final String NAME = "Mod Blacklister";
    public static final String VERSION = "1.0";

    private static final String CONFIG_FILENAME = "modblacklister.cfg";

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) throws IOException
    {
        File f = new File(String.format("%s/%s", event.getModConfigurationDirectory().getCanonicalPath(), CONFIG_FILENAME));
        if (!f.exists())
        {
            if (!f.createNewFile())
            {
                throw new IOException("Failed to create file.");
            }
            BufferedWriter file = new BufferedWriter(new FileWriter(f));
            file.write("# This is a comment, and how you can disable mods.\n");
            file.write("# You can disable mods by writing their mod id.\n");
            file.write("# When a user tries to load a blacklisted mod, it'll give them an explicit warning stating that the mod is blacklisted,\n");
            file.write("# instead of having a weird error occur later during initialization or runtime.\n");
            file.write("# It is imperative that this mod has a hoisted name, so that it occurs as early as possible\n");
            file.write("# during pre-initialization, unless you know the problematic mods fail later.\n");
            file.write("# If you want to leave a comment on why a mod is blacklisted, you can start a line with '#' as I have\n");
            file.write("# and leave an explanation. You cannot add a comment after a modid though, as that will cause issues.\n");
            file.write("\n");

            file.write("# Example disabled mod:\n");
            file.write("modblacklister\n");
            file.close();
        }
        if (f.exists() && !f.isDirectory())
        {
            MBFile file = new MBFile(f);
            Map<String, ModContainer> mods = Loader.instance().getIndexedModList();

            while(!file.isDone())
            {
                String key = file.getNextFilename();
                if (mods.containsKey(key))
                {
                    throw new RuntimeException(String.format("BLACKLISTED MOD DETECTED: %s (%s). Please remove it, or comment out the blacklisting in config/%s by adding a '#' to the start of the mod file name.", mods.get(key).getName(), mods.get(key).getSource().getName(), CONFIG_FILENAME));
                }
            }
        }
    }

}
