/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.client.resources.model.sprite.Material;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.data.AtlasIds;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import net.minecraft.world.item.ItemStackTemplate;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public abstract class ItemParticleProvider<T extends ParticleOptions> implements ParticleProvider<T> {
/* 68 */   private final ItemStackRenderState scratchRenderState = new ItemStackRenderState();
/*    */   
/*    */   protected TextureAtlasSprite getSprite(ItemStackTemplate item, ClientLevel level, RandomSource random) {
/* 71 */     Minecraft.getInstance().getItemModelResolver().updateForTopItem(this.scratchRenderState, item.create(), ItemDisplayContext.GROUND, (Level)level, null, 0);
/* 72 */     Material.Baked material = this.scratchRenderState.pickParticleMaterial(random);
/* 73 */     return (material != null) ? material.sprite() : Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.ITEMS).missingSprite();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\BreakingItemParticle$ItemParticleProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */