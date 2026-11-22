/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class EntityTippedArrowFix extends SimplestEntityRenameFix {
/*    */   public EntityTippedArrowFix(Schema outputSchema, boolean changesType) {
/*  9 */     super("EntityTippedArrowFix", outputSchema, changesType);
/*    */   }
/*    */   
/*    */   protected String rename(String name) {
/* 14 */     return Objects.equals(name, "TippedArrow") ? "Arrow" : name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\EntityTippedArrowFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */