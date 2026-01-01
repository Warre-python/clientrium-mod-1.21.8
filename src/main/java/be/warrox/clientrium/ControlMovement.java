package be.warrox.clientrium;

import baritone.api.BaritoneAPI;
import baritone.api.Settings;
import baritone.api.pathing.goals.GoalBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;

import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;


public class ControlMovement {
    private Vec3d checkpoint;

    public void navigateTo() {
        if (checkpoint == null) return;

        MinecraftClient client = MinecraftClient.getInstance();
        BlockPos goalPos = BlockPos.ofFloored(checkpoint.x, checkpoint.y, checkpoint.z);

        // 1. Start Baritone Pathfinding
        BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess()
                .setGoalAndPath(new GoalBlock(goalPos));

        // Logic update: Only click if Baritone has arrived or is very close
        if (client.player.getBlockPos().isWithinDistance(goalPos, 0.0)) {
            // Stop pathing before interacting
            BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().onLostControl();

            // Perform the interaction once
            openChest(goalPos.down());
        }

    }

    // Use this inside your navigateTo logic once the player is in range
    public void openChest(BlockPos pos) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.interactionManager == null || client.world == null) return;

        // Create a HitResult to tell the game exactly where you are clicking
        BlockHitResult hitResult = new BlockHitResult(
                Vec3d.ofCenter(pos), // Center of the chest
                Direction.UP,        // Clicking the top face
                pos,
                false
        );

        // Send the interaction packet directly
        client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, hitResult);
    }


    public void setCheckpoint(MinecraftClient client) {
        if (client.player != null) {
            checkpoint = client.player.getPos();
        }
    }

    public void setCustomCheckpoint(BlockPos pos) {
        // Convert BlockPos back to Vec3d for your navigation logic
        this.checkpoint = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
        System.out.println("Custom checkpoint set via command: " + checkpoint);
    }

    public void initSetting() {
        BaritoneAPI.getSettings().allowBreak.value = false;
        BaritoneAPI.getSettings().allowPlace.value = true;
        BaritoneAPI.getSettings().allowInventory.value = true;
        BaritoneAPI.getSettings().allowSprint.value = true;

    }


    public static boolean hasItem(Item item) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) {
            return false;
        }

        PlayerInventory inventory = client.player.getInventory();

        // Check main inventory and hotbar (slots 0-35)
        for (int i = 0; i < 36; i++) {
            if (inventory.getStack(i).isOf(item)) {
                return true;
            }
        }

        // Check offhand (slot 40)
        if (inventory.getStack(40).isOf(item)) {
            return true;
        }

        // Check armor slots if needed (e.g., for specific armor)
        // For general items, the above is usually enough

        return false;
    }
}
