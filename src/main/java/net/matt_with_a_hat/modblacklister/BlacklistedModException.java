package net.matt_with_a_hat.modblacklister;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiErrorScreen;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.CustomModLoadingErrorDisplayException;
import net.minecraftforge.fml.client.GuiScrollingList;

import java.util.List;

class BlacklistedModException extends CustomModLoadingErrorDisplayException
{
    private GuiScrollingList list;
    private final List<ModBlacklister.ModInfo> blacklistedMods;
    BlacklistedModException(List<ModBlacklister.ModInfo> blacklistedMods)
    {
        this.blacklistedMods = blacklistedMods;
    }

    @Override
    public void initGui(GuiErrorScreen errorScreen, FontRenderer fontRenderer)
    {
        list = new BlacklistedModScrollingList(errorScreen, blacklistedMods);
    }

    @Override
    public void drawScreen(GuiErrorScreen errorScreen, FontRenderer fontRenderer, int mouseRelX, int mouseRelY, float tickTime)
    {
        errorScreen.drawCenteredString(fontRenderer,
                TextFormatting.BOLD +
                        "Blacklisted mods were detected during start up!",
                errorScreen.width / 2, 16 - fontRenderer.FONT_HEIGHT, 0xFFFFFF);

        errorScreen.drawCenteredString(fontRenderer,
                TextFormatting.BOLD +
                        "Please remove them to continue.",
                errorScreen.width / 2, 18, 0xFFFFFF);

        list.drawScreen(mouseRelX, mouseRelY, tickTime);
    }
}