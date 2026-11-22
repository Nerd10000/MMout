/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class EntitySkeletonSplitFix extends SimpleEntityRenameFix {
/*    */   public EntitySkeletonSplitFix(Schema outputSchema, boolean changesType) {
/* 11 */     super("EntitySkeletonSplitFix", outputSchema, changesType);
/*    */   }
/*    */   
/*    */   protected Pair<String, Dynamic<?>> getNewNameAndTag(String name, Dynamic<?> tag) {
/* 16 */     if (Objects.equals(name, "Skeleton")) {
/* 17 */       int type = tag.get("SkeletonType").asInt(0);
/* 18 */       if (type == 1) {
/* 19 */         name = "WitherSkeleton";
/* 20 */       } else if (type == 2) {
/* 21 */         name = "Stray";
/*    */       } 
/*    */     } 
/* 24 */     return Pair.of(name, tag);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\EntitySkeletonSplitFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */