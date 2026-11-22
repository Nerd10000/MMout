/*     */ package com.mojang.blaze3d.platform;
/*     */ 
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public final class Key {
/*     */   private final String name;
/*     */   
/*     */   private final InputConstants.Type type;
/*     */   
/*     */   private final int value;
/*     */   
/*     */   private final Supplier<Component> displayName;
/*     */   
/* 379 */   private static final Map<String, Key> NAME_MAP = Maps.newHashMap();
/*     */   
/*     */   private Key(String name, InputConstants.Type type, int value) {
/* 382 */     this.name = name;
/* 383 */     this.type = type;
/* 384 */     this.value = value;
/* 386 */     this.displayName = (Supplier<Component>)Suppliers.memoize(() -> (Component)type.displayTextSupplier.apply(Integer.valueOf(value), name));
/* 387 */     NAME_MAP.put(name, this);
/*     */   }
/*     */   
/*     */   public InputConstants.Type getType() {
/* 391 */     return this.type;
/*     */   }
/*     */   
/*     */   public int getValue() {
/* 395 */     return this.value;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 399 */     return this.name;
/*     */   }
/*     */   
/*     */   public Component getDisplayName() {
/* 403 */     return this.displayName.get();
/*     */   }
/*     */   
/*     */   public OptionalInt getNumericKeyValue() {
/* 407 */     if (this.value >= 48 && this.value <= 57)
/* 408 */       return OptionalInt.of(this.value - 48); 
/* 410 */     if (this.value >= 320 && this.value <= 329)
/* 411 */       return OptionalInt.of(this.value - 320); 
/* 413 */     return OptionalInt.empty();
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 418 */     if (this == o)
/* 419 */       return true; 
/* 421 */     if (o == null || getClass() != o.getClass())
/* 422 */       return false; 
/* 424 */     Key key = (Key)o;
/* 425 */     return (this.value == key.value && this.type == key.type);
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 430 */     return Objects.hash(new Object[] { this.type, Integer.valueOf(this.value) });
/*     */   }
/*     */   
/*     */   public String toString() {
/* 435 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\platform\InputConstants$Key.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */