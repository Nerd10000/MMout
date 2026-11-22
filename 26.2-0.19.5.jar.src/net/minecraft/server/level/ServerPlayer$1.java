/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import com.google.common.cache.CacheBuilder;
/*     */ import com.google.common.cache.CacheLoader;
/*     */ import com.google.common.cache.LoadingCache;
/*     */ import com.google.common.hash.HashCode;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.component.TypedDataComponent;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundContainerSetDataPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetCursorItemPacket;
/*     */ import net.minecraft.util.HashOps;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.ContainerSynchronizer;
/*     */ import net.minecraft.world.inventory.RemoteSlot;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ class null implements ContainerSynchronizer {
/* 305 */   private final LoadingCache<TypedDataComponent<?>, Integer> cache = CacheBuilder.newBuilder()
/* 306 */     .maximumSize(256L)
/* 307 */     .build(new CacheLoader<TypedDataComponent<?>, Integer>() {
/* 308 */         private final DynamicOps<HashCode> registryHashOps = (DynamicOps<HashCode>)ServerPlayer.this.registryAccess().createSerializationContext((DynamicOps)HashOps.CRC32C_INSTANCE);
/*     */         
/*     */         public Integer load(TypedDataComponent<?> component) {
/* 312 */           return Integer.valueOf(((HashCode)component.encodeValue(this.registryHashOps).getOrThrow(msg -> new IllegalArgumentException("Failed to hash " + String.valueOf(component) + ": " + msg))).asInt());
/*     */         }
/*     */       });
/*     */   
/*     */   public void sendInitialData(AbstractContainerMenu container, List<ItemStack> slotItems, ItemStack carriedItem, int[] dataSlots) {
/* 318 */     ServerPlayer.this.connection.send((Packet)new ClientboundContainerSetContentPacket(container.containerId, container.incrementStateId(), slotItems, carriedItem));
/* 319 */     for (int slot = 0; slot < dataSlots.length; slot++)
/* 320 */       broadcastDataValue(container, slot, dataSlots[slot]); 
/*     */   }
/*     */   
/*     */   public void sendSlotChange(AbstractContainerMenu container, int slotIndex, ItemStack itemStack) {
/* 326 */     ServerPlayer.this.connection.send((Packet)new ClientboundContainerSetSlotPacket(container.containerId, container.incrementStateId(), slotIndex, itemStack));
/*     */   }
/*     */   
/*     */   public void sendCarriedChange(AbstractContainerMenu container, ItemStack itemStack) {
/* 331 */     ServerPlayer.this.connection.send((Packet)new ClientboundSetCursorItemPacket(itemStack));
/*     */   }
/*     */   
/*     */   public void sendDataChange(AbstractContainerMenu container, int id, int value) {
/* 336 */     broadcastDataValue(container, id, value);
/*     */   }
/*     */   
/*     */   private void broadcastDataValue(AbstractContainerMenu container, int id, int value) {
/* 340 */     ServerPlayer.this.connection.send((Packet)new ClientboundContainerSetDataPacket(container.containerId, id, value));
/*     */   }
/*     */   
/*     */   public RemoteSlot createSlot() {
/* 345 */     Objects.requireNonNull(this.cache);
/* 345 */     return (RemoteSlot)new RemoteSlot.Synchronized(this.cache::getUnchecked);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\ServerPlayer$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */