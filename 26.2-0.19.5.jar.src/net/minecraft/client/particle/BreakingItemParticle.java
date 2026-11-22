/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.resources.model.sprite.Material;
/*     */ import net.minecraft.core.particles.ItemParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.data.AtlasIds;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.ItemDisplayContext;
/*     */ import net.minecraft.world.item.ItemStackTemplate;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class BreakingItemParticle extends SingleQuadParticle {
/*     */   private final float uo;
/*     */   
/*     */   private final float vo;
/*     */   
/*     */   private final SingleQuadParticle.Layer layer;
/*     */   
/*     */   private BreakingItemParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, TextureAtlasSprite sprite) {
/*  23 */     this(level, x, y, z, sprite);
/*  24 */     this.xd *= 0.10000000149011612D;
/*  25 */     this.yd *= 0.10000000149011612D;
/*  26 */     this.zd *= 0.10000000149011612D;
/*  27 */     this.xd += xa;
/*  28 */     this.yd += ya;
/*  29 */     this.zd += za;
/*     */   }
/*     */   
/*     */   protected BreakingItemParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
/*  33 */     super(level, x, y, z, 0.0D, 0.0D, 0.0D, sprite);
/*  34 */     this.gravity = 1.0F;
/*  35 */     this.quadSize /= 2.0F;
/*  37 */     this.uo = this.random.nextFloat() * 3.0F;
/*  38 */     this.vo = this.random.nextFloat() * 3.0F;
/*  39 */     this.layer = SingleQuadParticle.Layer.bySprite(sprite);
/*     */   }
/*     */   
/*     */   protected float getU0() {
/*  44 */     return this.sprite.getU((this.uo + 1.0F) / 4.0F);
/*     */   }
/*     */   
/*     */   protected float getU1() {
/*  49 */     return this.sprite.getU(this.uo / 4.0F);
/*     */   }
/*     */   
/*     */   protected float getV0() {
/*  54 */     return this.sprite.getV(this.vo / 4.0F);
/*     */   }
/*     */   
/*     */   protected float getV1() {
/*  59 */     return this.sprite.getV((this.vo + 1.0F) / 4.0F);
/*     */   }
/*     */   
/*     */   public SingleQuadParticle.Layer getLayer() {
/*  64 */     return this.layer;
/*     */   }
/*     */   
/*     */   public static abstract class ItemParticleProvider<T extends ParticleOptions> implements ParticleProvider<T> {
/*  68 */     private final ItemStackRenderState scratchRenderState = new ItemStackRenderState();
/*     */     
/*     */     protected TextureAtlasSprite getSprite(ItemStackTemplate item, ClientLevel level, RandomSource random) {
/*  71 */       Minecraft.getInstance().getItemModelResolver().updateForTopItem(this.scratchRenderState, item.create(), ItemDisplayContext.GROUND, (Level)level, null, 0);
/*  72 */       Material.Baked material = this.scratchRenderState.pickParticleMaterial(random);
/*  73 */       return (material != null) ? material.sprite() : Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.ITEMS).missingSprite();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Provider extends ItemParticleProvider<ItemParticleOption> {
/*     */     public Particle createParticle(ItemParticleOption options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/*  80 */       return new BreakingItemParticle(level, x, y, z, xAux, yAux, zAux, getSprite(options.getItem(), level, random));
/*     */     }
/*     */   }
/*     */   
/*     */   public static class SlimeProvider extends ItemParticleProvider<SimpleParticleType> {
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/*  87 */       return new BreakingItemParticle(level, x, y, z, getSprite(new ItemStackTemplate(Items.SLIME_BALL), level, random));
/*     */     }
/*     */   }
/*     */   
/*     */   public static class SulfurCubeProvider extends ItemParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprites;
/*     */     
/*     */     public SulfurCubeProvider(SpriteSet sprites) {
/*  95 */       this.sprites = sprites;
/*     */     }
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 100 */       return new BreakingItemParticle(level, x, y, z, this.sprites.first());
/*     */     }
/*     */   }
/*     */   
/*     */   public static class CobwebProvider extends ItemParticleProvider<SimpleParticleType> {
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 107 */       return new BreakingItemParticle(level, x, y, z, getSprite(new ItemStackTemplate(Items.COBWEB), level, random));
/*     */     }
/*     */   }
/*     */   
/*     */   public static class SnowballProvider extends ItemParticleProvider<SimpleParticleType> {
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 114 */       return new BreakingItemParticle(level, x, y, z, getSprite(new ItemStackTemplate(Items.SNOWBALL), level, random));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\BreakingItemParticle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */