package com.machovisual.mixin;

import com.machovisual.Modules;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.Window;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
	@Inject(method = "getBasicProjectionMatrix", at = @At("RETURN"))
	private void machovisual$aspect(float fov, CallbackInfoReturnable<Matrix4f> cir) {
		float target = Modules.ratio();
		if (target <= 0f) return;
		Window w = MinecraftClient.getInstance().getWindow();
		float cur = (float) w.getFramebufferWidth() / (float) w.getFramebufferHeight();
		cir.getReturnValue().scale(cur / target, 1f, 1f);
	}
}
