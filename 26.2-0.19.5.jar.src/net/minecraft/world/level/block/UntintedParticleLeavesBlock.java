/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.ParticleUtils;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ 
/*    */ public class UntintedParticleLeavesBlock extends LeavesBlock {
/*    */   public static final MapCodec<UntintedParticleLeavesBlock> CODEC;
/*    */   
/*    */   protected final ParticleOptions leafParticle;
/*    */   
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.floatRange(0.0F, 1.0F).fieldOf("leaf_particle_chance").forGetter(()), (App)ParticleTypes.CODEC.fieldOf("leaf_particle").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, UntintedParticleLeavesBlock::new));
/*    */   }
/*    */   
/*    */   public UntintedParticleLeavesBlock(float leafParticleChance, ParticleOptions leafParticle, BlockBehaviour.Properties properties) {
/* 23 */     super(leafParticleChance, properties);
/* 24 */     this.leafParticle = leafParticle;
/*    */   }
/*    */   
/*    */   protected void spawnFallingLeavesParticle(Level level, BlockPos pos, RandomSource random) {
/* 29 */     ParticleUtils.spawnParticleBelow(level, pos, random, this.leafParticle);
/*    */   }
/*    */   
/*    */   public MapCodec<UntintedParticleLeavesBlock> codec() {
/* 34 */     return CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\UntintedParticleLeavesBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */