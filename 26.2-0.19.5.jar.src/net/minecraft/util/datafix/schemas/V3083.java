/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ public class V3083 extends NamespacedSchema {
/*    */   public V3083(int versionKey, Schema parent) {
/* 16 */     super(versionKey, parent);
/*    */   }
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
/* 21 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(schema);
/* 22 */     schema.register(map, "minecraft:allay", () -> DSL.optionalFields("Inventory", DSL.list(References.ITEM_STACK.in(schema)), "listener", DSL.optionalFields("event", DSL.optionalFields("game_event", References.GAME_EVENT_NAME.in(schema)))));
/* 30 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\schemas\V3083.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */