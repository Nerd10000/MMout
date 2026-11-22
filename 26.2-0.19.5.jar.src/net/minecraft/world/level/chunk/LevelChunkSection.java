/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeResolver;
/*     */ import net.minecraft.world.level.biome.Climate;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ 
/*     */ public class LevelChunkSection {
/*     */   public static final int BIOME_CONTAINER_BITS = 2;
/*     */   
/*     */   private short nonEmptyBlockCount;
/*     */   
/*     */   private short fluidCount;
/*     */   
/*     */   private short tickingBlockCount;
/*     */   
/*     */   private short tickingFluidCount;
/*     */   
/*     */   private final PalettedContainer<BlockState> states;
/*     */   
/*     */   private PalettedContainerRO<Holder<Biome>> biomes;
/*     */   
/*     */   private LevelChunkSection(LevelChunkSection source) {
/*  26 */     this.nonEmptyBlockCount = source.nonEmptyBlockCount;
/*  27 */     this.fluidCount = source.fluidCount;
/*  28 */     this.tickingBlockCount = source.tickingBlockCount;
/*  29 */     this.tickingFluidCount = source.tickingFluidCount;
/*  30 */     this.states = source.states.copy();
/*  31 */     this.biomes = source.biomes.copy();
/*     */   }
/*     */   
/*     */   public LevelChunkSection(PalettedContainer<BlockState> states, PalettedContainerRO<Holder<Biome>> biomes) {
/*  35 */     this.states = states;
/*  36 */     this.biomes = biomes;
/*  37 */     recalcBlockCounts();
/*     */   }
/*     */   
/*     */   public LevelChunkSection(PalettedContainerFactory containerFactory) {
/*  41 */     this.states = containerFactory.createForBlockStates();
/*  42 */     this.biomes = containerFactory.createForBiomes();
/*     */   }
/*     */   
/*     */   public BlockState getBlockState(int sectionX, int sectionY, int sectionZ) {
/*  46 */     return this.states.get(sectionX, sectionY, sectionZ);
/*     */   }
/*     */   
/*     */   public FluidState getFluidState(int sectionX, int sectionY, int sectionZ) {
/*  50 */     return ((BlockState)this.states.get(sectionX, sectionY, sectionZ)).getFluidState();
/*     */   }
/*     */   
/*     */   public void acquire() {
/*  54 */     this.states.acquire();
/*     */   }
/*     */   
/*     */   public void release() {
/*  58 */     this.states.release();
/*     */   }
/*     */   
/*     */   public BlockState setBlockState(int sectionX, int sectionY, int sectionZ, BlockState state) {
/*  62 */     return setBlockState(sectionX, sectionY, sectionZ, state, true);
/*     */   }
/*     */   
/*     */   public BlockState setBlockState(int sectionX, int sectionY, int sectionZ, BlockState state, boolean checkThreading) {
/*     */     BlockState previous;
/*  67 */     if (checkThreading) {
/*  68 */       previous = this.states.getAndSet(sectionX, sectionY, sectionZ, state);
/*     */     } else {
/*  70 */       previous = this.states.getAndSetUnchecked(sectionX, sectionY, sectionZ, state);
/*     */     } 
/*  73 */     if (!previous.isAir()) {
/*  74 */       this.nonEmptyBlockCount = (short)(this.nonEmptyBlockCount - 1);
/*  75 */       if (previous.isRandomlyTicking())
/*  76 */         this.tickingBlockCount = (short)(this.tickingBlockCount - 1); 
/*  78 */       FluidState previousFluid = previous.getFluidState();
/*  79 */       if (!previousFluid.isEmpty()) {
/*  80 */         this.fluidCount = (short)(this.fluidCount - 1);
/*  81 */         if (previousFluid.isRandomlyTicking())
/*  82 */           this.tickingFluidCount = (short)(this.tickingFluidCount - 1); 
/*     */       } 
/*     */     } 
/*  87 */     if (!state.isAir()) {
/*  88 */       this.nonEmptyBlockCount = (short)(this.nonEmptyBlockCount + 1);
/*  89 */       if (state.isRandomlyTicking())
/*  90 */         this.tickingBlockCount = (short)(this.tickingBlockCount + 1); 
/*  92 */       FluidState fluid = state.getFluidState();
/*  93 */       if (!fluid.isEmpty()) {
/*  94 */         this.fluidCount = (short)(this.fluidCount + 1);
/*  95 */         if (fluid.isRandomlyTicking())
/*  96 */           this.tickingFluidCount = (short)(this.tickingFluidCount + 1); 
/*     */       } 
/*     */     } 
/* 101 */     return previous;
/*     */   }
/*     */   
/*     */   public boolean hasOnlyAir() {
/* 105 */     return (this.nonEmptyBlockCount == 0);
/*     */   }
/*     */   
/*     */   public boolean hasFluid() {
/* 109 */     return (this.fluidCount > 0);
/*     */   }
/*     */   
/*     */   public boolean isRandomlyTicking() {
/* 113 */     return (isRandomlyTickingBlocks() || isRandomlyTickingFluids());
/*     */   }
/*     */   
/*     */   public boolean isRandomlyTickingBlocks() {
/* 117 */     return (this.tickingBlockCount > 0);
/*     */   }
/*     */   
/*     */   public boolean isRandomlyTickingFluids() {
/* 121 */     return (this.tickingFluidCount > 0);
/*     */   }
/*     */   
/*     */   public void recalcBlockCounts() {
/*     */     class BlockCounter implements PalettedContainer.CountConsumer<BlockState> {
/*     */       public int nonEmptyBlockCount;
/*     */       
/*     */       public int fluidCount;
/*     */       
/*     */       public int tickingBlockCount;
/*     */       
/*     */       public int tickingFluidCount;
/*     */       
/*     */       BlockCounter(LevelChunkSection this$0) {}
/*     */       
/*     */       public void accept(BlockState state, int count) {
/* 133 */         if (state.isAir())
/*     */           return; 
/* 136 */         this.nonEmptyBlockCount += count;
/* 137 */         if (state.isRandomlyTicking())
/* 138 */           this.tickingBlockCount += count; 
/* 140 */         FluidState fluid = state.getFluidState();
/* 141 */         if (!fluid.isEmpty()) {
/* 142 */           this.fluidCount += count;
/* 143 */           if (fluid.isRandomlyTicking())
/* 144 */             this.tickingFluidCount += count; 
/*     */         } 
/*     */       }
/*     */     };
/* 150 */     BlockCounter blockCounter = new BlockCounter(this);
/* 151 */     this.states.count(blockCounter);
/* 154 */     this.nonEmptyBlockCount = (short)blockCounter.nonEmptyBlockCount;
/* 155 */     this.fluidCount = (short)blockCounter.fluidCount;
/* 156 */     this.tickingBlockCount = (short)blockCounter.tickingBlockCount;
/* 157 */     this.tickingFluidCount = (short)blockCounter.tickingFluidCount;
/*     */   }
/*     */   
/*     */   public PalettedContainer<BlockState> getStates() {
/* 161 */     return this.states;
/*     */   }
/*     */   
/*     */   public PalettedContainerRO<Holder<Biome>> getBiomes() {
/* 165 */     return this.biomes;
/*     */   }
/*     */   
/*     */   public void read(FriendlyByteBuf buffer) {
/* 169 */     this.nonEmptyBlockCount = buffer.readShort();
/* 170 */     this.fluidCount = buffer.readShort();
/* 171 */     this.states.read(buffer);
/* 172 */     PalettedContainer<Holder<Biome>> biomes = this.biomes.recreate();
/* 173 */     biomes.read(buffer);
/* 174 */     this.biomes = biomes;
/*     */   }
/*     */   
/*     */   public void readBiomes(FriendlyByteBuf buffer) {
/* 178 */     PalettedContainer<Holder<Biome>> biomes = this.biomes.recreate();
/* 179 */     biomes.read(buffer);
/* 180 */     this.biomes = biomes;
/*     */   }
/*     */   
/*     */   public void write(FriendlyByteBuf buffer) {
/* 184 */     buffer.writeShort(this.nonEmptyBlockCount);
/* 185 */     buffer.writeShort(this.fluidCount);
/* 186 */     this.states.write(buffer);
/* 187 */     this.biomes.write(buffer);
/*     */   }
/*     */   
/*     */   public int getSerializedSize() {
/* 191 */     return 4 + this.states.getSerializedSize() + this.biomes.getSerializedSize();
/*     */   }
/*     */   
/*     */   public boolean maybeHas(Predicate<BlockState> predicate) {
/* 195 */     return this.states.maybeHas(predicate);
/*     */   }
/*     */   
/*     */   public Holder<Biome> getNoiseBiome(int quartX, int quartY, int quartZ) {
/* 199 */     return this.biomes.get(quartX, quartY, quartZ);
/*     */   }
/*     */   
/*     */   public void fillBiomesFromNoise(BiomeResolver biomeResolver, Climate.Sampler sampler, int quartMinX, int quartMinY, int quartMinZ) {
/* 203 */     PalettedContainer<Holder<Biome>> newBiomes = this.biomes.recreate();
/* 205 */     int size = 4;
/* 206 */     for (int x = 0; x < 4; x++) {
/* 207 */       for (int y = 0; y < 4; y++) {
/* 208 */         for (int z = 0; z < 4; z++)
/* 209 */           newBiomes.getAndSetUnchecked(x, y, z, biomeResolver.getNoiseBiome(quartMinX + x, quartMinY + y, quartMinZ + z, sampler)); 
/*     */       } 
/*     */     } 
/* 213 */     this.biomes = newBiomes;
/*     */   }
/*     */   
/*     */   public LevelChunkSection copy() {
/* 217 */     return new LevelChunkSection(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\LevelChunkSection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */