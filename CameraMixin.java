package com.machovisual.mixin;

import com.machovisual.Modules;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Camera.class)
public class CameraMixin {
	@ModifyVariable(method = "setRotation", at = @At("HEAD"), argsOnly = true, ordinal = 0)
	private float machovisual$yaw(float v) { return Modules.freeLook ? Modules.freeYaw : v; }

	@ModifyVariable(method = "setRotation", at = @At("HEAD"), argsOnly = true, ordinal = 1)
	private float machovisual$pitch(float v) { return Modules.freeLook ? Modules.freePitch : v; }
}
