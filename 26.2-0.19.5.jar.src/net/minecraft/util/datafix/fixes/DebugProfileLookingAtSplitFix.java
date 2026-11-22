/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class DebugProfileLookingAtSplitFix extends DataFix {
/*    */   public DebugProfileLookingAtSplitFix(Schema outputSchema) {
/* 16 */     super(outputSchema, false);
/*    */   }
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 21 */     return fixTypeEverywhereTyped("DebugProfileLookingAtSplitFix", getInputSchema().getType(References.DEBUG_PROFILE), input -> input.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */   
/*    */   private static <T> Dynamic<T> updateOptions(Dynamic<T> custom) {
/* 29 */     return (Dynamic<T>)DataFixUtils.orElse(custom.getMapValues().map(map -> {
/*    */             Map<Dynamic<T>, Dynamic<T>> newOptions = new HashMap<>();
/*    */             map.forEach(());
/*    */             return custom.createMap(newOptions);
/* 42 */           }).result(), custom);
/*    */   }
/*    */   
/*    */   private static <T> Optional<Dynamic<T>> renamedKey(Dynamic<T> keyDynamic) {
/* 52 */     Objects.requireNonNull(keyDynamic);
/*    */     return keyDynamic.asString().result().flatMap(key -> {
/*    */           switch (key) {
/*    */             case "minecraft:looking_at_block":
/*    */             
/*    */             case "minecraft:looking_at_fluid":
/*    */             
/*    */           } 
/*    */           return Optional.empty();
/* 52 */         }).map(keyDynamic::createString);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\DebugProfileLookingAtSplitFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */