/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ class MutableSpriteSet implements SpriteSet {
/*     */   private List<TextureAtlasSprite> sprites;
/*     */   
/*     */   public TextureAtlasSprite get(int index, int max) {
/* 283 */     return this.sprites.get(index * (this.sprites.size() - 1) / max);
/*     */   }
/*     */   
/*     */   public TextureAtlasSprite get(RandomSource random) {
/* 288 */     return this.sprites.get(random.nextInt(this.sprites.size()));
/*     */   }
/*     */   
/*     */   public TextureAtlasSprite first() {
/* 293 */     return this.sprites.getFirst();
/*     */   }
/*     */   
/*     */   public void rebind(List<TextureAtlasSprite> ids) {
/* 297 */     this.sprites = (List<TextureAtlasSprite>)ImmutableList.copyOf(ids);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\ParticleResources$MutableSpriteSet.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */