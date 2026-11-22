/*    */ package net.minecraft.server.dialog.action;
/*    */ 
/*    */ import net.minecraft.nbt.StringTag;
/*    */ import net.minecraft.nbt.Tag;
/*    */ 
/*    */ class null implements Action.ValueGetter {
/*    */   public String asTemplateSubstitution() {
/* 37 */     return value;
/*    */   }
/*    */   
/*    */   public Tag asTag() {
/* 42 */     return (Tag)StringTag.valueOf(value);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\dialog\action\Action$ValueGetter$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */