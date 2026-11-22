/*    */ package net.minecraft.world.level.levelgen.synth;
/*    */ 
/*    */ import java.util.Locale;
/*    */ 
/*    */ public class NoiseUtils {
/*    */   public static double biasTowardsExtreme(double noise, double factor) {
/* 12 */     return noise + Math.sin(Math.PI * noise) * factor / Math.PI;
/*    */   }
/*    */   
/*    */   public static void parityNoiseOctaveConfigString(StringBuilder sb, double xo, double yo, double zo, byte[] p) {
/* 17 */     sb.append(String.format(Locale.ROOT, "xo=%.3f, yo=%.3f, zo=%.3f, p0=%d, p255=%d", new Object[] { Float.valueOf((float)xo), Float.valueOf((float)yo), Float.valueOf((float)zo), Byte.valueOf(p[0]), Byte.valueOf(p[255]) }));
/*    */   }
/*    */   
/*    */   public static void parityNoiseOctaveConfigString(StringBuilder sb, double xo, double yo, double zo, int[] p) {
/* 22 */     sb.append(String.format(Locale.ROOT, "xo=%.3f, yo=%.3f, zo=%.3f, p0=%d, p255=%d", new Object[] { Float.valueOf((float)xo), Float.valueOf((float)yo), Float.valueOf((float)zo), Integer.valueOf(p[0]), Integer.valueOf(p[255]) }));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\synth\NoiseUtils.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */