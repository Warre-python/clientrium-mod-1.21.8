package be.warrox.clientrium.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;

import javax.swing.plaf.SliderUI;

public class ClientriumScreen extends Screen {
    public ClientriumScreen(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        ButtonWidget fly = ButtonWidget.builder(
                Text.literal("Fly: " + (ControlMovement.fly ? "§aON" : "§cOFF")),
                (btn) -> {
                    ControlMovement.toggleFly();
                    // Update the button text immediately after clicking
                    btn.setMessage(Text.literal("Fly: " + (ControlMovement.fly ? "§aON" : "§cOFF")));
                }
        ).dimensions(40, 40, 120, 20).build();

        ButtonWidget noFall = ButtonWidget.builder(
                Text.literal("No Fall: " + (ControlMovement.noFall ? "§aON" : "§cOFF")),
                (btn) -> {
                    ControlMovement.toggleNoFall();
                    // Update the button text immediately after clicking
                    btn.setMessage(Text.literal("No Fall: " + (ControlMovement.noFall ? "§aON" : "§cOFF")));
                }
        ).dimensions(40, 80, 120, 20).build();


        ButtonWidget xray = ButtonWidget.builder(
                Text.literal("Xray: " + (ControlMovement.xray ? "§aON" : "§cOFF")),
                (btn) -> {
                    ControlMovement.toggleXray();
                    // Update the button text immediately after clicking
                    btn.setMessage(Text.literal("Xray: " + (ControlMovement.xray ? "§aON" : "§cOFF")));
                }
        ).dimensions(40, 120, 120, 20).build();

        ButtonWidget jezus = ButtonWidget.builder(
                Text.literal("Jezus: " + (ControlMovement.jezus ? "§aON" : "§cOFF")),
                (btn) -> {
                    ControlMovement.toggleJezus();
                    // Update the button text immediately after clicking
                    btn.setMessage(Text.literal("Jezus: " + (ControlMovement.jezus ? "§aON" : "§cOFF")));
                }
        ).dimensions(40, 160, 120, 20).build();

        ButtonWidget autoEat = ButtonWidget.builder(
                Text.literal("Auto Eat: " + (ControlMovement.autoEat ? "§aON" : "§cOFF")),
                (btn) -> {
                    ControlMovement.toggleAutoEat();
                    // Update the button text immediately after clicking
                    btn.setMessage(Text.literal("Auto Eat: " + (ControlMovement.autoEat ? "§aON" : "§cOFF")));
                }
        ).dimensions(40, 200, 120, 20).build();

        MySlider changeSpeed = new MySlider(200, 40, 120, 20, Text.literal("Value: " + ControlMovement.speed + "%"), ControlMovement.speed);



        this.addDrawableChild(fly);
        this.addDrawableChild(noFall);
        this.addDrawableChild(xray);
        this.addDrawableChild(jezus);
        this.addDrawableChild(autoEat);
        this.addDrawableChild(changeSpeed);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        // Minecraft doesn't have a "label" widget, so we'll have to draw our own text.
        // We'll subtract the font height from the Y position to make the text appear above the button.
        // Subtracting an extra 10 pixels will give the text some padding.
        // textRenderer, text, x, y, color, hasShadow
        //context.drawText(this.textRenderer, "Special Button", 40, 40 - this.textRenderer.fontHeight - 10, 0xFFFFFFFF, true);
    }
}