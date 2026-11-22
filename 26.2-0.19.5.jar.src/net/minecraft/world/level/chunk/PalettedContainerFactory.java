/*    */ package net.minecraft.world.level.chunk;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.IdMap;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.biome.Biomes;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public final class PalettedContainerFactory extends Record {
/*    */   private final Strategy<BlockState> blockStatesStrategy;
/*    */   
/*    */   private final BlockState defaultBlockState;
/*    */   
/*    */   private final Codec<PalettedContainer<BlockState>> blockStatesContainerCodec;
/*    */   
/*    */   private final Strategy<Holder<Biome>> biomeStrategy;
/*    */   
/*    */   private final Holder<Biome> defaultBiome;
/*    */   
/*    */   private final Codec<PalettedContainerRO<Holder<Biome>>> biomeContainerCodec;
/*    */   
/*    */   public PalettedContainerFactory(Strategy<BlockState> blockStatesStrategy, BlockState defaultBlockState, Codec<PalettedContainer<BlockState>> blockStatesContainerCodec, Strategy<Holder<Biome>> biomeStrategy, Holder<Biome> defaultBiome, Codec<PalettedContainerRO<Holder<Biome>>> biomeContainerCodec) {
/* 14 */     this.blockStatesStrategy = blockStatesStrategy;
/* 14 */     this.defaultBlockState = defaultBlockState;
/* 14 */     this.blockStatesContainerCodec = blockStatesContainerCodec;
/* 14 */     this.biomeStrategy = biomeStrategy;
/* 14 */     this.defaultBiome = defaultBiome;
/* 14 */     this.biomeContainerCodec = biomeContainerCodec;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/chunk/PalettedContainerFactory;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/chunk/PalettedContainerFactory;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/chunk/PalettedContainerFactory;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/chunk/PalettedContainerFactory;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/chunk/PalettedContainerFactory;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/chunk/PalettedContainerFactory;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Strategy<BlockState> blockStatesStrategy() {
/* 14 */     return this.blockStatesStrategy;
/*    */   }
/*    */   
/*    */   public BlockState defaultBlockState() {
/* 14 */     return this.defaultBlockState;
/*    */   }
/*    */   
/*    */   public Codec<PalettedContainer<BlockState>> blockStatesContainerCodec() {
/* 14 */     return this.blockStatesContainerCodec;
/*    */   }
/*    */   
/*    */   public Strategy<Holder<Biome>> biomeStrategy() {
/* 14 */     return this.biomeStrategy;
/*    */   }
/*    */   
/*    */   public Holder<Biome> defaultBiome() {
/* 14 */     return this.defaultBiome;
/*    */   }
/*    */   
/*    */   public Codec<PalettedContainerRO<Holder<Biome>>> biomeContainerCodec() {
/* 14 */     return this.biomeContainerCodec;
/*    */   }
/*    */   
/*    */   public static PalettedContainerFactory create(RegistryAccess registries) {
/* 24 */     Strategy<BlockState> blockStateStrategy = Strategy.createForBlockStates((IdMap<BlockState>)Block.BLOCK_STATE_REGISTRY);
/* 25 */     BlockState defaultBlockState = Blocks.AIR.defaultBlockState();
/* 27 */     Registry<Biome> biomes = registries.lookupOrThrow(Registries.BIOME);
/* 29 */     Strategy<Holder<Biome>> biomeStrategy = Strategy.createForBiomes(biomes.asHolderIdMap());
/* 30 */     Holder.Reference<Biome> defaultBiome = biomes.getOrThrow(Biomes.PLAINS);
/* 32 */     return new PalettedContainerFactory(blockStateStrategy, defaultBlockState, 
/*    */         
/* 35 */         PalettedContainer.codecRW(BlockState.CODEC, blockStateStrategy, defaultBlockState), biomeStrategy, (Holder<Biome>)defaultBiome, 
/*    */         
/* 39 */         (Codec)PalettedContainer.codecRO(biomes.holderByNameCodec(), biomeStrategy, defaultBiome));
/*    */   }
/*    */   
/*    */   public PalettedContainer<BlockState> createForBlockStates() {
/* 44 */     return new PalettedContainer<>(this.defaultBlockState, this.blockStatesStrategy);
/*    */   }
/*    */   
/*    */   public PalettedContainer<Holder<Biome>> createForBiomes() {
/* 48 */     return new PalettedContainer<>(this.defaultBiome, this.biomeStrategy);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\PalettedContainerFactory.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */