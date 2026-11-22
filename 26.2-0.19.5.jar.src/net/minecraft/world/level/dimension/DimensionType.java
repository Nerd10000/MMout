/*     */ package net.minecraft.world.level.dimension;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function16;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.RegistryCodecs;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.resources.RegistryFileCodec;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.util.valueproviders.IntProviders;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributeMap;
/*     */ import net.minecraft.world.clock.WorldClock;
/*     */ import net.minecraft.world.level.CardinalLighting;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.timeline.Timeline;
/*     */ 
/*     */ public final class DimensionType extends Record {
/*     */   private final boolean hasFixedTime;
/*     */   
/*     */   private final boolean hasSkyLight;
/*     */   
/*     */   private final boolean hasCeiling;
/*     */   
/*     */   private final boolean hasEnderDragonFight;
/*     */   
/*     */   private final double coordinateScale;
/*     */   
/*     */   private final int minY;
/*     */   
/*     */   private final int height;
/*     */   
/*     */   private final int logicalHeight;
/*     */   
/*     */   private final HolderSet<Block> infiniburn;
/*     */   
/*     */   private final float ambientLight;
/*     */   
/*     */   private final MonsterSettings monsterSettings;
/*     */   
/*     */   private final Skybox skybox;
/*     */   
/*     */   private final CardinalLighting.Type cardinalLightType;
/*     */   
/*     */   private final EnvironmentAttributeMap attributes;
/*     */   
/*     */   private final HolderSet<Timeline> timelines;
/*     */   
/*     */   private final Optional<Holder<WorldClock>> defaultClock;
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/dimension/DimensionType;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #33	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/dimension/DimensionType;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/dimension/DimensionType;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #33	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/dimension/DimensionType;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/dimension/DimensionType;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #33	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/dimension/DimensionType;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public boolean hasFixedTime() {
/*  33 */     return this.hasFixedTime;
/*     */   }
/*     */   
/*     */   public boolean hasSkyLight() {
/*  33 */     return this.hasSkyLight;
/*     */   }
/*     */   
/*     */   public boolean hasCeiling() {
/*  33 */     return this.hasCeiling;
/*     */   }
/*     */   
/*     */   public boolean hasEnderDragonFight() {
/*  33 */     return this.hasEnderDragonFight;
/*     */   }
/*     */   
/*     */   public double coordinateScale() {
/*  33 */     return this.coordinateScale;
/*     */   }
/*     */   
/*     */   public int minY() {
/*  33 */     return this.minY;
/*     */   }
/*     */   
/*     */   public int height() {
/*  33 */     return this.height;
/*     */   }
/*     */   
/*     */   public int logicalHeight() {
/*  33 */     return this.logicalHeight;
/*     */   }
/*     */   
/*     */   public HolderSet<Block> infiniburn() {
/*  33 */     return this.infiniburn;
/*     */   }
/*     */   
/*     */   public float ambientLight() {
/*  33 */     return this.ambientLight;
/*     */   }
/*     */   
/*     */   public MonsterSettings monsterSettings() {
/*  33 */     return this.monsterSettings;
/*     */   }
/*     */   
/*     */   public Skybox skybox() {
/*  33 */     return this.skybox;
/*     */   }
/*     */   
/*     */   public CardinalLighting.Type cardinalLightType() {
/*  33 */     return this.cardinalLightType;
/*     */   }
/*     */   
/*     */   public EnvironmentAttributeMap attributes() {
/*  33 */     return this.attributes;
/*     */   }
/*     */   
/*     */   public HolderSet<Timeline> timelines() {
/*  33 */     return this.timelines;
/*     */   }
/*     */   
/*     */   public Optional<Holder<WorldClock>> defaultClock() {
/*  33 */     return this.defaultClock;
/*     */   }
/*     */   
/*  52 */   public static final int BITS_FOR_Y = BlockPos.PACKED_Y_LENGTH;
/*     */   
/*     */   public static final int MIN_HEIGHT = 16;
/*     */   
/*  55 */   public static final int Y_SIZE = (1 << BITS_FOR_Y) - 32;
/*     */   
/*  57 */   public static final int MAX_Y = (Y_SIZE >> 1) - 1;
/*     */   
/*  59 */   public static final int MIN_Y = MAX_Y - Y_SIZE + 1;
/*     */   
/*  62 */   public static final int WAY_ABOVE_MAX_Y = MAX_Y << 4;
/*     */   
/*  64 */   public static final int WAY_BELOW_MIN_Y = MIN_Y << 4;
/*     */   
/*     */   public static final class MonsterSettings extends Record {
/*     */     private final IntProvider monsterSpawnLightTest;
/*     */     
/*     */     private final int monsterSpawnBlockLightLimit;
/*     */     
/*     */     public static final MapCodec<MonsterSettings> CODEC;
/*     */     
/*     */     public MonsterSettings(IntProvider monsterSpawnLightTest, int monsterSpawnBlockLightLimit) {
/*  66 */       this.monsterSpawnLightTest = monsterSpawnLightTest;
/*  66 */       this.monsterSpawnBlockLightLimit = monsterSpawnBlockLightLimit;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/dimension/DimensionType$MonsterSettings;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #66	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/dimension/DimensionType$MonsterSettings;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/dimension/DimensionType$MonsterSettings;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #66	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/dimension/DimensionType$MonsterSettings;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/dimension/DimensionType$MonsterSettings;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #66	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/dimension/DimensionType$MonsterSettings;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public IntProvider monsterSpawnLightTest() {
/*  66 */       return this.monsterSpawnLightTest;
/*     */     }
/*     */     
/*     */     public int monsterSpawnBlockLightLimit() {
/*  66 */       return this.monsterSpawnBlockLightLimit;
/*     */     }
/*     */     
/*     */     static {
/*  70 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)IntProviders.codec(0, 15).fieldOf("monster_spawn_light_level").forGetter(MonsterSettings::monsterSpawnLightTest), (App)Codec.intRange(0, 15).fieldOf("monster_spawn_block_light_limit").forGetter(MonsterSettings::monsterSpawnBlockLightLimit)).apply((Applicative)i, MonsterSettings::new));
/*     */     }
/*     */   }
/*     */   
/*  76 */   public static final Codec<DimensionType> DIRECT_CODEC = createDirectCodec(EnvironmentAttributeMap.CODEC);
/*     */   
/*  77 */   public static final Codec<DimensionType> NETWORK_CODEC = createDirectCodec(EnvironmentAttributeMap.NETWORK_CODEC);
/*     */   
/*     */   private static Codec<DimensionType> createDirectCodec(Codec<EnvironmentAttributeMap> attributeMapCodec) {
/*  80 */     return ExtraCodecs.catchDecoderException(RecordCodecBuilder.create(i -> i.group((App)Codec.BOOL.optionalFieldOf("has_fixed_time", Boolean.valueOf(false)).forGetter(DimensionType::hasFixedTime), (App)Codec.BOOL.fieldOf("has_skylight").forGetter(DimensionType::hasSkyLight), (App)Codec.BOOL.fieldOf("has_ceiling").forGetter(DimensionType::hasCeiling), (App)Codec.BOOL.fieldOf("has_ender_dragon_fight").forGetter(DimensionType::hasEnderDragonFight), (App)Codec.doubleRange(9.999999747378752E-6D, 3.0E7D).fieldOf("coordinate_scale").forGetter(DimensionType::coordinateScale), (App)Codec.intRange(MIN_Y, MAX_Y).fieldOf("min_y").forGetter(DimensionType::minY), (App)Codec.intRange(16, Y_SIZE).fieldOf("height").forGetter(DimensionType::height), (App)Codec.intRange(0, Y_SIZE).fieldOf("logical_height").forGetter(DimensionType::logicalHeight), (App)RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("infiniburn").forGetter(DimensionType::infiniburn), (App)Codec.FLOAT.fieldOf("ambient_light").forGetter(DimensionType::ambientLight), (App)MonsterSettings.CODEC.forGetter(DimensionType::monsterSettings), (App)Skybox.CODEC.optionalFieldOf("skybox", Skybox.OVERWORLD).forGetter(DimensionType::skybox), (App)CardinalLighting.Type.CODEC.optionalFieldOf("cardinal_light", CardinalLighting.Type.DEFAULT).forGetter(DimensionType::cardinalLightType), (App)attributeMapCodec.optionalFieldOf("attributes", EnvironmentAttributeMap.EMPTY).forGetter(DimensionType::attributes), (App)RegistryCodecs.homogeneousList(Registries.TIMELINE).optionalFieldOf("timelines", HolderSet.empty()).forGetter(DimensionType::timelines), (App)WorldClock.CODEC.optionalFieldOf("default_clock").forGetter(DimensionType::defaultClock)).apply((Applicative)i, DimensionType::new)));
/*     */   }
/*     */   
/* 100 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<DimensionType>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.DIMENSION_TYPE);
/*     */   
/*     */   public DimensionType(boolean hasFixedTime, boolean hasSkyLight, boolean hasCeiling, boolean hasEnderDragonFight, double coordinateScale, int minY, int height, int logicalHeight, HolderSet<Block> infiniburn, float ambientLight, MonsterSettings monsterSettings, Skybox skybox, CardinalLighting.Type cardinalLightType, EnvironmentAttributeMap attributes, HolderSet<Timeline> timelines, Optional<Holder<WorldClock>> defaultClock) {
/* 103 */     if (height < 16)
/* 104 */       throw new IllegalStateException("height has to be at least 16"); 
/* 107 */     if (minY + height > MAX_Y + 1)
/* 108 */       throw new IllegalStateException("min_y + height cannot be higher than: " + MAX_Y + 1); 
/* 111 */     if (logicalHeight > height)
/* 112 */       throw new IllegalStateException("logical_height cannot be higher than height"); 
/* 115 */     if (height % 16 != 0)
/* 116 */       throw new IllegalStateException("height has to be multiple of 16"); 
/* 119 */     if (minY % 16 != 0)
/* 120 */       throw new IllegalStateException("min_y has to be a multiple of 16"); 
/*     */     this.hasFixedTime = hasFixedTime;
/*     */     this.hasSkyLight = hasSkyLight;
/*     */     this.hasCeiling = hasCeiling;
/*     */     this.hasEnderDragonFight = hasEnderDragonFight;
/*     */     this.coordinateScale = coordinateScale;
/*     */     this.minY = minY;
/*     */     this.height = height;
/*     */     this.logicalHeight = logicalHeight;
/*     */     this.infiniburn = infiniburn;
/*     */     this.ambientLight = ambientLight;
/*     */     this.monsterSettings = monsterSettings;
/*     */     this.skybox = skybox;
/*     */     this.cardinalLightType = cardinalLightType;
/*     */     this.attributes = attributes;
/*     */     this.timelines = timelines;
/*     */     this.defaultClock = defaultClock;
/*     */   }
/*     */   
/* 124 */   public static final float[] MOON_BRIGHTNESS_PER_PHASE = new float[] { 1.0F, 0.75F, 0.5F, 0.25F, 0.0F, 0.25F, 0.5F, 0.75F };
/*     */   
/* 128 */   public static final Codec<Holder<DimensionType>> CODEC = (Codec<Holder<DimensionType>>)RegistryFileCodec.create(Registries.DIMENSION_TYPE, DIRECT_CODEC);
/*     */   
/*     */   public static double getTeleportationScale(DimensionType lastDimensionType, DimensionType newDimensionType) {
/* 131 */     double oldScale = lastDimensionType.coordinateScale();
/* 132 */     double newScale = newDimensionType.coordinateScale();
/* 134 */     return oldScale / newScale;
/*     */   }
/*     */   
/*     */   public static Path getStorageFolder(ResourceKey<Level> name, Path baseFolder) {
/* 138 */     return name.identifier().resolveAgainst(baseFolder.resolve("dimensions"));
/*     */   }
/*     */   
/*     */   public IntProvider monsterSpawnLightTest() {
/* 142 */     return this.monsterSettings.monsterSpawnLightTest();
/*     */   }
/*     */   
/*     */   public int monsterSpawnBlockLightLimit() {
/* 146 */     return this.monsterSettings.monsterSpawnBlockLightLimit();
/*     */   }
/*     */   
/*     */   public boolean hasEndFlashes() {
/* 150 */     return (this.skybox == Skybox.END);
/*     */   }
/*     */   
/*     */   public enum Skybox implements StringRepresentable {
/* 154 */     NONE("none"),
/* 155 */     OVERWORLD("overworld"),
/* 156 */     END("end");
/*     */     
/* 159 */     public static final Codec<Skybox> CODEC = (Codec<Skybox>)StringRepresentable.fromEnum(Skybox::values);
/*     */     
/*     */     private final String name;
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     Skybox(String name) {
/* 164 */       this.name = name;
/*     */     }
/*     */     
/*     */     public String getSerializedName() {
/* 169 */       return this.name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\dimension\DimensionType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */