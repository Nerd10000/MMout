/*    */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*    */ 
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*    */ 
/*    */ public abstract class AbstractDragonSittingPhase extends AbstractDragonPhaseInstance {
/*    */   public AbstractDragonSittingPhase(EnderDragon dragon) {
/* 10 */     super(dragon);
/*    */   }
/*    */   
/*    */   public boolean isSitting() {
/* 15 */     return true;
/*    */   }
/*    */   
/*    */   public float onHurt(DamageSource source, float damage) {
/* 20 */     if (source.getDirectEntity() instanceof net.minecraft.world.entity.projectile.arrow.AbstractArrow || source.getDirectEntity() instanceof net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.WindCharge) {
/* 21 */       source.getDirectEntity().igniteForSeconds(1.0F);
/* 22 */       return 0.0F;
/*    */     } 
/* 24 */     return super.onHurt(source, damage);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\boss\enderdragon\phases\AbstractDragonSittingPhase.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */