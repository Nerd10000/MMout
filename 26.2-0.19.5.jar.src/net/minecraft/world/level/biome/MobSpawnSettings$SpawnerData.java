/*    */ package net.minecraft.world.level.biome;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.EntityTypes;
/*    */ import net.minecraft.world.entity.MobCategory;
/*    */ 
/*    */ public final class SpawnerData extends Record {
/*    */   private final EntityType<?> type;
/*    */   
/*    */   private final int minCount;
/*    */   
/*    */   private final int maxCount;
/*    */   
/*    */   public static final MapCodec<SpawnerData> CODEC;
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/biome/MobSpawnSettings$SpawnerData;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #67	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/biome/MobSpawnSettings$SpawnerData;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/biome/MobSpawnSettings$SpawnerData;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #67	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/biome/MobSpawnSettings$SpawnerData;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public EntityType<?> type() {
/* 67 */     return this.type;
/*    */   }
/*    */   
/*    */   public int minCount() {
/* 67 */     return this.minCount;
/*    */   }
/*    */   
/*    */   public int maxCount() {
/* 67 */     return this.maxCount;
/*    */   }
/*    */   
/*    */   static {
/* 72 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("type").forGetter(()), (App)ExtraCodecs.POSITIVE_INT.fieldOf("minCount").forGetter(()), (App)ExtraCodecs.POSITIVE_INT.fieldOf("maxCount").forGetter(())).apply((Applicative)i, SpawnerData::new)).validate(spawnerData -> (spawnerData.minCount > spawnerData.maxCount) ? DataResult.error(()) : DataResult.success(spawnerData));
/*    */   }
/*    */   
/*    */   public SpawnerData(EntityType<?> type, int minCount, int maxCount) {
/* 80 */     type = (type.getCategory() == MobCategory.MISC) ? EntityTypes.PIG : type;
/*    */     this.type = type;
/*    */     this.minCount = minCount;
/*    */     this.maxCount = maxCount;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 85 */     return String.valueOf(EntityType.getKey(this.type)) + "*(" + String.valueOf(EntityType.getKey(this.type)) + "-" + this.minCount + ")";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\biome\MobSpawnSettings$SpawnerData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */