/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public class V3439_1 extends NamespacedSchema {
/*    */   public V3439_1(int versionKey, Schema parent) {
/* 11 */     super(versionKey, parent);
/*    */   }
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
/* 16 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(schema);
/* 17 */     register(map, "minecraft:hanging_sign", () -> V3439.sign(schema));
/* 18 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\schemas\V3439_1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */