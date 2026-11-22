/*   */ package net.minecraft.world.level.levelgen.feature.configurations;
/*   */ 
/*   */ import com.mojang.datafixers.kinds.App;
/*   */ import com.mojang.datafixers.kinds.Applicative;
/*   */ import com.mojang.serialization.Codec;
/*   */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*   */ import java.util.function.BiFunction;
/*   */ import net.minecraft.world.level.block.state.BlockState;
/*   */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*   */ 
/*   */ public final class BlockBlobConfiguration extends Record implements FeatureConfiguration {
/*   */   private final BlockState state;
/*   */   
/*   */   private final BlockPredicate canPlaceOn;
/*   */   
/*   */   public static final Codec<BlockBlobConfiguration> CODEC;
/*   */   
/*   */   public BlockBlobConfiguration(BlockState state, BlockPredicate canPlaceOn) {
/* 8 */     this.state = state;
/* 8 */     this.canPlaceOn = canPlaceOn;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/configurations/BlockBlobConfiguration;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/BlockBlobConfiguration;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/configurations/BlockBlobConfiguration;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/BlockBlobConfiguration;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/configurations/BlockBlobConfiguration;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/BlockBlobConfiguration;
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
/*   */   static {
/* 9 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)BlockState.CODEC.fieldOf("state").forGetter(BlockBlobConfiguration::state), (App)BlockPredicate.CODEC.fieldOf("can_place_on").forGetter(BlockBlobConfiguration::canPlaceOn)).apply((Applicative)i, BlockBlobConfiguration::new));
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\configurations\BlockBlobConfiguration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */