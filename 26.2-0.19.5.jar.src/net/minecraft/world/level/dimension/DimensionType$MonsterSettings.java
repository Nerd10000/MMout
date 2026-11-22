/*    */ package net.minecraft.world.level.dimension;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.util.valueproviders.IntProviders;
/*    */ 
/*    */ public final class MonsterSettings extends Record {
/*    */   private final IntProvider monsterSpawnLightTest;
/*    */   
/*    */   private final int monsterSpawnBlockLightLimit;
/*    */   
/*    */   public static final MapCodec<MonsterSettings> CODEC;
/*    */   
/*    */   public MonsterSettings(IntProvider monsterSpawnLightTest, int monsterSpawnBlockLightLimit) {
/* 66 */     this.monsterSpawnLightTest = monsterSpawnLightTest;
/* 66 */     this.monsterSpawnBlockLightLimit = monsterSpawnBlockLightLimit;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/dimension/DimensionType$MonsterSettings;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #66	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/dimension/DimensionType$MonsterSettings;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/dimension/DimensionType$MonsterSettings;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #66	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/dimension/DimensionType$MonsterSettings;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/dimension/DimensionType$MonsterSettings;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #66	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/dimension/DimensionType$MonsterSettings;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public IntProvider monsterSpawnLightTest() {
/* 66 */     return this.monsterSpawnLightTest;
/*    */   }
/*    */   
/*    */   public int monsterSpawnBlockLightLimit() {
/* 66 */     return this.monsterSpawnBlockLightLimit;
/*    */   }
/*    */   
/*    */   static {
/* 70 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)IntProviders.codec(0, 15).fieldOf("monster_spawn_light_level").forGetter(MonsterSettings::monsterSpawnLightTest), (App)Codec.intRange(0, 15).fieldOf("monster_spawn_block_light_limit").forGetter(MonsterSettings::monsterSpawnBlockLightLimit)).apply((Applicative)i, MonsterSettings::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\dimension\DimensionType$MonsterSettings.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */