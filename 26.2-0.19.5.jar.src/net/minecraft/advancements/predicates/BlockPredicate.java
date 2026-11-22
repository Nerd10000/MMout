/*     */ package net.minecraft.advancements.predicates;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.RegistryCodecs;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockInWorld;
/*     */ 
/*     */ public final class BlockPredicate extends Record {
/*     */   private final Optional<HolderSet<Block>> blocks;
/*     */   
/*     */   private final Optional<StatePropertiesPredicate> properties;
/*     */   
/*     */   private final Optional<NbtPredicate> nbt;
/*     */   
/*     */   private final DataComponentMatchers components;
/*     */   
/*     */   public static final Codec<BlockPredicate> CODEC;
/*     */   
/*     */   public BlockPredicate(Optional<HolderSet<Block>> blocks, Optional<StatePropertiesPredicate> properties, Optional<NbtPredicate> nbt, DataComponentMatchers components) {
/*  27 */     this.blocks = blocks;
/*  27 */     this.properties = properties;
/*  27 */     this.nbt = nbt;
/*  27 */     this.components = components;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/BlockPredicate;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #27	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/BlockPredicate;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/BlockPredicate;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #27	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/BlockPredicate;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/BlockPredicate;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #27	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/BlockPredicate;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Optional<HolderSet<Block>> blocks() {
/*  27 */     return this.blocks;
/*     */   }
/*     */   
/*     */   public Optional<StatePropertiesPredicate> properties() {
/*  27 */     return this.properties;
/*     */   }
/*     */   
/*     */   public Optional<NbtPredicate> nbt() {
/*  27 */     return this.nbt;
/*     */   }
/*     */   
/*     */   public DataComponentMatchers components() {
/*  27 */     return this.components;
/*     */   }
/*     */   
/*     */   static {
/*  33 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)RegistryCodecs.homogeneousList(Registries.BLOCK).optionalFieldOf("blocks").forGetter(BlockPredicate::blocks), (App)StatePropertiesPredicate.CODEC.optionalFieldOf("state").forGetter(BlockPredicate::properties), (App)NbtPredicate.CODEC.optionalFieldOf("nbt").forGetter(BlockPredicate::nbt), (App)DataComponentMatchers.CODEC.forGetter(BlockPredicate::components)).apply((Applicative)i, BlockPredicate::new));
/*     */   }
/*     */   
/*  40 */   public static final StreamCodec<RegistryFriendlyByteBuf, BlockPredicate> STREAM_CODEC = StreamCodec.composite(
/*  41 */       ByteBufCodecs.optional(ByteBufCodecs.holderSet(Registries.BLOCK)), BlockPredicate::blocks, 
/*  42 */       ByteBufCodecs.optional(StatePropertiesPredicate.STREAM_CODEC), BlockPredicate::properties, 
/*  43 */       ByteBufCodecs.optional(NbtPredicate.STREAM_CODEC), BlockPredicate::nbt, DataComponentMatchers.STREAM_CODEC, BlockPredicate::components, BlockPredicate::new);
/*     */   
/*     */   public boolean matches(ServerLevel level, BlockPos pos) {
/*  49 */     if (!level.isLoaded(pos))
/*  50 */       return false; 
/*  52 */     if (!matchesState(level.getBlockState(pos)))
/*  53 */       return false; 
/*  55 */     if (this.nbt.isPresent() || !this.components.isEmpty()) {
/*  56 */       BlockEntity blockEntity = level.getBlockEntity(pos);
/*  58 */       if (this.nbt.isPresent() && !matchesBlockEntity((LevelReader)level, blockEntity, this.nbt.get()))
/*  59 */         return false; 
/*  62 */       if (!this.components.isEmpty() && !matchesComponents(blockEntity, this.components))
/*  63 */         return false; 
/*     */     } 
/*  66 */     return true;
/*     */   }
/*     */   
/*     */   public boolean matches(BlockInWorld blockInWorld) {
/*  70 */     if (!matchesState(blockInWorld.getState()))
/*  71 */       return false; 
/*  73 */     if (this.nbt.isPresent() && !matchesBlockEntity(blockInWorld.getLevel(), blockInWorld.getEntity(), this.nbt.get()))
/*  74 */       return false; 
/*  76 */     return true;
/*     */   }
/*     */   
/*     */   private boolean matchesState(BlockState state) {
/*  80 */     if (this.blocks.isPresent() && !state.is(this.blocks.get()))
/*  81 */       return false; 
/*  83 */     if (this.properties.isPresent() && !((StatePropertiesPredicate)this.properties.get()).matches(state))
/*  84 */       return false; 
/*  86 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean matchesBlockEntity(LevelReader level, BlockEntity entity, NbtPredicate nbt) {
/*  90 */     return (entity != null && nbt.matches((Tag)entity.saveWithFullMetadata((HolderLookup.Provider)level.registryAccess())));
/*     */   }
/*     */   
/*     */   private static boolean matchesComponents(BlockEntity entity, DataComponentMatchers components) {
/*  94 */     return (entity != null && components.test((DataComponentGetter)entity.collectComponents()));
/*     */   }
/*     */   
/*     */   public boolean requiresNbt() {
/*  98 */     return this.nbt.isPresent();
/*     */   }
/*     */   
/*     */   public static class Builder {
/* 102 */     private Optional<HolderSet<Block>> blocks = Optional.empty();
/*     */     
/* 103 */     private Optional<StatePropertiesPredicate> properties = Optional.empty();
/*     */     
/* 104 */     private Optional<NbtPredicate> nbt = Optional.empty();
/*     */     
/* 105 */     private DataComponentMatchers components = DataComponentMatchers.ANY;
/*     */     
/*     */     public static Builder block() {
/* 111 */       return new Builder();
/*     */     }
/*     */     
/*     */     public Builder of(HolderGetter<Block> lookup, Block... blocks) {
/* 115 */       return of(lookup, Arrays.asList(blocks));
/*     */     }
/*     */     
/*     */     public Builder of(HolderGetter<Block> lookup, Collection<Block> blocks) {
/* 120 */       this.blocks = Optional.of(HolderSet.direct(Block::builtInRegistryHolder, blocks));
/* 121 */       return this;
/*     */     }
/*     */     
/*     */     public Builder of(HolderGetter<Block> lookup, TagKey<Block> tag) {
/* 125 */       this.blocks = Optional.of(lookup.getOrThrow(tag));
/* 126 */       return this;
/*     */     }
/*     */     
/*     */     public Builder hasNbt(CompoundTag nbt) {
/* 130 */       this.nbt = Optional.of(new NbtPredicate(nbt));
/* 131 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setProperties(StatePropertiesPredicate.Builder properties) {
/* 135 */       this.properties = properties.build();
/* 136 */       return this;
/*     */     }
/*     */     
/*     */     public Builder components(DataComponentMatchers components) {
/* 140 */       this.components = components;
/* 141 */       return this;
/*     */     }
/*     */     
/*     */     public BlockPredicate build() {
/* 145 */       return new BlockPredicate(this.blocks, this.properties, this.nbt, this.components);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\BlockPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */