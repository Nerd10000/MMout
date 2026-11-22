/*    */ package net.minecraft.server.dialog.action;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.nbt.StringTag;
/*    */ import net.minecraft.nbt.Tag;
/*    */ 
/*    */ class null implements Action.ValueGetter {
/*    */   public String asTemplateSubstitution() {
/* 51 */     return value.get();
/*    */   }
/*    */   
/*    */   public Tag asTag() {
/* 56 */     return (Tag)StringTag.valueOf(value.get());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\dialog\action\Action$ValueGetter$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */