/*    */ package net.minecraft.world.entity.vehicle.minecart;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class Minecart extends AbstractMinecart {
/*    */   private float rotationOffset;
/*    */   
/*    */   private float playerRotationOffset;
/*    */   
/*    */   public Minecart(EntityType<?> type, Level level) {
/* 21 */     super(type, level);
/*    */   }
/*    */   
/*    */   public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
/* 26 */     if (!player.isSecondaryUseActive() && !isVehicle() && (level().isClientSide() || player.startRiding((Entity)this))) {
/* 27 */       this.playerRotationOffset = this.rotationOffset;
/* 28 */       if (!level().isClientSide())
/* 29 */         return player.startRiding((Entity)this) ? (InteractionResult)InteractionResult.CONSUME : (InteractionResult)InteractionResult.PASS; 
/* 31 */       return (InteractionResult)InteractionResult.SUCCESS;
/*    */     } 
/* 33 */     return (InteractionResult)InteractionResult.PASS;
/*    */   }
/*    */   
/*    */   protected Item getDropItem() {
/* 38 */     return Items.MINECART;
/*    */   }
/*    */   
/*    */   public ItemStack getPickResult() {
/* 43 */     return new ItemStack((ItemLike)Items.MINECART);
/*    */   }
/*    */   
/*    */   public void activateMinecart(ServerLevel level, int xt, int yt, int zt, boolean state) {
/* 48 */     if (state) {
/* 49 */       if (isVehicle())
/* 50 */         ejectPassengers(); 
/* 52 */       if (getHurtTime() == 0) {
/* 53 */         setHurtDir(-getHurtDir());
/* 54 */         setHurtTime(10);
/* 55 */         setDamage(50.0F);
/* 56 */         markHurt();
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean isRideable() {
/* 63 */     return true;
/*    */   }
/*    */   
/*    */   public void tick() {
/* 68 */     double lastKnownYRot = getYRot();
/* 69 */     Vec3 lastKnownPos = position();
/* 70 */     super.tick();
/* 71 */     double tickDiff = (getYRot() - lastKnownYRot) % 360.0D;
/* 72 */     if (level().isClientSide() && lastKnownPos.distanceTo(position()) > 0.01D) {
/* 73 */       this.rotationOffset += (float)tickDiff;
/* 74 */       this.rotationOffset %= 360.0F;
/*    */     } 
/*    */   }
/*    */   
/*    */   protected void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {
/* 80 */     super.positionRider(passenger, moveFunction);
/* 81 */     if (level().isClientSide() && passenger instanceof Player) {
/* 81 */       Player player = (Player)passenger;
/* 81 */       if (player.shouldRotateWithMinecart() && useExperimentalMovement(level())) {
/* 82 */         float yRot = (float)Mth.rotLerp(0.5D, this.playerRotationOffset, this.rotationOffset);
/* 83 */         player.setYRot(player.getYRot() - yRot - this.playerRotationOffset);
/* 84 */         this.playerRotationOffset = yRot;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\vehicle\minecart\Minecart.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */