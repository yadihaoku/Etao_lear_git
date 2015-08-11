package com.etao.test.widget;

/**
 * 流体力学 
 * @author YadiYan 2015年8月4日
 *
 */
public final class AnimateUtils {
	private static float END_TENSION = 0.0F;
	private static final int NB_SAMPLES = 100;
	private static final float[] SPLINE;
	private static float START_TENSION = 0.4F;
	private static float sViscousFluidNormalize;
	private static float sViscousFluidScale;

	static {
		END_TENSION = 1.0F - START_TENSION;
		SPLINE = new float[101];
		float f1 = 0.0F;
		int i = 0;
		if (i <= 100) {
			float f2 = i / 100.0F;
			float f3 = 1.0F;
			float f4 = f1;
			while (true) {
				float f5 = f4 + (f3 - f4) / 2.0F;
				float f6 = 3.0F * f5 * (1.0F - f5);
				float f7 = f6 * ((1.0F - f5) * START_TENSION + f5 * END_TENSION) + f5 * (f5 * f5);
				if (Math.abs(f7 - f2) < 1.E-005D) {
					float f8 = f6 + f5 * (f5 * f5);
					SPLINE[i] = f8;
					i++;
					f1 = f4;
					break;
				}
				if (f7 > f2) {
					f3 = f5;
					continue;
				}
				f4 = f5;
			}
		}
		SPLINE[100] = 1.0F;
		sViscousFluidScale = 8.0F;
		sViscousFluidNormalize = 1.0F;
		sViscousFluidNormalize = 1.0F / viscousFluid(1.0F);
	}

	public static float viscousFluid(float paramFloat) {
		float f1 = paramFloat * sViscousFluidScale;
		float f2 = 0;
		if (f1 < 1.0F)
			f2 = f1 - (1.0F - (float) Math.exp(-f1));
		else
			f2 = 0.3678795F + (1.0F - (float) Math.exp(1.0F - f1)) * (1.0F - 0.3678795F);
		
		return f2 * sViscousFluidNormalize;

	}
}

/*
 * Location: L:\crash\resource\qq2013\classes_dex2jar.jar Qualified Name:
 * com.tencent.util.AnimateUtils JD-Core Version: 0.6.0
 */