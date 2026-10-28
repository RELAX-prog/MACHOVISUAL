package com.machovisual.mixin;

import com.machovisual.Modules;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
	@Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
	private void machovisual$crosshair(CallbackInfo ci) {
		if (Modules.crosshair) ci.cancel();
	}
}
