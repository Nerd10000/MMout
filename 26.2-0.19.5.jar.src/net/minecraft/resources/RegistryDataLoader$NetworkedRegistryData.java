/*     */ package net.minecraft.resources;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.RegistrySynchronization;
/*     */ import net.minecraft.tags.TagNetworkSerialization;
/*     */ 
/*     */ public final class NetworkedRegistryData extends Record {
/*     */   private final List<RegistrySynchronization.PackedRegistryEntry> elements;
/*     */   
/*     */   private final TagNetworkSerialization.NetworkPayload tags;
/*     */   
/*     */   public NetworkedRegistryData(List<RegistrySynchronization.PackedRegistryEntry> elements, TagNetworkSerialization.NetworkPayload tags) {
/* 195 */     this.elements = elements;
/* 195 */     this.tags = tags;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/resources/RegistryDataLoader$NetworkedRegistryData;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #195	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/resources/RegistryDataLoader$NetworkedRegistryData;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/resources/RegistryDataLoader$NetworkedRegistryData;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #195	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/resources/RegistryDataLoader$NetworkedRegistryData;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/resources/RegistryDataLoader$NetworkedRegistryData;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #195	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/resources/RegistryDataLoader$NetworkedRegistryData;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public List<RegistrySynchronization.PackedRegistryEntry> elements() {
/* 195 */     return this.elements;
/*     */   }
/*     */   
/*     */   public TagNetworkSerialization.NetworkPayload tags() {
/* 195 */     return this.tags;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\resources\RegistryDataLoader$NetworkedRegistryData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */