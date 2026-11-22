/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ public class V1906 extends NamespacedSchema {
/*    */   public V1906(int versionKey, Schema parent) {
/* 14 */     super(versionKey, parent);
/*    */   }
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
/* 19 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(schema);
/* 21 */     registerInventory(schema, map, "minecraft:barrel");
/* 22 */     registerInventory(schema, map, "minecraft:smoker");
/* 23 */     registerInventory(schema, map, "minecraft:blast_furnace");
/* 25 */     schema.register(map, "minecraft:lectern", name -> DSL.optionalFields("Book", References.ITEM_STACK.in(schema)));
/* 29 */     schema.registerSimple(map, "minecraft:bell");
/* 31 */     return map;
/*    */   }
/*    */   
/*    */   protected static void registerInventory(Schema schema, Map<String, Supplier<TypeTemplate>> map, String name) {
/* 35 */     schema.register(map, name, () -> V1458.nameableInventory(schema));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\schemas\V1906.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */