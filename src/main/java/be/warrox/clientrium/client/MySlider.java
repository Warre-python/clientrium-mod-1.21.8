package be.warrox.clientrium.client;

import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public class MySlider extends SliderWidget {
    public MySlider(int x, int y, int width, int height, Text text, double value) {
        super(x, y, width, height, text, value);
        value = ControlMovement.speed;
    }

    @Override
    protected void updateMessage() {
        this.setMessage(Text.literal("Value: " + (int)(this.value * 100) + "%"));
    }

    @Override
    protected void applyValue() {
        ControlMovement.changeSpeed(value);
    }
}
