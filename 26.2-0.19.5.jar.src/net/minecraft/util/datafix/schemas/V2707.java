/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public class V2707 extends NamespacedSchema {
/*    */   public V2707(int versionKey, Schema parent) {
/* 11 */     super(versionKey, parent);
/*    */   }
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
/* 16 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(schema);
/* 17 */     registerSimple(map, "minecraft:marker");
/* 18 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\schemas\V2707.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */