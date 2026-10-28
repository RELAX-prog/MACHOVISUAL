package com.machovisual;

public class Modules {
	public static boolean hud = true, crosshair = true, autoTotem = false, fullBright = false,
			freeLookEnabled = true, jumpCircle = true;
	public static boolean freeLook = false;
	public static float freeYaw, freePitch;
	public static final float[] RATIOS = {0f, 16f / 9f, 4f / 3f, 5f / 4f, 1f, 21f / 9f};
	public static final String[] RATIO_NAMES = {"Off", "16:9", "4:3", "5:4", "1:1", "21:9"};
	public static int ratioIdx = 0;
	public static float ratio() { return RATIOS[ratioIdx]; }
}
