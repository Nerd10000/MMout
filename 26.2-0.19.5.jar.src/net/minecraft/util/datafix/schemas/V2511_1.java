/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ public class V2511_1 extends NamespacedSchema {
/*    */   public V2511_1(int versionKey, Schema parent) {
/* 14 */     super(versionKey, parent);
/*    */   }
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
/* 19 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(schema);
/* 20 */     schema.register(map, "minecraft:potion", name -> DSL.optionalFields("Item", References.ITEM_STACK.in(schema)));
/* 23 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\schemas\V2511_1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */