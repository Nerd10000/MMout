/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function7;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public final class SpeleothemConfiguration extends Record implements FeatureConfiguration {
/*    */   private final BlockState baseBlock;
/*    */   
/*    */   private final BlockState pointedBlock;
/*    */   
/*    */   private final HolderSet<Block> replaceableBlocks;
/*    */   
/*    */   private final float chanceOfTallerGeneration;
/*    */   
/*    */   private final float chanceOfDirectionalSpread;
/*    */   
/*    */   private final float chanceOfSpreadRadius2;
/*    */   
/*    */   private final float chanceOfSpreadRadius3;
/*    */   
/*    */   public static final Codec<SpeleothemConfiguration> CODEC;
/*    */   
/*    */   public SpeleothemConfiguration(BlockState baseBlock, BlockState pointedBlock, HolderSet<Block> replaceableBlocks, float chanceOfTallerGeneration, float chanceOfDirectionalSpread, float chanceOfSpreadRadius2, float chanceOfSpreadRadius3) {
/* 11 */     this.baseBlock = baseBlock;
/* 11 */     this.pointedBlock = pointedBlock;
/* 11 */     this.replaceableBlocks = replaceableBlocks;
/* 11 */     this.chanceOfTallerGeneration = chanceOfTallerGeneration;
/* 11 */     this.chanceOfDirectionalSpread = chanceOfDirectionalSpread;
/* 11 */     this.chanceOfSpreadRadius2 = chanceOfSpreadRadius2;
/* 11 */     this.chanceOfSpreadRadius3 = chanceOfSpreadRadius3;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/configurations/SpeleothemConfiguration;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/SpeleothemConfiguration;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/configurations/SpeleothemConfiguration;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/SpeleothemConfiguration;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/configurations/SpeleothemConfiguration;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/SpeleothemConfiguration;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public BlockState baseBlock() {
/* 11 */     return this.baseBlock;
/*    */   }
/*    */   
/*    */   public BlockState pointedBlock() {
/* 11 */     return this.pointedBlock;
/*    */   }
/*    */   
/*    */   public HolderSet<Block> replaceableBlocks() {
/* 11 */     return this.replaceableBlocks;
/*    */   }
/*    */   
/*    */   public float chanceOfTallerGeneration() {
/* 11 */     return this.chanceOfTallerGeneration;
/*    */   }
/*    */   
/*    */   public float chanceOfDirectionalSpread() {
/* 11 */     return this.chanceOfDirectionalSpread;
/*    */   }
/*    */   
/*    */   public float chanceOfSpreadRadius2() {
/* 11 */     return this.chanceOfSpreadRadius2;
/*    */   }
/*    */   
/*    */   public float chanceOfSpreadRadius3() {
/* 11 */     return this.chanceOfSpreadRadius3;
/*    */   }
/*    */   
/*    */   static {
/* 20 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)BlockState.CODEC.fieldOf("base_block").forGetter(()), (App)BlockState.CODEC.fieldOf("pointed_block").forGetter(()), (App)RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("replaceable_blocks").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).optionalFieldOf("chance_of_taller_generation", Float.valueOf(0.2F)).forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).optionalFieldOf("chance_of_directional_spread", Float.valueOf(0.7F)).forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).optionalFieldOf("chance_of_spread_radius2", Float.valueOf(0.5F)).forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).optionalFieldOf("chance_of_spread_radius3", Float.valueOf(0.5F)).forGetter(())).apply((Applicative)i, SpeleothemConfiguration::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\configurations\SpeleothemConfiguration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */