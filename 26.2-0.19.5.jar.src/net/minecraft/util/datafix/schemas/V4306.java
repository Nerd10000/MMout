/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ public class V4306 extends NamespacedSchema {
/*    */   public V4306(int versionKey, Schema parent) {
/* 14 */     super(versionKey, parent);
/*    */   }
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
/* 19 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(schema);
/* 20 */     map.remove("minecraft:potion");
/* 21 */     schema.register(map, "minecraft:splash_potion", () -> DSL.optionalFields("Item", References.ITEM_STACK.in(schema)));
/* 24 */     schema.register(map, "minecraft:lingering_potion", () -> DSL.optionalFields("Item", References.ITEM_STACK.in(schema)));
/* 27 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\schemas\V4306.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */