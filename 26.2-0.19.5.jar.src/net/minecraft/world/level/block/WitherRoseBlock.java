/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.Difficulty;
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.component.SuspiciousStewEffects;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class WitherRoseBlock extends FlowerBlock {
/*    */   public static final MapCodec<WitherRoseBlock> CODEC;
/*    */   
/*    */   static {
/* 28 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)EFFECTS_FIELD.forGetter(FlowerBlock::getSuspiciousEffects), (App)propertiesCodec()).apply((Applicative)i, WitherRoseBlock::new));
/*    */   }
/*    */   
/*    */   public MapCodec<WitherRoseBlock> codec() {
/* 35 */     return CODEC;
/*    */   }
/*    */   
/*    */   public WitherRoseBlock(Holder<MobEffect> mobEffect, float effectSeconds, BlockBehaviour.Properties properties) {
/* 39 */     this(makeEffectList(mobEffect, effectSeconds), properties);
/*    */   }
/*    */   
/*    */   public WitherRoseBlock(SuspiciousStewEffects suspiciousStewEffects, BlockBehaviour.Properties properties) {
/* 43 */     super(suspiciousStewEffects, properties);
/*    */   }
/*    */   
/*    */   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
/* 48 */     return state.is(BlockTags.SUPPORTS_WITHER_ROSE);
/*    */   }
/*    */   
/*    */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 53 */     VoxelShape shape = getShape(state, (BlockGetter)level, pos, CollisionContext.empty());
/* 54 */     Vec3 shapeCenter = shape.bounds().getCenter();
/* 55 */     double x = pos.getX() + shapeCenter.x;
/* 56 */     double z = pos.getZ() + shapeCenter.z;
/* 57 */     for (int i = 0; i < 3; i++) {
/* 58 */       if (random.nextBoolean())
/* 59 */         level.addParticle((ParticleOptions)ParticleTypes.SMOKE, x + random.nextDouble() / 5.0D, pos.getY() + 0.5D - random.nextDouble(), z + random.nextDouble() / 5.0D, 0.0D, 0.0D, 0.0D); 
/*    */     } 
/*    */   }
/*    */   
/*    */   protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/* 66 */     if (level instanceof ServerLevel) {
/* 66 */       ServerLevel serverLevel = (ServerLevel)level;
/* 66 */       if (level
/* 67 */         .getDifficulty() != Difficulty.PEACEFUL && entity instanceof LivingEntity) {
/* 68 */         LivingEntity livingEntity = (LivingEntity)entity;
/* 69 */         if (!livingEntity.isInvulnerableTo(serverLevel, level.damageSources().wither()))
/* 70 */           livingEntity.addEffect(getBeeInteractionEffect()); 
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public MobEffectInstance getBeeInteractionEffect() {
/* 76 */     return new MobEffectInstance(MobEffects.WITHER, 40);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\WitherRoseBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */