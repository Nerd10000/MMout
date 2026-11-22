/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ public class V3807 extends NamespacedSchema {
/*    */   public V3807(int versionKey, Schema parent) {
/* 15 */     super(versionKey, parent);
/*    */   }
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
/* 20 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(schema);
/* 21 */     schema.register(map, "minecraft:vault", () -> DSL.optionalFields("config", DSL.optionalFields("key_item", References.ITEM_STACK.in(schema)), "server_data", DSL.optionalFields("items_to_eject", DSL.list(References.ITEM_STACK.in(schema))), "shared_data", DSL.optionalFields("display_item", References.ITEM_STACK.in(schema))));
/* 32 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\schemas\V3807.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */