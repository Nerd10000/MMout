/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ public class V3081 extends NamespacedSchema {
/*    */   public V3081(int versionKey, Schema parent) {
/* 14 */     super(versionKey, parent);
/*    */   }
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
/* 19 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(schema);
/* 20 */     schema.register(map, "minecraft:warden", () -> DSL.optionalFields("listener", DSL.optionalFields("event", DSL.optionalFields("game_event", References.GAME_EVENT_NAME.in(schema)))));
/* 27 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\schemas\V3081.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */