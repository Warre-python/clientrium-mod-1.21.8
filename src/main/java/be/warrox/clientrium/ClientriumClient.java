package be.warrox.clientrium;

import be.warrox.clientrium.client.ClientCommands;
import be.warrox.clientrium.client.ClientHacks;
import be.warrox.clientrium.client.ClientriumScreen;
import be.warrox.clientrium.client.ControlMovement;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ClientriumClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Clientrium.LOGGER.info("Initializeing ClientModInitialize");
        ControlMovement.initSetting();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                long handle = client.getWindow().getHandle();
                if (InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_X)) {
                    ControlMovement.navigateTo();
                }

                if (InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_N)) {
                    ControlMovement.setCheckpoint();
                }

                if (InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_RIGHT_SHIFT)) {
                    MinecraftClient.getInstance().setScreen(
                            new ClientriumScreen(Text.of("Clientrium Hacks"))
                    );
                }

                ClientHacks.update();
            }
        });

        ClientCommands.register();
    }
}