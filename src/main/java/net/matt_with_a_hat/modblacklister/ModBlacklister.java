package net.matt_with_a_hat.modblacklister;

import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mod(modid = ModBlacklister.MODID, name = ModBlacklister.NAME, version = ModBlacklister.VERSION)
public class ModBlacklister
{
    public static final String MODID = "modblacklister";
    public static final String NAME = "Mod Blacklister";
    public static final String VERSION = "1.1";
    private static final String CONFIG_FILENAME = "modblacklister.cfg";
    private List<ModInfo> blacklistedMods = new ArrayList<>();

    protected static class ModInfo
    {
        public String modName;
        public String modFileName;
        public @Nullable String modComment;

        public ModInfo(String modName, String modFileName, String comment)
        {
            this.modName = modName;
            this.modFileName = modFileName;
            this.modComment = comment;
        }
    }

    private @Nullable MBFile GetBlacklistFile(File configFile) throws IOException
    {
        if (!configFile.exists())
        {
            CreateBlacklistFile(configFile);
        }

        if (configFile.exists() && !configFile.isDirectory())
        {
            return new MBFile(configFile);
        }

        return null;
    }

    @EventHandler
    @SideOnly(Side.CLIENT)
    public void onConstruct(FMLConstructionEvent event) throws IOException
    {
        Map<String, ModContainer> mods = Loader.instance().getIndexedModList();
        MBFile file = GetBlacklistFile(new File(Loader.instance().getConfigDir(), CONFIG_FILENAME));

        if (file == null) return;

        while (!file.isDone())
        {
            MBFile.ModInfo mf = file.getNextMod();
            ModContainer mod = mods.get(mf.modid);
            if (mod != null)
            {
                blacklistedMods.add(new ModInfo(mod.getName(), mod.getSource().getName(), mf.comment));
            }
        }

    }

    @EventHandler
    @SideOnly(Side.CLIENT)
    public void preInit(FMLPreInitializationEvent event)
    {
        if (blacklistedMods.size() > 0)
        {
            throw new BlacklistedModException(blacklistedMods);
        }
        else
        {
            blacklistedMods = null;
        }
    }

    private void CreateBlacklistFile(File configFile) throws IOException
    {
        if (!configFile.createNewFile())
        {
            throw new IOException("Failed to create file.");
        }

        BufferedWriter file = new BufferedWriter(new FileWriter(configFile));

        file.write("# This is a comment, and how you can disable mods.\n");
        file.write("# You can disable mods by writing their mod id at the start of a line.\n");
        file.write("# When a user tries to load a blacklisted mod, it'll give them an explicit warning stating that the mod is blacklisted,\n");
        file.write("# instead of having a weird error occur later during initialization or runtime.\n");
        file.write("# It is imperative that this mod has a hoisted name, so that it occurs as early as possible\n");
        file.write("# during pre-initialization, unless you know the problematic mods fail later.\n");
        file.write("# If you want to leave a comment on why a mod is blacklisted, you can start a line with '#' as I have\n");
        file.write("# and leave an explanation. You can also leave a comment on the same line as a disabled mod, after the id.\n");
        file.write("# This comment will actually show up during the warning screen. If the message is too long, it'll end up cut off, though.\n");
        file.write("\n");

        file.write("# Example disabled mod:\n");
        file.write("examplemod # This mod has been disabled because it provides nothing to the experience.\n");

        file.close();
    }

}
