/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ public class V2684 extends NamespacedSchema {
/*    */   public V2684(int versionKey, Schema parent) {
/* 16 */     super(versionKey, parent);
/*    */   }
/*    */   
/*    */   public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
/* 21 */     super.registerTypes(schema, entityTypes, blockEntityTypes);
/* 22 */     schema.registerType(false, References.GAME_EVENT_NAME, () -> DSL.constType(namespacedString()));
/*    */   }
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
/* 27 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(schema);
/* 28 */     schema.register(map, "minecraft:sculk_sensor", () -> DSL.optionalFields("listener", DSL.optionalFields("event", DSL.optionalFields("game_event", References.GAME_EVENT_NAME.in(schema)))));
/* 35 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\schemas\V2684.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */