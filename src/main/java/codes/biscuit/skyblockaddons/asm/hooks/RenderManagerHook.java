package codes.biscuit.skyblockaddons.asm.hooks;

import codes.biscuit.skyblockaddons.SkyblockAddons;
import codes.biscuit.skyblockaddons.asm.utils.ReturnValue;
import codes.biscuit.skyblockaddons.utils.Feature;
import codes.biscuit.skyblockaddons.utils.Location;
import codes.biscuit.skyblockaddons.utils.npc.NPCUtils;
import codes.biscuit.skyblockaddons.utils.npc.Tag;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.BlockPos;

import java.util.LinkedHashSet;

public class RenderManagerHook {

    @Getter private static LinkedHashSet<EntityArmorStand> nearbyBones = new LinkedHashSet<>();

    public static void shouldRender(Entity entityIn, ReturnValue<Boolean> returnValue) {
        SkyblockAddons main = SkyblockAddons.getInstance();

        if (main.getUtils().isOnSkyblock()) {
            Location currentLocation = main.getUtils().getLocation();

            if (main.getConfigValues().isEnabled(Feature.HIDE_BONES)) {
                if (entityIn instanceof EntityArmorStand) {
                    EntityArmorStand stand = (EntityArmorStand) entityIn;
                    if (stand.getHeldItem() != null && stand.getHeldItem().toString().contains("item.bone")) {
                        returnValue.cancel();
                        BlockPos position = Minecraft.getMinecraft().thePlayer.getPosition();
                        stand.setPositionAndUpdate(position.getX(), position.getY() - 2, position.getZ());
                        stand.serverPosX = position.getX();
                        stand.serverPosY = position.getY() - 2;
                        stand.serverPosZ = position.getZ();
                    }
                }
            }

            if (main.getConfigValues().isEnabled(Feature.HIDE_PLAYERS_NEAR_NPCS)) {
                if (entityIn instanceof EntityOtherPlayerMP && NPCUtils.isNearAnyNPCWithTag(entityIn, Tag.IMPORTANT) && !NPCUtils.isNPC(entityIn)) {
                    returnValue.cancel();
                }
            }

            if (main.getConfigValues().isEnabled(Feature.HIDE_PLAYERS)) {
                if ((entityIn instanceof EntityOtherPlayerMP) &&
                        entityIn.getDistanceToEntity(Minecraft.getMinecraft().thePlayer) > 7) {
                    returnValue.cancel();
                }
            } else if (main.getConfigValues().isEnabled(Feature.HIDE_PLAYERS_IN_LOBBY)) {
                if (currentLocation == Location.VILLAGE || currentLocation == Location.AUCTION_HOUSE ||
                        currentLocation == Location.BANK) {
                    if ((entityIn instanceof EntityOtherPlayerMP) &&
                            entityIn.getDistanceToEntity(Minecraft.getMinecraft().thePlayer) > 7) {
                        returnValue.cancel();
                    }
                }
            }
        }
    }

}
