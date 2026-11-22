/*    */ package net.minecraft.commands.arguments.selector;
/*    */ 
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.entity.EntityTypeTest;
/*    */ 
/*    */ class null implements EntityTypeTest<Entity, Entity> {
/*    */   public Entity tryCast(Entity entity) {
/* 38 */     return entity;
/*    */   }
/*    */   
/*    */   public Class<? extends Entity> getBaseClass() {
/* 43 */     return Entity.class;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\selector\EntitySelector$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */