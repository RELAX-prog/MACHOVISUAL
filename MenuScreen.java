package com.machovisual;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class MenuScreen extends Screen {
	public MenuScreen() { super(Text.literal("MachoVisual")); }

	static Text label(String n, boolean on) { return Text.literal(n + ": " + (on ? "ON" : "OFF")); }

	void toggle(int y, String name, BooleanSupplier get, Consumer<Boolean> set) {
		addDrawableChild(ButtonWidget.builder(label(name, get.getAsBoolean()), b -> {
			set.accept(!get.getAsBoolean());
			b.setMessage(label(name, get.getAsBoolean()));
		}).dimensions(width / 2 - 85, y, 170, 20).build());
	}

	@Override
	protected void init() {
		int y = height / 2 - 100;
		toggle(y, "HUD (ping/FPS)", () -> Modules.hud, v -> Modules.hud = v); y += 23;
		toggle(y, "Crosshair", () -> Modules.crosshair, v -> Modules.crosshair = v); y += 23;
		toggle(y, "Auto Totem", () -> Modules.autoTotem, v -> Modules.autoTotem = v); y += 23;
		toggle(y, "Full Bright", () -> Modules.fullBright, v -> Modules.fullBright = v); y += 23;
		toggle(y, "Free Look (hold Alt)", () -> Modules.freeLookEnabled, v -> Modules.freeLookEnabled = v); y += 23;
		toggle(y, "Jump Circle", () -> Modules.jumpCircle, v -> Modules.jumpCircle = v); y += 23;
		addDrawableChild(ButtonWidget.builder(Text.literal("Aspect Ratio: " + Modules.RATIO_NAMES[Modules.ratioIdx]), b -> {
			Modules.ratioIdx = (Modules.ratioIdx + 1) % Modules.RATIOS.length;
			b.setMessage(Text.literal("Aspect Ratio: " + Modules.RATIO_NAMES[Modules.ratioIdx]));
		}).dimensions(width / 2 - 85, y, 170, 20).build()); y += 28;
		addDrawableChild(ButtonWidget.builder(Text.literal("Done"), b -> close())
				.dimensions(width / 2 - 85, y, 170, 20).build());
	}

	@Override
	public void render(DrawContext c, int mx, int my, float delta) {
		super.render(c, mx, my, delta);
		c.drawCenteredTextWithShadow(textRenderer, "MachoVisual", width / 2, height / 2 - 116, 0xFFA78BFA);
	}
}
