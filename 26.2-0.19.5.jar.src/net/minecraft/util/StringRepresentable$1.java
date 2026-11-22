/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Keyable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ class null implements Keyable {
/*     */   public <T> Stream<T> keys(DynamicOps<T> ops) {
/* 107 */     Objects.requireNonNull(ops);
/* 107 */     return Arrays.<StringRepresentable>stream(values).map(StringRepresentable::getSerializedName).map(ops::createString);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\StringRepresentable$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */