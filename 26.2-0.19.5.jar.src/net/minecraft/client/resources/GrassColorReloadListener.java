/*    */ package net.minecraft.client.resources;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
/*    */ import net.minecraft.util.profiling.ProfilerFiller;
/*    */ import net.minecraft.world.level.GrassColor;
/*    */ 
/*    */ public class GrassColorReloadListener extends SimplePreparableReloadListener<int[]> {
/* 12 */   private static final Identifier LOCATION = Identifier.withDefaultNamespace("textures/colormap/grass.png");
/*    */   
/*    */   protected int[] prepare(ResourceManager manager, ProfilerFiller profiler) {
/*    */     try {
/* 17 */       return LegacyStuffWrapper.getPixels(manager, LOCATION);
/* 18 */     } catch (IOException e) {
/* 19 */       throw new IllegalStateException("Failed to load grass color texture", e);
/*    */     } 
/*    */   }
/*    */   
/*    */   protected void apply(int[] pixels, ResourceManager manager, ProfilerFiller profiler) {
/* 25 */     GrassColor.init(pixels);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\GrassColorReloadListener.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */