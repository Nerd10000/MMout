/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class WebBlock extends Block {
/* 14 */   public static final MapCodec<WebBlock> CODEC = simpleCodec(WebBlock::new);
/*    */   
/*    */   public MapCodec<WebBlock> codec() {
/* 18 */     return CODEC;
/*    */   }
/*    */   
/*    */   public WebBlock(BlockBehaviour.Properties properties) {
/* 22 */     super(properties);
/*    */   }
/*    */   
/*    */   protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/* 27 */     Vec3 speedMultiplier = new Vec3(0.25D, 0.05000000074505806D, 0.25D);
/* 28 */     if (entity instanceof LivingEntity) {
/* 28 */       LivingEntity livingEntity = (LivingEntity)entity;
/* 28 */       if (livingEntity.hasEffect(MobEffects.WEAVING))
/* 29 */         speedMultiplier = new Vec3(0.5D, 0.25D, 0.5D); 
/*    */     } 
/* 31 */     entity.makeStuckInBlock(state, speedMultiplier);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\WebBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */