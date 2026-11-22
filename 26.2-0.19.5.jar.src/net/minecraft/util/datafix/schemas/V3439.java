/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ public class V3439 extends NamespacedSchema {
/*    */   public V3439(int versionKey, Schema parent) {
/* 15 */     super(versionKey, parent);
/*    */   }
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
/* 20 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(schema);
/* 21 */     register(map, "minecraft:sign", () -> sign(schema));
/* 22 */     return map;
/*    */   }
/*    */   
/*    */   public static TypeTemplate sign(Schema schema) {
/* 26 */     return DSL.optionalFields("front_text", 
/* 27 */         DSL.optionalFields("messages", 
/* 28 */           DSL.list(References.TEXT_COMPONENT.in(schema)), "filtered_messages", 
/* 29 */           DSL.list(References.TEXT_COMPONENT.in(schema))), "back_text", 
/*    */         
/* 31 */         DSL.optionalFields("messages", 
/* 32 */           DSL.list(References.TEXT_COMPONENT.in(schema)), "filtered_messages", 
/* 33 */           DSL.list(References.TEXT_COMPONENT.in(schema))));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\schemas\V3439.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */