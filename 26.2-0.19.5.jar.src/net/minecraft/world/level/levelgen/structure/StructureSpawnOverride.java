/*    */ package net.minecraft.world.level.levelgen.structure;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ import net.minecraft.world.level.biome.MobSpawnSettings;
/*    */ 
/*    */ public final class StructureSpawnOverride extends Record {
/*    */   private final BoundingBoxType boundingBox;
/*    */   
/*    */   private final WeightedList<MobSpawnSettings.SpawnerData> spawns;
/*    */   
/*    */   public static final Codec<StructureSpawnOverride> CODEC;
/*    */   
/*    */   public StructureSpawnOverride(BoundingBoxType boundingBox, WeightedList<MobSpawnSettings.SpawnerData> spawns) {
/*  9 */     this.boundingBox = boundingBox;
/*  9 */     this.spawns = spawns;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/StructureSpawnOverride;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/StructureSpawnOverride;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/StructureSpawnOverride;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/StructureSpawnOverride;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/StructureSpawnOverride;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/StructureSpawnOverride;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public BoundingBoxType boundingBox() {
/*  9 */     return this.boundingBox;
/*    */   }
/*    */   
/*    */   public WeightedList<MobSpawnSettings.SpawnerData> spawns() {
/*  9 */     return this.spawns;
/*    */   }
/*    */   
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)BoundingBoxType.CODEC.fieldOf("bounding_box").forGetter(StructureSpawnOverride::boundingBox), (App)WeightedList.codec(MobSpawnSettings.SpawnerData.CODEC).fieldOf("spawns").forGetter(StructureSpawnOverride::spawns)).apply((Applicative)i, StructureSpawnOverride::new));
/*    */   }
/*    */   
/*    */   public enum BoundingBoxType implements StringRepresentable {
/* 19 */     PIECE("piece"),
/* 20 */     STRUCTURE("full");
/*    */     
/* 22 */     public static final Codec<BoundingBoxType> CODEC = (Codec<BoundingBoxType>)StringRepresentable.fromEnum(BoundingBoxType::values);
/*    */     
/*    */     private final String id;
/*    */     
/*    */     static {
/*    */     
/*    */     }
/*    */     
/*    */     BoundingBoxType(String id) {
/* 27 */       this.id = id;
/*    */     }
/*    */     
/*    */     public String getSerializedName() {
/* 32 */       return this.id;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\StructureSpawnOverride.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */