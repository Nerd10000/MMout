/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ class null implements EnvironmentAttributeReader {
/*    */   public <Value> Value getDimensionValue(EnvironmentAttribute<Value> attribute) {
/* 13 */     return attribute.defaultValue();
/*    */   }
/*    */   
/*    */   public <Value> Value getValue(EnvironmentAttribute<Value> attribute, Vec3 pos, SpatialAttributeInterpolator biomeInterpolator) {
/* 18 */     return attribute.defaultValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\attribute\EnvironmentAttributeReader$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */