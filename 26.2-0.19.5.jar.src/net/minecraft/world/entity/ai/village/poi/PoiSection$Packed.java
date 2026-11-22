/*     */ package net.minecraft.world.entity.ai.village.poi;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.function.BiFunction;
/*     */ 
/*     */ public final class Packed extends Record {
/*     */   private final boolean isValid;
/*     */   
/*     */   private final List<PoiRecord.Packed> records;
/*     */   
/*     */   public static final Codec<Packed> CODEC;
/*     */   
/*     */   public Packed(boolean isValid, List<PoiRecord.Packed> records) {
/* 156 */     this.isValid = isValid;
/* 156 */     this.records = records;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/ai/village/poi/PoiSection$Packed;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #156	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/village/poi/PoiSection$Packed;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/ai/village/poi/PoiSection$Packed;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #156	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/village/poi/PoiSection$Packed;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/ai/village/poi/PoiSection$Packed;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #156	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/entity/ai/village/poi/PoiSection$Packed;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public boolean isValid() {
/* 156 */     return this.isValid;
/*     */   }
/*     */   
/*     */   public List<PoiRecord.Packed> records() {
/* 156 */     return this.records;
/*     */   }
/*     */   
/*     */   static {
/* 160 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.BOOL.lenientOptionalFieldOf("Valid", Boolean.valueOf(false)).forGetter(Packed::isValid), (App)PoiRecord.Packed.CODEC.listOf().fieldOf("Records").forGetter(Packed::records)).apply((Applicative)i, Packed::new));
/*     */   }
/*     */   
/*     */   public PoiSection unpack(Runnable setDirty) {
/* 166 */     return new PoiSection(setDirty, this.isValid, this.records.stream().map(record -> record.unpack(setDirty)).toList());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\village\poi\PoiSection$Packed.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */