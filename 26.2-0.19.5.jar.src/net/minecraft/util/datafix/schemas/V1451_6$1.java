/*     */ package net.minecraft.util.datafix.schemas;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.types.templates.Hook;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.Map;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ class null implements Hook.HookFunction {
/*     */   public <T> T apply(DynamicOps<T> ops, T value) {
/*  95 */     Dynamic<T> input = new Dynamic(ops, value);
/*  97 */     return (T)((Dynamic)DataFixUtils.orElse(input
/*  98 */         .get("CriteriaName").asString().result()
/*  99 */         .map(name -> {
/*     */             int colonPos = name.indexOf(':');
/*     */             if (colonPos < 0)
/*     */               return Pair.of("_special", name); 
/*     */             try {
/*     */               Identifier statType = Identifier.bySeparator(name.substring(0, colonPos), '.');
/*     */               Identifier statId = Identifier.bySeparator(name.substring(colonPos + 1), '.');
/*     */               return Pair.of(statType.toString(), statId.toString());
/* 108 */             } catch (Exception e) {
/*     */               return Pair.of("_special", name);
/*     */             } 
/* 112 */           }).map(explodedId -> input.set("CriteriaType", input.createMap((Map)ImmutableMap.of(input.createString("type"), input.createString((String)explodedId.getFirst()), input.createString("id"), input.createString((String)explodedId.getSecond()))))), input))
/*     */       
/* 121 */       .getValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\schemas\V1451_6$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */