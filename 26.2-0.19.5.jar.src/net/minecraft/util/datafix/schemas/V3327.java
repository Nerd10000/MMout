/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ public class V3327 extends NamespacedSchema {
/*    */   public V3327(int versionKey, Schema parent) {
/* 17 */     super(versionKey, parent);
/*    */   }
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
/* 22 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(schema);
/* 23 */     schema.register(map, "minecraft:decorated_pot", () -> DSL.optionalFields("shards", DSL.list(References.ITEM_NAME.in(schema)), "item", References.ITEM_STACK.in(schema)));
/* 28 */     schema.register(map, "minecraft:suspicious_sand", () -> DSL.optionalFields("item", References.ITEM_STACK.in(schema)));
/* 32 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\schemas\V3327.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */