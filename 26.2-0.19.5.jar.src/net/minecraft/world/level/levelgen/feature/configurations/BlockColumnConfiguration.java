/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.util.valueproviders.IntProviders;
/*    */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ 
/*    */ public final class BlockColumnConfiguration extends Record implements FeatureConfiguration {
/*    */   private final List<Layer> layers;
/*    */   
/*    */   private final Direction direction;
/*    */   
/*    */   private final BlockPredicate allowedPlacement;
/*    */   
/*    */   private final boolean prioritizeTip;
/*    */   
/*    */   public static final Codec<BlockColumnConfiguration> CODEC;
/*    */   
/*    */   public BlockColumnConfiguration(List<Layer> layers, Direction direction, BlockPredicate allowedPlacement, boolean prioritizeTip) {
/* 13 */     this.layers = layers;
/* 13 */     this.direction = direction;
/* 13 */     this.allowedPlacement = allowedPlacement;
/* 13 */     this.prioritizeTip = prioritizeTip;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public List<Layer> layers() {
/* 13 */     return this.layers;
/*    */   }
/*    */   
/*    */   public Direction direction() {
/* 13 */     return this.direction;
/*    */   }
/*    */   
/*    */   public BlockPredicate allowedPlacement() {
/* 13 */     return this.allowedPlacement;
/*    */   }
/*    */   
/*    */   public boolean prioritizeTip() {
/* 13 */     return this.prioritizeTip;
/*    */   }
/*    */   
/*    */   static {
/* 15 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Layer.CODEC.listOf().fieldOf("layers").forGetter(BlockColumnConfiguration::layers), (App)Direction.CODEC.fieldOf("direction").forGetter(BlockColumnConfiguration::direction), (App)BlockPredicate.CODEC.fieldOf("allowed_placement").forGetter(BlockColumnConfiguration::allowedPlacement), (App)Codec.BOOL.fieldOf("prioritize_tip").forGetter(BlockColumnConfiguration::prioritizeTip)).apply((Applicative)i, BlockColumnConfiguration::new));
/*    */   }
/*    */   
/*    */   public static final class Layer extends Record {
/*    */     private final IntProvider height;
/*    */     
/*    */     private final BlockStateProvider state;
/*    */     
/*    */     public static final Codec<Layer> CODEC;
/*    */     
/*    */     public Layer(IntProvider height, BlockStateProvider state) {
/* 22 */       this.height = height;
/* 22 */       this.state = state;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration$Layer;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #22	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration$Layer;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration$Layer;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #22	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration$Layer;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration$Layer;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #22	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration$Layer;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public IntProvider height() {
/* 22 */       return this.height;
/*    */     }
/*    */     
/*    */     public BlockStateProvider state() {
/* 22 */       return this.state;
/*    */     }
/*    */     
/*    */     static {
/* 23 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)IntProviders.NON_NEGATIVE_CODEC.fieldOf("height").forGetter(Layer::height), (App)BlockStateProvider.CODEC.fieldOf("provider").forGetter(Layer::state)).apply((Applicative)i, Layer::new));
/*    */     }
/*    */   }
/*    */   
/*    */   public static Layer layer(IntProvider height, BlockStateProvider state) {
/* 30 */     return new Layer(height, state);
/*    */   }
/*    */   
/*    */   public static BlockColumnConfiguration simple(IntProvider height, BlockStateProvider state) {
/* 34 */     return new BlockColumnConfiguration(List.of(layer(height, state)), Direction.UP, BlockPredicate.ONLY_IN_AIR_PREDICATE, false);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\configurations\BlockColumnConfiguration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */