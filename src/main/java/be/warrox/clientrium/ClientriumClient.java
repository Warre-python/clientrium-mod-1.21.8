package be.warrox.clientrium;

import be.warrox.clientrium.Clientrium;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ClientriumClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Clientrium.LOGGER.info("Initializeing ClientModInitialize");
        ControlMovement controlMovement = new ControlMovement();
        controlMovement.initSetting();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {

                long handle = client.getWindow().getHandle();
                if (InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_X)) {
                    controlMovement.navigateTo();
                }

                if (InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_N)) {
                    controlMovement.setCheckpoint(client);
                }
            }
        });
    }
}