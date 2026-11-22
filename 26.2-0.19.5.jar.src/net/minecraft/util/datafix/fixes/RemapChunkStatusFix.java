/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.function.UnaryOperator;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class RemapChunkStatusFix extends DataFix {
/*    */   private final String name;
/*    */   
/*    */   private final UnaryOperator<String> mapper;
/*    */   
/*    */   public RemapChunkStatusFix(Schema schema, String name, UnaryOperator<String> mapper) {
/* 19 */     super(schema, false);
/* 20 */     this.name = name;
/* 21 */     this.mapper = mapper;
/*    */   }
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 26 */     return fixTypeEverywhereTyped(this.name, getInputSchema().getType(References.CHUNK), input -> input.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */   
/*    */   private <T> Dynamic<T> fixStatus(Dynamic<T> dynamic) {
/* 38 */     Objects.requireNonNull(dynamic);
/* 38 */     Optional<Dynamic<T>> remapped = dynamic.asString().result().map(NamespacedSchema::ensureNamespaced).map(this.mapper).map(dynamic::createString);
/* 40 */     return (Dynamic<T>)DataFixUtils.orElse(remapped, dynamic);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\RemapChunkStatusFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */