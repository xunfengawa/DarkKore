package io.github.darkkronicle.darkkore.gui.components;

import io.github.darkkronicle.darkkore.util.FluidText;
import io.github.darkkronicle.darkkore.util.PositionedRectangle;
import io.github.darkkronicle.darkkore.util.Rectangle;
import io.github.darkkronicle.darkkore.util.StyleFormatter;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.List;

public class TextComponent extends BasicComponent {

    @Getter protected List<Text> lines;

    @Getter @Setter protected boolean updateBounds;

    @Getter @Setter protected int width;

    @Getter @Setter protected int height;

    @Getter @Setter protected boolean autoUpdateWidth = false;

    @Getter @Setter protected boolean autoUpdateHeight = false;

    @Getter @Setter protected boolean center = false;


    @Getter @Setter protected int leftPadding = 2;
    @Getter @Setter protected int rightPadding = 2;
    @Getter @Setter protected int topPadding = 2;
    @Getter @Setter protected int bottomPadding = 2;
    @Getter @Setter protected int linePadding = 2;


    public TextComponent(Text text) {
        this(-1, -1, text);
    }

    public TextComponent(int width, int height, Text text) {
        this.width = width;
        this.height = height;
        if (width < 0) {
            autoUpdateWidth = true;
        }
        if (height < 0) {
            autoUpdateHeight = true;
        }
        setLines(text);
    }

    public void setLines(Text text) {
        text = StyleFormatter.formatText(new FluidText(text));
        this.lines = StyleFormatter.wrapText(MinecraftClient.getInstance().textRenderer, autoUpdateWidth ? 10000000 : width, text);
        if (autoUpdateWidth) {
            updateWidth();
        }
        if (autoUpdateHeight) {
            updateHeight();
        }
    }

    protected void updateWidth() {
        int maxWidth = 0;
        for (Text text : lines) {
            maxWidth = Math.max(maxWidth, MinecraftClient.getInstance().textRenderer.getWidth(text) + leftPadding + rightPadding);
        }
        width = maxWidth;
    }

    protected void updateHeight() {
        height = topPadding + bottomPadding + lines.size() * (MinecraftClient.getInstance().textRenderer.fontHeight + linePadding);
    }

    @Override
    public void renderComponent(MatrixStack matrices, PositionedRectangle renderBounds, int x, int y, int mouseX, int mouseY) {
        int renderY = y + topPadding;
        for (Text line : lines) {
            if (center) {
                int width = MinecraftClient.getInstance().textRenderer.getWidth(line);
                MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, line, x + leftPadding + (int) ((this.width - rightPadding) / 2) - (int) (width / 2), renderY + topPadding, -1);
            } else {
                MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, line, x + leftPadding, renderY + topPadding, -1);
            }
            renderY += MinecraftClient.getInstance().textRenderer.fontHeight + linePadding;
        }
    }

    @Override
    public Rectangle getBoundingBox() {
        return new Rectangle(width, height);
    }
}
