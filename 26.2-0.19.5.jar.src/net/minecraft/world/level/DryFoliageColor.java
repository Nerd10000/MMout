/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ public class DryFoliageColor {
/*    */   public static final int FOLIAGE_DRY_DEFAULT = -10732494;
/*    */   
/*  6 */   private static int[] pixels = new int[65536];
/*    */   
/*    */   public static void init(int[] pixels) {
/*  9 */     DryFoliageColor.pixels = pixels;
/*    */   }
/*    */   
/*    */   public static int get(double temp, double rain) {
/* 13 */     return ColorMapColorUtil.get(temp, rain, pixels, -10732494);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\DryFoliageColor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */