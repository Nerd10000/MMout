/*   */ package net.minecraft.world.level.levelgen.feature.configurations;
/*   */ 
/*   */ import com.mojang.datafixers.kinds.App;
/*   */ import com.mojang.datafixers.kinds.Applicative;
/*   */ import com.mojang.datafixers.util.Function3;
/*   */ import com.mojang.serialization.Codec;
/*   */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*   */ import net.minecraft.world.level.block.state.BlockState;
/*   */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*   */ 
/*   */ public final class SpikeConfiguration extends Record implements FeatureConfiguration {
/*   */   private final BlockState state;
/*   */   
/*   */   private final BlockPredicate canPlaceOn;
/*   */   
/*   */   private final BlockPredicate canReplace;
/*   */   
/*   */   public static final Codec<SpikeConfiguration> CODEC;
/*   */   
/*   */   public SpikeConfiguration(BlockState state, BlockPredicate canPlaceOn, BlockPredicate canReplace) {
/* 8 */     this.state = state;
/* 8 */     this.canPlaceOn = canPlaceOn;
/* 8 */     this.canReplace = canReplace;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/configurations/SpikeConfiguration;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/SpikeConfiguration;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/configurations/SpikeConfiguration;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/SpikeConfiguration;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/configurations/SpikeConfiguration;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/SpikeConfiguration;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   public BlockState state() {
/* 8 */     return this.state;
/*   */   }
/*   */   
/*   */   public BlockPredicate canPlaceOn() {
/* 8 */     return this.canPlaceOn;
/*   */   }
/*   */   
/*   */   public BlockPredicate canReplace() {
/* 8 */     return this.canReplace;
/*   */   }
/*   */   
/*   */   static {
/* 9 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)BlockState.CODEC.fieldOf("state").forGetter(SpikeConfiguration::state), (App)BlockPredicate.CODEC.fieldOf("can_place_on").forGetter(SpikeConfiguration::canPlaceOn), (App)BlockPredicate.CODEC.fieldOf("can_replace").forGetter(SpikeConfiguration::canReplace)).apply((Applicative)i, SpikeConfiguration::new));
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\configurations\SpikeConfiguration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */