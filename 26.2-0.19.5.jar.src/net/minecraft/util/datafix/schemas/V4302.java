/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ public class V4302 extends NamespacedSchema {
/*    */   public V4302(int versionKey, Schema parent) {
/* 15 */     super(versionKey, parent);
/*    */   }
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
/* 20 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(schema);
/* 21 */     schema.registerSimple(map, "minecraft:test_block");
/* 22 */     schema.register(map, "minecraft:test_instance_block", () -> DSL.optionalFields("data", DSL.optionalFields("error_message", References.TEXT_COMPONENT.in(schema)), "errors", DSL.list(DSL.optionalFields("text", References.TEXT_COMPONENT.in(schema)))));
/* 30 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\schemas\V4302.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */