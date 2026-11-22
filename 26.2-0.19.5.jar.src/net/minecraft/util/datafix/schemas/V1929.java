/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ public class V1929 extends NamespacedSchema {
/*    */   public V1929(int versionKey, Schema parent) {
/* 16 */     super(versionKey, parent);
/*    */   }
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
/* 21 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(schema);
/* 22 */     schema.register(map, "minecraft:wandering_trader", name -> DSL.optionalFields("Inventory", DSL.list(References.ITEM_STACK.in(schema)), "Offers", DSL.optionalFields("Recipes", DSL.list(References.VILLAGER_TRADE.in(schema)))));
/* 29 */     schema.register(map, "minecraft:trader_llama", name -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)), "SaddleItem", References.ITEM_STACK.in(schema), "DecorItem", References.ITEM_STACK.in(schema)));
/* 35 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\schemas\V1929.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */