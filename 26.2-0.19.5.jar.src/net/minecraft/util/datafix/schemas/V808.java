/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ public class V808 extends NamespacedSchema {
/*    */   public V808(int versionKey, Schema parent) {
/* 15 */     super(versionKey, parent);
/*    */   }
/*    */   
/*    */   protected static void registerInventory(Schema schema, Map<String, Supplier<TypeTemplate>> map, String name) {
/* 19 */     schema.register(map, name, () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema))));
/*    */   }
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
/* 26 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(schema);
/* 28 */     registerInventory(schema, map, "minecraft:shulker_box");
/* 30 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\schemas\V808.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */