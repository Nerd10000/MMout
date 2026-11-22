/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.particles.ColorParticleOption;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.ParticleUtils;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ 
/*    */ public class TintedParticleLeavesBlock extends LeavesBlock {
/*    */   public static final MapCodec<TintedParticleLeavesBlock> CODEC;
/*    */   
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.floatRange(0.0F, 1.0F).fieldOf("leaf_particle_chance").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, TintedParticleLeavesBlock::new));
/*    */   }
/*    */   
/*    */   public TintedParticleLeavesBlock(float leafParticleChance, BlockBehaviour.Properties properties) {
/* 20 */     super(leafParticleChance, properties);
/*    */   }
/*    */   
/*    */   protected void spawnFallingLeavesParticle(Level level, BlockPos pos, RandomSource random) {
/* 25 */     ColorParticleOption particle = ColorParticleOption.create(ParticleTypes.TINTED_LEAVES, level.getClientLeafTintColor(pos));
/* 26 */     ParticleUtils.spawnParticleBelow(level, pos, random, (ParticleOptions)particle);
/*    */   }
/*    */   
/*    */   public MapCodec<? extends TintedParticleLeavesBlock> codec() {
/* 31 */     return CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\TintedParticleLeavesBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */