package be.warrox.clientrium.client;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

public class ClientCommands {
    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("clientrium")
                    .executes(ClientCommands::executeClientrium)
                    .then(ClientCommandManager.literal("setCustomCheckpoint")
                            .then(ClientCommandManager.argument("x", IntegerArgumentType.integer())
                                    .then(ClientCommandManager.argument("y", IntegerArgumentType.integer())
                                            .then(ClientCommandManager.argument("z", IntegerArgumentType.integer())
                                                    .executes(ClientCommands::executeSetCustomCheckpoint)
                                            )
                                    )
                            )
                    )
                    .then(ClientCommandManager.literal("setCheckpoint")
                            .executes(ClientCommands::executeSetCheckpoint)
                    )

                    .then(ClientCommandManager.literal("start")
                            .executes(ClientCommands::executeStart)
                    )

                    .then(ClientCommandManager.literal("stop")
                            .executes(ClientCommands::executeStop)
                    )

                    .then(ClientCommandManager.literal("toggleFly")
                            .executes(ClientCommands::executeToggleFly)
                    )
                    .then(ClientCommandManager.literal("toggleNoFall")
                            .executes(ClientCommands::executeToggleNoFall)
                    )
                    .then(ClientCommandManager.literal("toggleJezus")
                            .executes(ClientCommands::executeToggleJezus)
                    )

                    .then(ClientCommandManager.literal("toggleXray")
                            .executes(ClientCommands::executeToggleXray)
                    )

                    .then(ClientCommandManager.literal("toggleAutoEat")
                            .executes(ClientCommands::executeToggleAutoEat)
                    )

                    .then(ClientCommandManager.literal("changeSpeed")
                            .then(ClientCommandManager.argument("speed", DoubleArgumentType.doubleArg())
                            .executes(ClientCommands::executeChangeSpeed)
                    )

            ));
        });
    }

    public static int executeClientrium(CommandContext<FabricClientCommandSource> context) {
        context.getSource().sendFeedback(Text.literal("Hallo from Clientrium"));
        return 1;
    }

    public static int executeSetCustomCheckpoint(CommandContext<FabricClientCommandSource> context) {
        int x = IntegerArgumentType.getInteger(context, "x");
        int y = IntegerArgumentType.getInteger(context, "y");
        int z = IntegerArgumentType.getInteger(context, "z");

        Vec3d pos = new Vec3d((double) x, (double) y, (double) z);

        ControlMovement.setCustomCheckpoint(pos);
        context.getSource().sendFeedback(Text.literal("Setting Custom Checkpoint on "+ x + " " + y + " " + z));
        return 1;
    }

    public static int executeSetCheckpoint(CommandContext<FabricClientCommandSource> context) {
        ControlMovement.setCheckpoint();
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            context.getSource().sendFeedback(Text.literal("Setting Checkpoint on " + client.player.getPos()));
            return 1;
        }
        return 0;
    }

    public static int executeStart(CommandContext<FabricClientCommandSource> context) {
        ControlMovement.navigateTo();
        return 1;
    }

    public static int executeStop(CommandContext<FabricClientCommandSource> context) {
        ControlMovement.stop();
        return 1;
    }

    public static int executeToggleFly(CommandContext<FabricClientCommandSource> context) {
        ControlMovement.toggleFly();
        return 1;
    }

    public static int executeChangeSpeed(CommandContext<FabricClientCommandSource> context) {
        double newSpeed = DoubleArgumentType.getDouble(context, "speed");
        ControlMovement.changeSpeed(newSpeed);
        return 1;
    }

    public static int executeToggleNoFall(CommandContext<FabricClientCommandSource> context) {
        ControlMovement.toggleNoFall();
        return 1;
    }

    public static int executeToggleJezus(CommandContext<FabricClientCommandSource> context) {
        ControlMovement.toggleJezus();
        return 1;
    }

    public static int executeToggleXray(CommandContext<FabricClientCommandSource> context) {
        ControlMovement.toggleXray();
        return 1;
    }

    public static int executeToggleAutoEat(CommandContext<FabricClientCommandSource> context) {
        ControlMovement.toggleAutoEat();
        return 1;
    }
}
