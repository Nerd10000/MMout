/*    */ package net.minecraft.util.debug;
/*    */ 
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiRecord;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*    */ 
/*    */ public final class DebugPoiInfo extends Record {
/*    */   private final BlockPos pos;
/*    */   
/*    */   private final Holder<PoiType> poiType;
/*    */   
/*    */   private final int freeTicketCount;
/*    */   
/*    */   public int freeTicketCount() {
/* 12 */     return this.freeTicketCount;
/*    */   }
/*    */   
/*    */   public Holder<PoiType> poiType() {
/* 12 */     return this.poiType;
/*    */   }
/*    */   
/*    */   public BlockPos pos() {
/* 12 */     return this.pos;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/debug/DebugPoiInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/debug/DebugPoiInfo;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/debug/DebugPoiInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/debug/DebugPoiInfo;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/debug/DebugPoiInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/debug/DebugPoiInfo;
/*    */   }
/*    */   
/*    */   public DebugPoiInfo(BlockPos pos, Holder<PoiType> poiType, int freeTicketCount) {
/* 12 */     this.pos = pos;
/* 12 */     this.poiType = poiType;
/* 12 */     this.freeTicketCount = freeTicketCount;
/*    */   }
/*    */   
/* 17 */   public static final StreamCodec<RegistryFriendlyByteBuf, DebugPoiInfo> STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, DebugPoiInfo::pos, 
/*    */       
/* 19 */       ByteBufCodecs.holderRegistry(Registries.POINT_OF_INTEREST_TYPE), DebugPoiInfo::poiType, ByteBufCodecs.VAR_INT, DebugPoiInfo::freeTicketCount, DebugPoiInfo::new);
/*    */   
/*    */   public DebugPoiInfo(PoiRecord record) {
/* 25 */     this(record.getPos(), record.getPoiType(), record.getFreeTickets());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\debug\DebugPoiInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */