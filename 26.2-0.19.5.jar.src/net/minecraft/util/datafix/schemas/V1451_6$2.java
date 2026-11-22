/*     */ package net.minecraft.util.datafix.schemas;
/*     */ 
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.types.templates.Hook;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.Optional;
/*     */ 
/*     */ class null implements Hook.HookFunction {
/*     */   public <T> T apply(DynamicOps<T> ops, T value) {
/* 133 */     Dynamic<T> input = new Dynamic(ops, value);
/* 135 */     Optional<Dynamic<T>> repackedId = input.get("CriteriaType").get().result().flatMap(type -> {
/*     */           Optional<String> statType = type.get("type").asString().result();
/*     */           Optional<String> statId = type.get("id").asString().result();
/*     */           if (statType.isPresent() && statId.isPresent()) {
/*     */             String unpackedType = statType.get();
/*     */             return unpackedType.equals("_special") ? Optional.of(input.createString(statId.get())) : Optional.of(type.createString(V1451_6.packNamespacedWithDot(unpackedType) + ":" + V1451_6.packNamespacedWithDot(unpackedType)));
/*     */           } 
/*     */           return Optional.empty();
/*     */         });
/* 149 */     return (T)((Dynamic)DataFixUtils.orElse(repackedId.map(id -> input.set("CriteriaName", id).remove("CriteriaType")), input)).getValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\schemas\V1451_6$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */