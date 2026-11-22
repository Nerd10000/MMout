/*    */ package net.minecraft.client.renderer.texture;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Locale;
/*    */ 
/*    */ public class StitcherException extends RuntimeException {
/*    */   private final Collection<Stitcher.Entry> allSprites;
/*    */   
/*    */   public StitcherException(Stitcher.Entry sprite, Collection<Stitcher.Entry> allSprites) {
/* 10 */     super(String.format(Locale.ROOT, "Unable to fit: %s - size: %dx%d - Maybe try a lower resolution resourcepack?", new Object[] { sprite
/*    */             
/* 13 */             .name(), 
/* 14 */             Integer.valueOf(sprite.width()), 
/* 15 */             Integer.valueOf(sprite.height()) }));
/* 18 */     this.allSprites = allSprites;
/*    */   }
/*    */   
/*    */   public Collection<Stitcher.Entry> getAllSprites() {
/* 22 */     return this.allSprites;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\texture\StitcherException.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */