/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function8;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ 
/*    */ public final class GeodeBlockSettings extends Record {
/*    */   private final BlockStateProvider fillingProvider;
/*    */   
/*    */   private final BlockStateProvider innerLayerProvider;
/*    */   
/*    */   private final BlockStateProvider alternateInnerLayerProvider;
/*    */   
/*    */   private final BlockStateProvider middleLayerProvider;
/*    */   
/*    */   private final BlockStateProvider outerLayerProvider;
/*    */   
/*    */   private final List<BlockState> innerPlacements;
/*    */   
/*    */   private final HolderSet<Block> cannotReplace;
/*    */   
/*    */   private final HolderSet<Block> invalidBlocks;
/*    */   
/*    */   public static final Codec<GeodeBlockSettings> CODEC;
/*    */   
/*    */   public GeodeBlockSettings(BlockStateProvider fillingProvider, BlockStateProvider innerLayerProvider, BlockStateProvider alternateInnerLayerProvider, BlockStateProvider middleLayerProvider, BlockStateProvider outerLayerProvider, List<BlockState> innerPlacements, HolderSet<Block> cannotReplace, HolderSet<Block> invalidBlocks) {
/* 15 */     this.fillingProvider = fillingProvider;
/* 15 */     this.innerLayerProvider = innerLayerProvider;
/* 15 */     this.alternateInnerLayerProvider = alternateInnerLayerProvider;
/* 15 */     this.middleLayerProvider = middleLayerProvider;
/* 15 */     this.outerLayerProvider = outerLayerProvider;
/* 15 */     this.innerPlacements = innerPlacements;
/* 15 */     this.cannotReplace = cannotReplace;
/* 15 */     this.invalidBlocks = invalidBlocks;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/GeodeBlockSettings;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/GeodeBlockSettings;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/GeodeBlockSettings;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/GeodeBlockSettings;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/GeodeBlockSettings;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/GeodeBlockSettings;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public BlockStateProvider fillingProvider() {
/* 15 */     return this.fillingProvider;
/*    */   }
/*    */   
/*    */   public BlockStateProvider innerLayerProvider() {
/* 15 */     return this.innerLayerProvider;
/*    */   }
/*    */   
/*    */   public BlockStateProvider alternateInnerLayerProvider() {
/* 15 */     return this.alternateInnerLayerProvider;
/*    */   }
/*    */   
/*    */   public BlockStateProvider middleLayerProvider() {
/* 15 */     return this.middleLayerProvider;
/*    */   }
/*    */   
/*    */   public BlockStateProvider outerLayerProvider() {
/* 15 */     return this.outerLayerProvider;
/*    */   }
/*    */   
/*    */   public List<BlockState> innerPlacements() {
/* 15 */     return this.innerPlacements;
/*    */   }
/*    */   
/*    */   public HolderSet<Block> cannotReplace() {
/* 15 */     return this.cannotReplace;
/*    */   }
/*    */   
/*    */   public HolderSet<Block> invalidBlocks() {
/* 15 */     return this.invalidBlocks;
/*    */   }
/*    */   
/*    */   static {
/* 25 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)BlockStateProvider.CODEC.fieldOf("filling_provider").forGetter(GeodeBlockSettings::fillingProvider), (App)BlockStateProvider.CODEC.fieldOf("inner_layer_provider").forGetter(GeodeBlockSettings::innerLayerProvider), (App)BlockStateProvider.CODEC.fieldOf("alternate_inner_layer_provider").forGetter(GeodeBlockSettings::alternateInnerLayerProvider), (App)BlockStateProvider.CODEC.fieldOf("middle_layer_provider").forGetter(GeodeBlockSettings::middleLayerProvider), (App)BlockStateProvider.CODEC.fieldOf("outer_layer_provider").forGetter(GeodeBlockSettings::outerLayerProvider), (App)ExtraCodecs.nonEmptyList(BlockState.CODEC.listOf()).fieldOf("inner_placements").forGetter(GeodeBlockSettings::innerPlacements), (App)RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("cannot_replace").forGetter(GeodeBlockSettings::cannotReplace), (App)RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("invalid_blocks").forGetter(GeodeBlockSettings::invalidBlocks)).apply((Applicative)i, GeodeBlockSettings::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\GeodeBlockSettings.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */