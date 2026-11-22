/*    */ package net.minecraft.client.renderer.state.gui.pip;
/*    */ 
/*    */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*    */ import net.minecraft.client.model.object.banner.BannerFlagModel;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.level.block.entity.BannerPatternLayers;
/*    */ 
/*    */ public final class GuiBannerResultRenderState extends Record implements PictureInPictureRenderState {
/*    */   private final BannerFlagModel flag;
/*    */   
/*    */   private final DyeColor baseColor;
/*    */   
/*    */   private final BannerPatternLayers resultBannerPatterns;
/*    */   
/*    */   private final int x0;
/*    */   
/*    */   private final int y0;
/*    */   
/*    */   private final int x1;
/*    */   
/*    */   private final int y1;
/*    */   
/*    */   private final ScreenRectangle scissorArea;
/*    */   
/*    */   private final ScreenRectangle bounds;
/*    */   
/*    */   public GuiBannerResultRenderState(BannerFlagModel flag, DyeColor baseColor, BannerPatternLayers resultBannerPatterns, int x0, int y0, int x1, int y1, ScreenRectangle scissorArea, ScreenRectangle bounds) {
/*  9 */     this.flag = flag;
/*  9 */     this.baseColor = baseColor;
/*  9 */     this.resultBannerPatterns = resultBannerPatterns;
/*  9 */     this.x0 = x0;
/*  9 */     this.y0 = y0;
/*  9 */     this.x1 = x1;
/*  9 */     this.y1 = y1;
/*  9 */     this.scissorArea = scissorArea;
/*  9 */     this.bounds = bounds;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/state/gui/pip/GuiBannerResultRenderState;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/state/gui/pip/GuiBannerResultRenderState;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/state/gui/pip/GuiBannerResultRenderState;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/state/gui/pip/GuiBannerResultRenderState;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/state/gui/pip/GuiBannerResultRenderState;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/state/gui/pip/GuiBannerResultRenderState;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public BannerFlagModel flag() {
/*  9 */     return this.flag;
/*    */   }
/*    */   
/*    */   public DyeColor baseColor() {
/*  9 */     return this.baseColor;
/*    */   }
/*    */   
/*    */   public BannerPatternLayers resultBannerPatterns() {
/*  9 */     return this.resultBannerPatterns;
/*    */   }
/*    */   
/*    */   public int x0() {
/*  9 */     return this.x0;
/*    */   }
/*    */   
/*    */   public int y0() {
/*  9 */     return this.y0;
/*    */   }
/*    */   
/*    */   public int x1() {
/*  9 */     return this.x1;
/*    */   }
/*    */   
/*    */   public int y1() {
/*  9 */     return this.y1;
/*    */   }
/*    */   
/*    */   public ScreenRectangle scissorArea() {
/*  9 */     return this.scissorArea;
/*    */   }
/*    */   
/*    */   public ScreenRectangle bounds() {
/*  9 */     return this.bounds;
/*    */   }
/*    */   
/*    */   public GuiBannerResultRenderState(BannerFlagModel flag, DyeColor baseColor, BannerPatternLayers resultBannerPatterns, int x0, int y0, int x1, int y1, ScreenRectangle scissorArea) {
/* 12 */     this(flag, baseColor, resultBannerPatterns, x0, y0, x1, y1, scissorArea, PictureInPictureRenderState.getBounds(x0, y0, x1, y1, scissorArea));
/*    */   }
/*    */   
/*    */   public float scale() {
/* 17 */     return 16.0F;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\state\gui\pip\GuiBannerResultRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */