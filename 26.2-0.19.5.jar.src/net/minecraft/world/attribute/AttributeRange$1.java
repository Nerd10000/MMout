/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ import com.mojang.serialization.DataResult;
/*    */ 
/*    */ class null implements AttributeRange<Value> {
/*    */   public DataResult<Value> validate(Value value) {
/* 14 */     return DataResult.success(value);
/*    */   }
/*    */   
/*    */   public Value sanitize(Value value) {
/* 19 */     return value;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\attribute\AttributeRange$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */