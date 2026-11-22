/*     */ package net.minecraft.advancements.predicates;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.datafixers.util.Function9;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.RegistryCodecs;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.CampfireBlock;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ 
/*     */ public final class LocationPredicate extends Record {
/*     */   private final Optional<PositionPredicate> position;
/*     */   
/*     */   private final Optional<HolderSet<Biome>> biomes;
/*     */   
/*     */   private final Optional<HolderSet<Structure>> structures;
/*     */   
/*     */   private final Optional<ResourceKey<Level>> dimension;
/*     */   
/*     */   private final Optional<Boolean> smokey;
/*     */   
/*     */   private final Optional<LightPredicate> light;
/*     */   
/*     */   private final Optional<BlockPredicate> block;
/*     */   
/*     */   private final Optional<FluidPredicate> fluid;
/*     */   
/*     */   private final Optional<Boolean> canSeeSky;
/*     */   
/*     */   public static final Codec<LocationPredicate> CODEC;
/*     */   
/*     */   public LocationPredicate(Optional<PositionPredicate> position, Optional<HolderSet<Biome>> biomes, Optional<HolderSet<Structure>> structures, Optional<ResourceKey<Level>> dimension, Optional<Boolean> smokey, Optional<LightPredicate> light, Optional<BlockPredicate> block, Optional<FluidPredicate> fluid, Optional<Boolean> canSeeSky) {
/*  19 */     this.position = position;
/*  19 */     this.biomes = biomes;
/*  19 */     this.structures = structures;
/*  19 */     this.dimension = dimension;
/*  19 */     this.smokey = smokey;
/*  19 */     this.light = light;
/*  19 */     this.block = block;
/*  19 */     this.fluid = fluid;
/*  19 */     this.canSeeSky = canSeeSky;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/LocationPredicate;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #19	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/LocationPredicate;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/LocationPredicate;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #19	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/LocationPredicate;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/LocationPredicate;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #19	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/LocationPredicate;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Optional<PositionPredicate> position() {
/*  19 */     return this.position;
/*     */   }
/*     */   
/*     */   public Optional<HolderSet<Biome>> biomes() {
/*  19 */     return this.biomes;
/*     */   }
/*     */   
/*     */   public Optional<HolderSet<Structure>> structures() {
/*  19 */     return this.structures;
/*     */   }
/*     */   
/*     */   public Optional<ResourceKey<Level>> dimension() {
/*  19 */     return this.dimension;
/*     */   }
/*     */   
/*     */   public Optional<Boolean> smokey() {
/*  19 */     return this.smokey;
/*     */   }
/*     */   
/*     */   public Optional<LightPredicate> light() {
/*  19 */     return this.light;
/*     */   }
/*     */   
/*     */   public Optional<BlockPredicate> block() {
/*  19 */     return this.block;
/*     */   }
/*     */   
/*     */   public Optional<FluidPredicate> fluid() {
/*  19 */     return this.fluid;
/*     */   }
/*     */   
/*     */   public Optional<Boolean> canSeeSky() {
/*  19 */     return this.canSeeSky;
/*     */   }
/*     */   
/*     */   static {
/*  30 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)PositionPredicate.CODEC.optionalFieldOf("position").forGetter(LocationPredicate::position), (App)RegistryCodecs.homogeneousList(Registries.BIOME).optionalFieldOf("biomes").forGetter(LocationPredicate::biomes), (App)RegistryCodecs.homogeneousList(Registries.STRUCTURE).optionalFieldOf("structures").forGetter(LocationPredicate::structures), (App)ResourceKey.codec(Registries.DIMENSION).optionalFieldOf("dimension").forGetter(LocationPredicate::dimension), (App)Codec.BOOL.optionalFieldOf("smokey").forGetter(LocationPredicate::smokey), (App)LightPredicate.CODEC.optionalFieldOf("light").forGetter(LocationPredicate::light), (App)BlockPredicate.CODEC.optionalFieldOf("block").forGetter(LocationPredicate::block), (App)FluidPredicate.CODEC.optionalFieldOf("fluid").forGetter(LocationPredicate::fluid), (App)Codec.BOOL.optionalFieldOf("can_see_sky").forGetter(LocationPredicate::canSeeSky)).apply((Applicative)i, LocationPredicate::new));
/*     */   }
/*     */   
/*     */   public boolean matches(ServerLevel level, double x, double y, double z) {
/*  43 */     if (this.position.isPresent() && !((PositionPredicate)this.position.get()).matches(x, y, z))
/*  44 */       return false; 
/*  47 */     if (this.dimension.isPresent() && this.dimension.get() != level.dimension())
/*  48 */       return false; 
/*  51 */     BlockPos pos = BlockPos.containing(x, y, z);
/*  52 */     boolean loaded = level.isLoaded(pos);
/*  54 */     if (this.biomes.isPresent() && (!loaded || !((HolderSet)this.biomes.get()).contains(level.getBiome(pos))))
/*  55 */       return false; 
/*  57 */     if (this.structures.isPresent() && (!loaded || !level.structureManager().getStructureWithPieceAt(pos, this.structures.get()).isValid()))
/*  58 */       return false; 
/*  60 */     if (this.smokey.isPresent() && (!loaded || ((Boolean)this.smokey.get()).booleanValue() != CampfireBlock.isSmokeyPos((Level)level, pos)))
/*  61 */       return false; 
/*  63 */     if (this.light.isPresent() && !((LightPredicate)this.light.get()).matches(level, pos))
/*  64 */       return false; 
/*  66 */     if (this.block.isPresent() && !((BlockPredicate)this.block.get()).matches(level, pos))
/*  67 */       return false; 
/*  69 */     if (this.fluid.isPresent() && !((FluidPredicate)this.fluid.get()).matches(level, pos))
/*  70 */       return false; 
/*  72 */     if (this.canSeeSky.isPresent() && ((Boolean)this.canSeeSky.get()).booleanValue() != level.canSeeSky(pos))
/*  73 */       return false; 
/*  76 */     return true;
/*     */   }
/*     */   
/*     */   private static final class PositionPredicate extends Record {
/*     */     private final MinMaxBounds.Doubles x;
/*     */     
/*     */     private final MinMaxBounds.Doubles y;
/*     */     
/*     */     private final MinMaxBounds.Doubles z;
/*     */     
/*     */     public static final Codec<PositionPredicate> CODEC;
/*     */     
/*     */     private PositionPredicate(MinMaxBounds.Doubles x, MinMaxBounds.Doubles y, MinMaxBounds.Doubles z) {
/*  79 */       this.x = x;
/*  79 */       this.y = y;
/*  79 */       this.z = z;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/LocationPredicate$PositionPredicate;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #79	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/predicates/LocationPredicate$PositionPredicate;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/LocationPredicate$PositionPredicate;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #79	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/predicates/LocationPredicate$PositionPredicate;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/LocationPredicate$PositionPredicate;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #79	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/advancements/predicates/LocationPredicate$PositionPredicate;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public MinMaxBounds.Doubles x() {
/*  79 */       return this.x;
/*     */     }
/*     */     
/*     */     public MinMaxBounds.Doubles y() {
/*  79 */       return this.y;
/*     */     }
/*     */     
/*     */     public MinMaxBounds.Doubles z() {
/*  79 */       return this.z;
/*     */     }
/*     */     
/*     */     static {
/*  80 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)MinMaxBounds.Doubles.CODEC.optionalFieldOf("x", MinMaxBounds.Doubles.ANY).forGetter(PositionPredicate::x), (App)MinMaxBounds.Doubles.CODEC.optionalFieldOf("y", MinMaxBounds.Doubles.ANY).forGetter(PositionPredicate::y), (App)MinMaxBounds.Doubles.CODEC.optionalFieldOf("z", MinMaxBounds.Doubles.ANY).forGetter(PositionPredicate::z)).apply((Applicative)i, PositionPredicate::new));
/*     */     }
/*     */     
/*     */     private static Optional<PositionPredicate> of(MinMaxBounds.Doubles x, MinMaxBounds.Doubles y, MinMaxBounds.Doubles z) {
/*  87 */       if (x.isAny() && y.isAny() && z.isAny())
/*  88 */         return Optional.empty(); 
/*  90 */       return Optional.of(new PositionPredicate(x, y, z));
/*     */     }
/*     */     
/*     */     public boolean matches(double x, double y, double z) {
/*  94 */       return (this.x.matches(x) && this.y.matches(y) && this.z.matches(z));
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Builder {
/*     */     private MinMaxBounds.Doubles x;
/*     */     
/*     */     private MinMaxBounds.Doubles y;
/*     */     
/*     */     private MinMaxBounds.Doubles z;
/*     */     
/*     */     private Optional<HolderSet<Biome>> biomes;
/*     */     
/*     */     private Optional<HolderSet<Structure>> structures;
/*     */     
/*     */     private Optional<ResourceKey<Level>> dimension;
/*     */     
/*     */     private Optional<Boolean> smokey;
/*     */     
/*     */     private Optional<LightPredicate> light;
/*     */     
/*     */     private Optional<BlockPredicate> block;
/*     */     
/*     */     private Optional<FluidPredicate> fluid;
/*     */     
/*     */     private Optional<Boolean> canSeeSky;
/*     */     
/*     */     public Builder() {
/*  99 */       this.x = MinMaxBounds.Doubles.ANY;
/* 100 */       this.y = MinMaxBounds.Doubles.ANY;
/* 101 */       this.z = MinMaxBounds.Doubles.ANY;
/* 103 */       this.biomes = Optional.empty();
/* 104 */       this.structures = Optional.empty();
/* 105 */       this.dimension = Optional.empty();
/* 106 */       this.smokey = Optional.empty();
/* 108 */       this.light = Optional.empty();
/* 109 */       this.block = Optional.empty();
/* 110 */       this.fluid = Optional.empty();
/* 111 */       this.canSeeSky = Optional.empty();
/*     */     }
/*     */     
/*     */     public static Builder location() {
/* 114 */       return new Builder();
/*     */     }
/*     */     
/*     */     public static Builder inBiome(Holder<Biome> biome) {
/* 118 */       return location().setBiomes((HolderSet<Biome>)HolderSet.direct(new Holder[] { biome }));
/*     */     }
/*     */     
/*     */     public static Builder inDimension(ResourceKey<Level> dimension) {
/* 122 */       return location().setDimension(dimension);
/*     */     }
/*     */     
/*     */     public static Builder inStructure(Holder<Structure> structure) {
/* 126 */       return location().setStructures((HolderSet<Structure>)HolderSet.direct(new Holder[] { structure }));
/*     */     }
/*     */     
/*     */     public static Builder atYLocation(MinMaxBounds.Doubles yLocation) {
/* 130 */       return location().setY(yLocation);
/*     */     }
/*     */     
/*     */     public Builder setX(MinMaxBounds.Doubles x) {
/* 134 */       this.x = x;
/* 135 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setY(MinMaxBounds.Doubles y) {
/* 139 */       this.y = y;
/* 140 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setZ(MinMaxBounds.Doubles z) {
/* 144 */       this.z = z;
/* 145 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setBiomes(HolderSet<Biome> biomes) {
/* 149 */       this.biomes = Optional.of(biomes);
/* 150 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setStructures(HolderSet<Structure> structures) {
/* 154 */       this.structures = Optional.of(structures);
/* 155 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setDimension(ResourceKey<Level> dimension) {
/* 159 */       this.dimension = Optional.of(dimension);
/* 160 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setLight(LightPredicate.Builder light) {
/* 164 */       this.light = Optional.of(light.build());
/* 165 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setBlock(BlockPredicate.Builder block) {
/* 169 */       this.block = Optional.of(block.build());
/* 170 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setFluid(FluidPredicate.Builder fluid) {
/* 174 */       this.fluid = Optional.of(fluid.build());
/* 175 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setSmokey(boolean smokey) {
/* 179 */       this.smokey = Optional.of(Boolean.valueOf(smokey));
/* 180 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setCanSeeSky(boolean canSeeSky) {
/* 184 */       this.canSeeSky = Optional.of(Boolean.valueOf(canSeeSky));
/* 185 */       return this;
/*     */     }
/*     */     
/*     */     public LocationPredicate build() {
/* 189 */       Optional<LocationPredicate.PositionPredicate> position = LocationPredicate.PositionPredicate.of(this.x, this.y, this.z);
/* 190 */       return new LocationPredicate(position, this.biomes, this.structures, this.dimension, this.smokey, this.light, this.block, this.fluid, this.canSeeSky);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\LocationPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */