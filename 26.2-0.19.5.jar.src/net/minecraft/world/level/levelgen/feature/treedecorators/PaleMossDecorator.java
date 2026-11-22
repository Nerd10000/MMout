/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Collections;
/*    */ import java.util.Comparator;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.features.VegetationFeatures;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.HangingMossBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ 
/*    */ public class PaleMossDecorator extends TreeDecorator {
/*    */   public static final MapCodec<PaleMossDecorator> CODEC;
/*    */   
/*    */   private final float leavesProbability;
/*    */   
/*    */   private final float trunkProbability;
/*    */   
/*    */   private final float groundProbability;
/*    */   
/*    */   protected TreeDecoratorType<?> type() {
/* 22 */     return TreeDecoratorType.PALE_MOSS;
/*    */   }
/*    */   
/*    */   static {
/* 25 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.floatRange(0.0F, 1.0F).fieldOf("leaves_probability").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("trunk_probability").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("ground_probability").forGetter(())).apply((Applicative)i, PaleMossDecorator::new));
/*    */   }
/*    */   
/*    */   public PaleMossDecorator(float leavesProbability, float trunkProbability, float groundProbability) {
/* 36 */     this.leavesProbability = leavesProbability;
/* 37 */     this.trunkProbability = trunkProbability;
/* 38 */     this.groundProbability = groundProbability;
/*    */   }
/*    */   
/*    */   public void place(TreeDecorator.Context context) {
/* 43 */     RandomSource random = context.random();
/* 45 */     WorldGenLevel level = context.level();
/* 47 */     List<BlockPos> logs = Util.shuffledCopy(context.logs(), random);
/* 48 */     if (logs.isEmpty())
/*    */       return; 
/* 51 */     BlockPos origin = Collections.<BlockPos>min(logs, Comparator.comparingInt(Vec3i::getY));
/* 53 */     if (random.nextFloat() < this.groundProbability)
/* 54 */       level.registryAccess()
/* 55 */         .lookup(Registries.CONFIGURED_FEATURE)
/* 56 */         .flatMap(registry -> registry.get(VegetationFeatures.PALE_MOSS_PATCH))
/* 57 */         .ifPresent(mossPatch -> ((ConfiguredFeature)mossPatch.value()).place(level, level.getLevel().getChunkSource().getGenerator(), random, origin.above())); 
/* 59 */     context.logs().forEach(pos -> {
/*    */           if (random.nextFloat() < this.trunkProbability) {
/*    */             BlockPos down = pos.below();
/*    */             if (context.isAir(down))
/*    */               addMossHanger(down, context); 
/*    */           } 
/*    */         });
/* 67 */     context.leaves().forEach(pos -> {
/*    */           if (random.nextFloat() < this.leavesProbability) {
/*    */             BlockPos down = pos.below();
/*    */             if (context.isAir(down))
/*    */               addMossHanger(down, context); 
/*    */           } 
/*    */         });
/*    */   }
/*    */   
/*    */   private static void addMossHanger(BlockPos pos, TreeDecorator.Context context) {
/* 78 */     while (context.isAir(pos.below()) && 
/* 79 */       context.random().nextFloat() >= 0.5D) {
/* 82 */       context.setBlock(pos, (BlockState)Blocks.PALE_HANGING_MOSS.defaultBlockState().setValue((Property)HangingMossBlock.TIP, Boolean.valueOf(false)));
/* 83 */       pos = pos.below();
/*    */     } 
/* 85 */     context.setBlock(pos, (BlockState)Blocks.PALE_HANGING_MOSS.defaultBlockState().setValue((Property)HangingMossBlock.TIP, Boolean.valueOf(true)));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\treedecorators\PaleMossDecorator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */