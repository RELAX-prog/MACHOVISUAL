package com.machovisual;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.slot.SlotActionType;
import org.lwjgl.glfw.GLFW;

public class MachoVisualClient implements ClientModInitializer {
	static KeyBinding menuKey, freeKey;
	int totemCd = 0, fbTimer = 0;
	boolean prevGround = true, fbApplied = false;
	Perspective prevPersp = Perspective.FIRST_PERSON;

	@Override
	public void onInitializeClient() {
		menuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("Open MachoVisual menu",
				InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, "MachoVisual"));
		freeKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("Free Look (hold)",
				InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_ALT, "MachoVisual"));
		ClientTickEvents.END_CLIENT_TICK.register(this::tick);
		HudRenderCallback.EVENT.register(Hud::render);
	}

	void tick(MinecraftClient mc) {
		while (menuKey.wasPressed()) mc.setScreen(new MenuScreen());
		ClientPlayerEntity p = mc.player;
		if (p == null || mc.world == null) return;

		// Free Look
		boolean want = Modules.freeLookEnabled && freeKey.isPressed() && mc.currentScreen == null;
		if (want && !Modules.freeLook) {
			Modules.freeLook = true;
			Modules.freeYaw = p.getYaw();
			Modules.freePitch = p.getPitch();
			prevPersp = mc.options.getPerspective();
			mc.options.setPerspective(Perspective.THIRD_PERSON_BACK);
		} else if (!want && Modules.freeLook) {
			Modules.freeLook = false;
			mc.options.setPerspective(prevPersp);
		}

		// Full Bright (client-side night vision)
		if (Modules.fullBright) {
			if (--fbTimer <= 0) {
				p.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 1000, 0, false, false, false));
				fbTimer = 40;
			}
			fbApplied = true;
		} else if (fbApplied) {
			p.removeStatusEffect(StatusEffects.NIGHT_VISION);
			fbApplied = false;
		}

		// Auto Totem
		if (Modules.autoTotem && --totemCd <= 0 && mc.currentScreen == null && mc.interactionManager != null
				&& !p.getOffHandStack().isOf(Items.TOTEM_OF_UNDYING)) {
			for (int i = 0; i < 36; i++) {
				if (p.getInventory().getStack(i).isOf(Items.TOTEM_OF_UNDYING)) {
					int slot = i < 9 ? i + 36 : i;
					mc.interactionManager.clickSlot(p.playerScreenHandler.syncId, slot, 40, SlotActionType.SWAP, p);
					totemCd = 5;
					break;
				}
			}
		}

		// Jump Circle
		if (Modules.jumpCircle && prevGround && !p.isOnGround() && p.getVelocity().y > 0.1) {
			for (int r = 0; r < 2; r++) {
				double speed = 0.10 + r * 0.08;
				for (int i = 0; i < 32; i++) {
					double a = Math.PI * 2 * i / 32.0;
					mc.world.addParticle(ParticleTypes.END_ROD,
							p.getX() + Math.cos(a) * 0.3, p.getY() + 0.05, p.getZ() + Math.sin(a) * 0.3,
							Math.cos(a) * speed, 0.0, Math.sin(a) * speed);
				}
			}
		}
		prevGround = p.isOnGround();
	}
}
