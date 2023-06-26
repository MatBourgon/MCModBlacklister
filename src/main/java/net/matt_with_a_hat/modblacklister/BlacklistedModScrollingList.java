package net.matt_with_a_hat.modblacklister;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.GuiScrollingList;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;

import java.awt.*;
import java.io.File;
import java.util.List;

class BlacklistedModScrollingList extends GuiScrollingList
{
    private final List<ModBlacklister.ModInfo> blacklistedMods;
    GuiScreen screen;
    FontRenderer fontRenderer;

    public BlacklistedModScrollingList(GuiScreen screen, List<ModBlacklister.ModInfo> mods)
    {
        super(screen.mc, screen.width, screen.height, 32, screen.height - 48 - 8, 0,
                screen.mc.fontRenderer.FONT_HEIGHT * 4, screen.width, screen.height);

        this.blacklistedMods = mods;
        this.screen = screen;
        this.fontRenderer = screen.mc.fontRenderer;
    }

    @Override
    protected int getSize() {
        return blacklistedMods.size();
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick) { }

    @Override
    protected boolean isSelected(int index) {
        return false;
    }

    @Override
    protected void drawBackground() { }

    @Override
    protected int getContentHeight()
    {
        return this.getSize() * this.slotHeight;
    }

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess)
    {
        ModBlacklister.ModInfo mod = blacklistedMods.get(slotIdx);
        String modNameStr = String.format("%s (%s)", mod.modName, mod.modFileName);

        screen.drawString(fontRenderer,
                modNameStr, (screen.width - fontRenderer.getStringWidth(modNameStr)) / 2, slotTop, 0xFFFFFF);

        if (mod.modComment != null)
        {
            List<String> comments = fontRenderer.listFormattedStringToWidth(mod.modComment, screen.width - 16);

            int GRAY = 0xAAAAAA;
            screen.drawString(fontRenderer, TextFormatting.ITALIC + comments.get(0),
                    (screen.width - fontRenderer.getStringWidth(comments.get(0))) / 2, slotTop + fontRenderer.FONT_HEIGHT, GRAY);

            if (comments.size() > 1)
            {
                String comment = comments.get(1);
                if (comments.size() > 2)
                {
                    comment = comment.substring(0, comment.length() - 3) + "...";
                }
                screen.drawString(fontRenderer, TextFormatting.ITALIC + comment,
                        (screen.width - fontRenderer.getStringWidth(comment)) / 2, slotTop + 2 * fontRenderer.FONT_HEIGHT, GRAY);
            }
        }
    }
}