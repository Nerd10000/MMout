/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamDecoder;
/*     */ 
/*     */ enum OperationType {
/*  95 */   ADD(AddOperation::new),
/*  96 */   REMOVE(input -> ClientboundBossEventPacket.REMOVE_OPERATION),
/*  97 */   UPDATE_PROGRESS(UpdateProgressOperation::new),
/*  98 */   UPDATE_NAME(UpdateNameOperation::new),
/*  99 */   UPDATE_STYLE(UpdateStyleOperation::new),
/* 100 */   UPDATE_PROPERTIES(UpdatePropertiesOperation::new);
/*     */   
/*     */   private final StreamDecoder<RegistryFriendlyByteBuf, ClientboundBossEventPacket.Operation> reader;
/*     */   
/*     */   OperationType(StreamDecoder<RegistryFriendlyByteBuf, ClientboundBossEventPacket.Operation> reader) {
/* 106 */     this.reader = reader;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundBossEventPacket$OperationType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */