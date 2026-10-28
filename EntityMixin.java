package com.machovisual.mixin;

import com.machovisual.Modules;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
	@Inject(method = "changeLookDirection", at = @At("HEAD"), cancellable = true)
	private void machovisual$look(double dx, double dy, CallbackInfo ci) {
		if (Modules.freeLook && (Object) this instanceof ClientPlayerEntity) {
			Modules.freeYaw += (float) dx * 0.15f;
			Modules.freePitch = Math.max(-90f, Math.min(90f, Modules.freePitch + (float) dy * 0.15f));
			ci.cancel();
		}
	}
}
