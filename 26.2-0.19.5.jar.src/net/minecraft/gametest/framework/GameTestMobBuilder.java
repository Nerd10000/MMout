/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class GameTestMobBuilder<E extends Mob> extends GameTestEntityBuilder<E> {
/*    */   private boolean freeWill = true;
/*    */   
/*    */   public GameTestMobBuilder(GameTestHelper testHelper, EntityType<E> entityType, Vec3 position) {
/* 11 */     super(testHelper, entityType, position);
/*    */   }
/*    */   
/*    */   public GameTestMobBuilder<E> withNoFreeWill() {
/* 15 */     this.freeWill = false;
/* 16 */     return this;
/*    */   }
/*    */   
/*    */   public E spawn() {
/* 21 */     Mob mob = (Mob)super.spawn();
/* 22 */     if (!this.freeWill)
/* 23 */       mob.removeFreeWill(); 
/* 25 */     return (E)mob;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\GameTestMobBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */