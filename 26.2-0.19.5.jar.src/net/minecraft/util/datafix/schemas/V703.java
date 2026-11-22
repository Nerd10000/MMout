/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ public class V703 extends Schema {
/*    */   public V703(int versionKey, Schema parent) {
/* 15 */     super(versionKey, parent);
/*    */   }
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
/* 20 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(schema);
/* 22 */     map.remove("EntityHorse");
/* 23 */     schema.register(map, "Horse", () -> DSL.optionalFields("ArmorItem", References.ITEM_STACK.in(schema), "SaddleItem", References.ITEM_STACK.in(schema)));
/* 27 */     schema.register(map, "Donkey", () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)), "SaddleItem", References.ITEM_STACK.in(schema)));
/* 31 */     schema.register(map, "Mule", () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)), "SaddleItem", References.ITEM_STACK.in(schema)));
/* 35 */     schema.register(map, "ZombieHorse", () -> DSL.optionalFields("SaddleItem", References.ITEM_STACK.in(schema)));
/* 38 */     schema.register(map, "SkeletonHorse", () -> DSL.optionalFields("SaddleItem", References.ITEM_STACK.in(schema)));
/* 42 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\schemas\V703.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */