/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public interface EnvironmentAttributeReader {
/* 10 */   public static final EnvironmentAttributeReader EMPTY = new EnvironmentAttributeReader() {
/*    */       public <Value> Value getDimensionValue(EnvironmentAttribute<Value> attribute) {
/* 13 */         return attribute.defaultValue();
/*    */       }
/*    */       
/*    */       public <Value> Value getValue(EnvironmentAttribute<Value> attribute, Vec3 pos, SpatialAttributeInterpolator biomeInterpolator) {
/* 18 */         return attribute.defaultValue();
/*    */       }
/*    */     };
/*    */   
/*    */   <Value> Value getDimensionValue(EnvironmentAttribute<Value> paramEnvironmentAttribute);
/*    */   
/*    */   default <Value> Value getValue(EnvironmentAttribute<Value> attribute, BlockPos pos) {
/* 25 */     return getValue(attribute, Vec3.atCenterOf((Vec3i)pos));
/*    */   }
/*    */   
/*    */   default <Value> Value getValue(EnvironmentAttribute<Value> attribute, Vec3 pos) {
/* 29 */     return getValue(attribute, pos, null);
/*    */   }
/*    */   
/*    */   <Value> Value getValue(EnvironmentAttribute<Value> paramEnvironmentAttribute, Vec3 paramVec3, SpatialAttributeInterpolator paramSpatialAttributeInterpolator);
/*    */   
/*    */   default <Value> Value getValue(LootContext context, EnvironmentAttribute<Value> attribute) {
/* 35 */     if (attribute.isPositional())
/* 36 */       return getValue(attribute, (Vec3)context.getParameter(LootContextParams.ORIGIN)); 
/* 38 */     return getDimensionValue(attribute);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\attribute\EnvironmentAttributeReader.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */