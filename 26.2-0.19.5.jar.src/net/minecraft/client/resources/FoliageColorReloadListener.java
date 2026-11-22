/*    */ package net.minecraft.client.resources;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
/*    */ import net.minecraft.util.profiling.ProfilerFiller;
/*    */ import net.minecraft.world.level.FoliageColor;
/*    */ 
/*    */ public class FoliageColorReloadListener extends SimplePreparableReloadListener<int[]> {
/* 12 */   private static final Identifier LOCATION = Identifier.withDefaultNamespace("textures/colormap/foliage.png");
/*    */   
/*    */   protected int[] prepare(ResourceManager manager, ProfilerFiller profiler) {
/*    */     try {
/* 17 */       return LegacyStuffWrapper.getPixels(manager, LOCATION);
/* 18 */     } catch (IOException e) {
/* 19 */       throw new IllegalStateException("Failed to load foliage color texture", e);
/*    */     } 
/*    */   }
/*    */   
/*    */   protected void apply(int[] pixels, ResourceManager manager, ProfilerFiller profiler) {
/* 25 */     FoliageColor.init(pixels);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\FoliageColorReloadListener.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */