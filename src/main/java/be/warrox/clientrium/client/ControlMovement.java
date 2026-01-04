package be.warrox.clientrium.client;

import baritone.api.BaritoneAPI;
import baritone.api.pathing.goals.GoalBlock;
import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.List;


public class ControlMovement {
    private static Vec3d checkpoint;
    public static boolean fly = false;
    public static double speed = 1.0;
    public static boolean noFall = false;
    public static boolean jezus = false;
    public static boolean xray = false;
    public static boolean autoEat = false;

    public static void navigateTo() {

        if (checkpoint == null) return;

        BlockPos goalPos = BlockPos.ofFloored(checkpoint.x, checkpoint.y, checkpoint.z);

        // 1. Start Baritone Pathfinding
        BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess()
                .setGoalAndPath(new GoalBlock(goalPos));

    }

    public static void stop() {
        BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().cancelEverything();
    }

    public static void toggleFly() {
        fly = !fly;
    }

    public static void changeSpeed(double newSpeed) {
        speed = newSpeed;
    }

    public static void fly() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (fly && client.player != null) {
            

            // 1. Get the player's movement input (WASD)
            // input.movementForward is W/S, input.movementSideways is A/D
            // Access the movement values via the playerInput record
            float forward = 0;
            if (client.player.input.playerInput.forward()) forward += 1;
            if (client.player.input.playerInput.backward()) forward -= 1;

            float sideways = 0;
            if (client.player.input.playerInput.left()) sideways -= 1;
            if (client.player.input.playerInput.right()) sideways += 1;

            float yaw = client.player.getYaw();

            // 2. Calculate the horizontal (X, Z) direction based on where the player looks
            // This converts WASD input into a 3D vector aligned with player rotation
            Vec3d velocity = Vec3d.fromPolar(0, yaw).multiply(forward)
                    .add(Vec3d.fromPolar(0, yaw + 90).multiply(sideways))
                    .normalize()
                    .multiply(speed);

            // 3. Handle Vertical (Y) movement
            double yVelocity = 0;
            if (client.options.jumpKey.isPressed()) {
                yVelocity = speed;
            } else if (client.options.sneakKey.isPressed()) {
                yVelocity = -speed;
            }

            // 4. Apply the calculated velocity to the player
            client.player.setVelocity(velocity.x, yVelocity, velocity.z);
        }
    }

    public static void toggleNoFall() {
        noFall = !noFall;
    }

    public static void noFall() {
        if (noFall) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null && client.player.fallDistance > 2.0f) {
                // Send a packet claiming the player is on the ground
                client.player.networkHandler.sendPacket(
                        new PlayerMoveC2SPacket.OnGroundOnly(true, client.player.horizontalCollision)
                );
            }
        }
    }

    public static void toggleJezus() {
        jezus = !jezus;
    }


    public static void jezus() {
        if (jezus) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null || client.world == null) return;

            // Check if player is just above water
            if (client.world.getFluidState(client.player.getBlockPos().down()).isOf(Fluids.WATER)) {
                //if (!client.player.isSneaking() && client.player.fallDistance < 3.0f) {
                Vec3d vel = client.player.getVelocity();

                // Set vertical velocity to 0 to stay on top
                client.player.setVelocity(vel.x, 0.0, vel.z);
                client.player.setOnGround(true);

            }
        }
    }

    public static void toggleXray() {
        xray = !xray;
    }

    public static void autoEat() {
        if (autoEat) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null || client.world == null) return;

            // Only eat if hunger is 14 or lower (7 drumsticks)
            if (client.player.getHungerManager().getFoodLevel() <= 14) {

                // Search inventory for food
                for (int i = 0; i < client.player.getInventory().size(); i++) {
                    ItemStack stack = client.player.getInventory().getStack(i);

                    // In 1.21.8, we check for the Food component
                    if (stack.getComponents().contains(DataComponentTypes.FOOD)) {

                        // Switch to the slot or use it (Simple version: must be in hotbar)
                        if (i < 9) { // Check if it's in the hotbar
                            client.player.getInventory().setSelectedSlot(i);

                            // Simulate holding right-click
                            client.options.useKey.setPressed(true);
                            return;
                        }
                    }
                }
            } else {
                // Stop eating once full
                if (!client.options.useKey.getDefaultKey().equals(client.options.useKey)) {
                    // Optional: logic to stop pressing if AutoEat was the one pressing it
                }
            }
        }
    }

    public static void toggleAutoEat() {
        autoEat = !autoEat;
    }





    public static void setCheckpoint() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            checkpoint = client.player.getPos();
        }
    }

    public static void setCustomCheckpoint(Vec3d pos) {
        checkpoint = pos;
        System.out.println("Custom checkpoint set via command: " + checkpoint);
    }

    public static void initSetting() {
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
