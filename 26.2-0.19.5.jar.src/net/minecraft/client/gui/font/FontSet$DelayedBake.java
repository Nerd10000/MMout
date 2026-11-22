/*     */ package net.minecraft.client.gui.font;
/*     */ 
/*     */ import com.mojang.blaze3d.font.UnbakedGlyph;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.gui.font.glyphs.BakedGlyph;
/*     */ 
/*     */ class DelayedBake implements Supplier<BakedGlyph> {
/*     */   private final UnbakedGlyph unbaked;
/*     */   
/*     */   private BakedGlyph baked;
/*     */   
/*     */   private DelayedBake(UnbakedGlyph unbaked) {
/* 199 */     this.unbaked = unbaked;
/*     */   }
/*     */   
/*     */   public BakedGlyph get() {
/* 204 */     if (this.baked == null)
/* 205 */       this.baked = this.unbaked.bake(FontSet.this.wrappedStitcher); 
/* 207 */     return this.baked;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\FontSet$DelayedBake.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */