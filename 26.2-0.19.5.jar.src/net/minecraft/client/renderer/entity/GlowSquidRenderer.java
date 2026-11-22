/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.animal.squid.SquidModel;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.SquidRenderState;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.animal.squid.GlowSquid;
/*    */ 
/*    */ public class GlowSquidRenderer extends SquidRenderer<GlowSquid> {
/* 12 */   private static final Identifier GLOW_SQUID_LOCATION = Identifier.withDefaultNamespace("textures/entity/squid/glow_squid.png");
/*    */   
/* 13 */   private static final Identifier GLOW_SQUID_BABY_LOCATION = Identifier.withDefaultNamespace("textures/entity/squid/glow_squid_baby.png");
/*    */   
/*    */   public GlowSquidRenderer(EntityRendererProvider.Context context, SquidModel model, SquidModel babyModel) {
/* 16 */     super(context, model, babyModel);
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(SquidRenderState state) {
/* 21 */     return state.isBaby ? GLOW_SQUID_BABY_LOCATION : GLOW_SQUID_LOCATION;
/*    */   }
/*    */   
/*    */   protected int getBlockLightLevel(GlowSquid entity, BlockPos blockPos) {
/* 26 */     int glowLightLevel = (int)Mth.clampedLerp(1.0F - entity.getDarkTicksRemaining() / 10.0F, 0.0F, 15.0F);
/* 27 */     if (glowLightLevel == 15)
/* 28 */       return 15; 
/* 30 */     return Math.max(glowLightLevel, super.getBlockLightLevel(entity, blockPos));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\GlowSquidRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */