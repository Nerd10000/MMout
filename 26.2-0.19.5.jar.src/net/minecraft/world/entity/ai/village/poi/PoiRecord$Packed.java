/*     */ package net.minecraft.world.entity.ai.village.poi;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.RegistryFixedCodec;
/*     */ 
/*     */ public final class Packed extends Record {
/*     */   private final BlockPos pos;
/*     */   
/*     */   private final Holder<PoiType> poiType;
/*     */   
/*     */   private final int freeTickets;
/*     */   
/*     */   public static final Codec<Packed> CODEC;
/*     */   
/*     */   public Packed(BlockPos pos, Holder<PoiType> poiType, int freeTickets) {
/*  93 */     this.pos = pos;
/*  93 */     this.poiType = poiType;
/*  93 */     this.freeTickets = freeTickets;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/ai/village/poi/PoiRecord$Packed;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #93	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/village/poi/PoiRecord$Packed;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/ai/village/poi/PoiRecord$Packed;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #93	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/village/poi/PoiRecord$Packed;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/ai/village/poi/PoiRecord$Packed;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #93	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/entity/ai/village/poi/PoiRecord$Packed;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public BlockPos pos() {
/*  93 */     return this.pos;
/*     */   }
/*     */   
/*     */   public Holder<PoiType> poiType() {
/*  93 */     return this.poiType;
/*     */   }
/*     */   
/*     */   public int freeTickets() {
/*  93 */     return this.freeTickets;
/*     */   }
/*     */   
/*     */   static {
/*  94 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)BlockPos.CODEC.fieldOf("pos").forGetter(Packed::pos), (App)RegistryFixedCodec.create(Registries.POINT_OF_INTEREST_TYPE).fieldOf("type").forGetter(Packed::poiType), (App)Codec.INT.optionalFieldOf("free_tickets", Integer.valueOf(0)).forGetter(Packed::freeTickets)).apply((Applicative)i, Packed::new));
/*     */   }
/*     */   
/*     */   public PoiRecord unpack(Runnable setDirty) {
/* 101 */     return new PoiRecord(this.pos, this.poiType, this.freeTickets, setDirty);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\village\poi\PoiRecord$Packed.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */