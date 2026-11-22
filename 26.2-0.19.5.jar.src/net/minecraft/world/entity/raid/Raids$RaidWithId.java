/*     */ package net.minecraft.world.entity.raid;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import java.util.function.BiFunction;
/*     */ 
/*     */ final class RaidWithId extends Record {
/*     */   private final int id;
/*     */   
/*     */   private final Raid raid;
/*     */   
/*     */   public static final Codec<RaidWithId> CODEC;
/*     */   
/*     */   private RaidWithId(int id, Raid raid) {
/* 187 */     this.id = id;
/* 187 */     this.raid = raid;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/raid/Raids$RaidWithId;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #187	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/raid/Raids$RaidWithId;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/raid/Raids$RaidWithId;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #187	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/raid/Raids$RaidWithId;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/raid/Raids$RaidWithId;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #187	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/entity/raid/Raids$RaidWithId;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int id() {
/* 187 */     return this.id;
/*     */   }
/*     */   
/*     */   public Raid raid() {
/* 187 */     return this.raid;
/*     */   }
/*     */   
/*     */   static {
/* 188 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.INT.fieldOf("id").forGetter(RaidWithId::id), (App)Raid.MAP_CODEC.forGetter(RaidWithId::raid)).apply((Applicative)i, RaidWithId::new));
/*     */   }
/*     */   
/*     */   public static RaidWithId from(Int2ObjectMap.Entry<Raid> entry) {
/* 194 */     return new RaidWithId(entry.getIntKey(), (Raid)entry.getValue());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\raid\Raids$RaidWithId.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */