/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class EntityCatSplitFix extends SimpleEntityRenameFix {
/*    */   public EntityCatSplitFix(Schema outputSchema, boolean changesType) {
/* 11 */     super("EntityCatSplitFix", outputSchema, changesType);
/*    */   }
/*    */   
/*    */   protected Pair<String, Dynamic<?>> getNewNameAndTag(String name, Dynamic<?> tag) {
/* 16 */     if (Objects.equals("minecraft:ocelot", name)) {
/* 17 */       int type = tag.get("CatType").asInt(0);
/* 18 */       if (type == 0) {
/* 19 */         String ownerName = tag.get("Owner").asString("");
/* 20 */         String ownerUUID = tag.get("OwnerUUID").asString("");
/* 21 */         if (!ownerName.isEmpty() || !ownerUUID.isEmpty())
/* 22 */           tag.set("Trusting", tag.createBoolean(true)); 
/* 24 */       } else if (type > 0 && type < 4) {
/* 25 */         tag = tag.set("CatType", tag.createInt(type));
/* 26 */         tag = tag.set("OwnerUUID", tag.createString(tag.get("OwnerUUID").asString("")));
/* 27 */         return Pair.of("minecraft:cat", tag);
/*    */       } 
/*    */     } 
/* 31 */     return Pair.of(name, tag);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\EntityCatSplitFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */