package com.machovisual;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.RenderTickCounter;

public class Hud {
	static final int BG = 0xE60B0D14, PURPLE = 0xFF8B5CF6, WHITE = 0xFFFFFFFF, GRAY = 0xFF6B7280;

	public static void render(DrawContext c, RenderTickCounter tc) {
		MinecraftClient mc = MinecraftClient.getInstance();
		if (mc.player == null || mc.options.hudHidden) return;
		int sw = c.getScaledWindowWidth(), sh = c.getScaledWindowHeight();

		if (Modules.crosshair && mc.options.getPerspective().isFirstPerson() && mc.currentScreen == null) {
			int cx = sw / 2, cy = sh / 2, col = 0xFFFFFFFF;
			c.fill(cx - 7, cy, cx - 3, cy + 1, col);
			c.fill(cx + 4, cy, cx + 8, cy + 1, col);
			c.fill(cx, cy - 7, cx + 1, cy - 3, col);
			c.fill(cx, cy + 4, cx + 1, cy + 8, col);
			c.fill(cx, cy, cx + 1, cy + 1, PURPLE);
		}

		if (Modules.hud) {
			TextRenderer f = mc.textRenderer;
			int ping = 0;
			if (mc.getNetworkHandler() != null) {
				PlayerListEntry e = mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());
				if (e != null) ping = e.getLatency();
			}
			String name = "MachoVisual", pg = ping + " ms", fps = mc.getCurrentFps() + " FPS";
			int gap = 14;
			int w = 26 + f.getWidth(name) + gap + f.getWidth(pg) + gap + f.getWidth(fps) + 10;
			int x0 = sw / 2 - w / 2, y0 = 4, x1 = x0 + w, y1 = y0 + 22;
			c.fill(x0 + 3, y0, x1 - 3, y1, BG);
			c.fill(x0, y0 + 3, x1, y1 - 3, BG);
			c.fill(x0 + 1, y0 + 1, x1 - 1, y1 - 1, BG);
			c.fill(x0 + 2, y0 + 2, x1 - 2, y1 - 2, BG);
			// logo: dark tile with purple pulse bars
			c.fill(x0 + 5, y0 + 3, x0 + 21, y0 + 19, 0xFF1E1B3A);
			int[] hs = {3, 7, 11, 5, 9, 3};
			for (int i = 0; i < hs.length; i++) {
				int bx = x0 + 7 + i * 2;
				c.fill(bx, y0 + 11 - hs[i] / 2, bx + 1, y0 + 11 + (hs[i] + 1) / 2, PURPLE);
			}
			int tx = x0 + 26, ty = y0 + 7;
			c.drawText(f, name, tx, ty, WHITE, false);
			tx += f.getWidth(name);
			c.drawText(f, "/", tx + gap / 2 - 2, ty, GRAY, false);
			tx += gap;
			c.drawText(f, pg, tx, ty, 0xFFD1D5DB, false);
			tx += f.getWidth(pg);
			c.drawText(f, "/", tx + gap / 2 - 2, ty, GRAY, false);
			tx += gap;
			c.drawText(f, fps, tx, ty, 0xFFD1D5DB, false);
		}
	}
}
