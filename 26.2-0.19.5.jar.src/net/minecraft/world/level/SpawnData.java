/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.InclusiveRange;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ import net.minecraft.world.entity.EquipmentTable;
/*    */ 
/*    */ public final class SpawnData extends Record {
/*    */   private final CompoundTag entityToSpawn;
/*    */   
/*    */   private final Optional<CustomSpawnRules> customSpawnRules;
/*    */   
/*    */   private final Optional<EquipmentTable> equipment;
/*    */   
/*    */   public static final String ENTITY_TAG = "entity";
/*    */   
/*    */   public static final Codec<SpawnData> CODEC;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/SpawnData;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/SpawnData;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/SpawnData;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/SpawnData;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/SpawnData;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/SpawnData;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public CompoundTag entityToSpawn() {
/* 18 */     return this.entityToSpawn;
/*    */   }
/*    */   
/*    */   public Optional<CustomSpawnRules> customSpawnRules() {
/* 18 */     return this.customSpawnRules;
/*    */   }
/*    */   
/*    */   public Optional<EquipmentTable> equipment() {
/* 18 */     return this.equipment;
/*    */   }
/*    */   
/*    */   static {
/* 25 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)CompoundTag.CODEC.fieldOf("entity").forGetter(()), (App)CustomSpawnRules.CODEC.optionalFieldOf("custom_spawn_rules").forGetter(()), (App)EquipmentTable.CODEC.optionalFieldOf("equipment").forGetter(())).apply((Applicative)i, SpawnData::new));
/*    */   }
/*    */   
/* 31 */   public static final Codec<WeightedList<SpawnData>> LIST_CODEC = WeightedList.codec(CODEC);
/*    */   
/*    */   public SpawnData() {
/* 34 */     this(new CompoundTag(), Optional.empty(), Optional.empty());
/*    */   }
/*    */   
/*    */   public SpawnData(CompoundTag entityToSpawn, Optional<CustomSpawnRules> customSpawnRules, Optional<EquipmentTable> equipment) {
/* 38 */     Optional<Identifier> id = entityToSpawn.read("id", Identifier.CODEC);
/* 40 */     if (id.isPresent()) {
/* 41 */       entityToSpawn.store("id", Identifier.CODEC, id.get());
/*    */     } else {
/* 43 */       entityToSpawn.remove("id");
/*    */     } 
/*    */     this.entityToSpawn = entityToSpawn;
/*    */     this.customSpawnRules = customSpawnRules;
/*    */     this.equipment = equipment;
/*    */   }
/*    */   
/*    */   public CompoundTag getEntityToSpawn() {
/* 48 */     return this.entityToSpawn;
/*    */   }
/*    */   
/*    */   public Optional<CustomSpawnRules> getCustomSpawnRules() {
/* 52 */     return this.customSpawnRules;
/*    */   }
/*    */   
/*    */   public Optional<EquipmentTable> getEquipment() {
/* 56 */     return this.equipment;
/*    */   }
/*    */   
/*    */   public static final class CustomSpawnRules extends Record {
/*    */     private final InclusiveRange<Integer> blockLightLimit;
/*    */     
/*    */     private final InclusiveRange<Integer> skyLightLimit;
/*    */     
/*    */     public CustomSpawnRules(InclusiveRange<Integer> blockLightLimit, InclusiveRange<Integer> skyLightLimit) {
/* 59 */       this.blockLightLimit = blockLightLimit;
/* 59 */       this.skyLightLimit = skyLightLimit;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/SpawnData$CustomSpawnRules;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #59	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/SpawnData$CustomSpawnRules;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/SpawnData$CustomSpawnRules;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #59	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/SpawnData$CustomSpawnRules;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/SpawnData$CustomSpawnRules;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #59	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/SpawnData$CustomSpawnRules;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public InclusiveRange<Integer> blockLightLimit() {
/* 59 */       return this.blockLightLimit;
/*    */     }
/*    */     
/*    */     public InclusiveRange<Integer> skyLightLimit() {
/* 59 */       return this.skyLightLimit;
/*    */     }
/*    */     
/* 63 */     private static final InclusiveRange<Integer> LIGHT_RANGE = new InclusiveRange(Integer.valueOf(0), Integer.valueOf(15));
/*    */     
/*    */     public static final Codec<CustomSpawnRules> CODEC;
/*    */     
/*    */     private static DataResult<InclusiveRange<Integer>> checkLightBoundaries(InclusiveRange<Integer> range) {
/* 66 */       if (!LIGHT_RANGE.contains(range))
/* 67 */         return DataResult.error(() -> "Light values must be withing range " + String.valueOf(LIGHT_RANGE)); 
/* 69 */       return DataResult.success(range);
/*    */     }
/*    */     
/*    */     private static MapCodec<InclusiveRange<Integer>> lightLimit(String name) {
/* 73 */       return InclusiveRange.INT.lenientOptionalFieldOf(name, LIGHT_RANGE).validate(CustomSpawnRules::checkLightBoundaries);
/*    */     }
/*    */     
/*    */     static {
/* 76 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)lightLimit("block_light_limit").forGetter(()), (App)lightLimit("sky_light_limit").forGetter(())).apply((Applicative)i, CustomSpawnRules::new));
/*    */     }
/*    */     
/*    */     public boolean isValidPosition(BlockPos blockSpawnPos, ServerLevel level) {
/* 83 */       return (this.blockLightLimit.isValueInRange(Integer.valueOf(level.getBrightness(LightLayer.BLOCK, blockSpawnPos))) && this.skyLightLimit
/* 84 */         .isValueInRange(Integer.valueOf(level.getEffectiveSkyBrightness(blockSpawnPos))));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\SpawnData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */