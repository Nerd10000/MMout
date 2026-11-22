/*    */ package net.minecraft.world.entity.projectile.throwableitemprojectile;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.AreaEffectCloud;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.EntityTypes;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ 
/*    */ public class ThrownLingeringPotion extends AbstractThrownPotion {
/*    */   public ThrownLingeringPotion(EntityType<? extends ThrownLingeringPotion> type, Level level) {
/* 17 */     super((EntityType)type, level);
/*    */   }
/*    */   
/*    */   public ThrownLingeringPotion(Level level, LivingEntity owner, ItemStack itemStack) {
/* 21 */     super(EntityTypes.LINGERING_POTION, level, owner, itemStack);
/*    */   }
/*    */   
/*    */   public ThrownLingeringPotion(Level level, double x, double y, double z, ItemStack itemStack) {
/* 25 */     super(EntityTypes.LINGERING_POTION, level, x, y, z, itemStack);
/*    */   }
/*    */   
/*    */   protected Item getDefaultItem() {
/* 30 */     return Items.LINGERING_POTION;
/*    */   }
/*    */   
/*    */   public void onHitAsPotion(ServerLevel level, ItemStack potionItem, HitResult hitResult) {
/* 35 */     AreaEffectCloud cloud = new AreaEffectCloud(level(), getX(), getY(), getZ());
/* 36 */     Entity entity = getOwner();
/* 36 */     if (entity instanceof LivingEntity) {
/* 36 */       LivingEntity owner = (LivingEntity)entity;
/* 37 */       cloud.setOwner(owner);
/*    */     } 
/* 39 */     cloud.setRadius(3.0F);
/* 40 */     cloud.setRadiusOnUse(-0.5F);
/* 41 */     cloud.setDuration(600);
/* 42 */     cloud.setWaitTime(10);
/* 43 */     cloud.setRadiusPerTick(-cloud.getRadius() / cloud.getDuration());
/* 44 */     cloud.applyComponentsFromItemStack(potionItem);
/* 46 */     level.addFreshEntity((Entity)cloud);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\projectile\throwableitemprojectile\ThrownLingeringPotion.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */