/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public class V1486 extends NamespacedSchema {
/*    */   public V1486(int versionKey, Schema parent) {
/* 11 */     super(versionKey, parent);
/*    */   }
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
/* 16 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(schema);
/* 18 */     map.put("minecraft:cod", map.remove("minecraft:cod_mob"));
/* 19 */     map.put("minecraft:salmon", map.remove("minecraft:salmon_mob"));
/* 21 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\schemas\V1486.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */