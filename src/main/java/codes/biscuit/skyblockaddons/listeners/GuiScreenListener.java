package codes.biscuit.skyblockaddons.listeners;

import codes.biscuit.skyblockaddons.SkyblockAddons;
import codes.biscuit.skyblockaddons.utils.dev.DevUtils;
import codes.biscuit.skyblockaddons.utils.nifty.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.util.concurrent.TimeUnit;

/**
 * This listener listens for events that happen while a {@code GuiScreen} is open.
 *
 * @author ILikePlayingGames
 * @version 1.0
 */
public class GuiScreenListener {

    private final SkyblockAddons main;

    public GuiScreenListener(SkyblockAddons main) {
        this.main = main;
    }

    /**
     * Listens for key presses while a GUI is open
     *
     * @param event the {@code GuiScreenEvent.KeyboardInputEvent} to listen for
     */
    @SubscribeEvent()
    public void onKeyInput(GuiScreenEvent.KeyboardInputEvent event) {
        int eventKey = Keyboard.getEventKey();

        if (eventKey == DevUtils.DEV_KEY) {
            long eventTime = TimeUnit.MILLISECONDS.convert(Keyboard.getEventNanoseconds(), TimeUnit.NANOSECONDS);
            event.setCanceled(true);

            // For some reason four key presses are detected for each actual press so count only the first one.
            if (eventTime - DevUtils.getLastDevKeyEvent() > 100L) {
                DevUtils.setLastDevKeyEvent(Minecraft.getSystemTime());

                // Copy Item NBT
                if (main.isDevMode()) {
                    GuiScreen currentScreen = event.gui;

                    // Check if the player is in an inventory.
                    if (GuiContainer.class.isAssignableFrom(currentScreen.getClass())) {
                        Slot currentSlot = ((GuiContainer) currentScreen).getSlotUnderMouse();

                        if (currentSlot != null && currentSlot.getHasStack()) {
                            DevUtils.copyNBTTagToClipboard(currentSlot.getStack().getTagCompound(),
                                    ChatFormatting.GREEN + "Item data was copied to clipboard!");
                        }
                    }
                }
            }
        }
    }

}
