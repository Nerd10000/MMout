/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import com.google.common.cache.CacheLoader;
/*     */ import com.google.common.hash.HashCode;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import net.minecraft.core.component.TypedDataComponent;
/*     */ import net.minecraft.util.HashOps;
/*     */ 
/*     */ class null extends CacheLoader<TypedDataComponent<?>, Integer> {
/* 308 */   private final DynamicOps<HashCode> registryHashOps = (DynamicOps<HashCode>)this.this$1.this$0.registryAccess().createSerializationContext((DynamicOps)HashOps.CRC32C_INSTANCE);
/*     */   
/*     */   public Integer load(TypedDataComponent<?> component) {
/* 312 */     return Integer.valueOf(((HashCode)component.encodeValue(this.registryHashOps).getOrThrow(msg -> new IllegalArgumentException("Failed to hash " + String.valueOf(component) + ": " + msg))).asInt());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\ServerPlayer$1$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */