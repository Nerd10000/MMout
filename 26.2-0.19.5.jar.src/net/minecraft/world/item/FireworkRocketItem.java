/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.core.dispenser.BlockSource;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.entity.projectile.FireworkRocketEntity;
/*    */ import net.minecraft.world.entity.projectile.Projectile;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class FireworkRocketItem extends Item implements ProjectileItem {
/* 22 */   public static final byte[] CRAFTABLE_DURATIONS = new byte[] { 1, 2, 3 };
/*    */   
/*    */   public static final double ROCKET_PLACEMENT_OFFSET = 0.15D;
/*    */   
/*    */   public FireworkRocketItem(Item.Properties properties) {
/* 27 */     super(properties);
/*    */   }
/*    */   
/*    */   public InteractionResult useOn(UseOnContext context) {
/* 32 */     Level level = context.getLevel();
/* 35 */     Player player = context.getPlayer();
/* 36 */     if (player != null && player.isFallFlying())
/* 37 */       return (InteractionResult)InteractionResult.PASS; 
/* 40 */     if (level instanceof ServerLevel) {
/* 40 */       ServerLevel serverLevel = (ServerLevel)level;
/* 41 */       ItemStack itemStack = context.getItemInHand();
/* 43 */       Vec3 clickLocation = context.getClickLocation();
/* 44 */       Direction direction = context.getClickedFace();
/* 46 */       Projectile.spawnProjectile((Projectile)new FireworkRocketEntity(level, (Entity)context
/*    */             
/* 48 */             .getPlayer(), clickLocation.x + direction
/* 49 */             .getStepX() * 0.15D, clickLocation.y + direction
/* 50 */             .getStepY() * 0.15D, clickLocation.z + direction
/* 51 */             .getStepZ() * 0.15D, itemStack), serverLevel, itemStack);
/* 55 */       itemStack.shrink(1);
/*    */     } 
/* 57 */     return (InteractionResult)InteractionResult.SUCCESS;
/*    */   }
/*    */   
/*    */   public InteractionResult use(Level level, Player player, InteractionHand hand) {
/* 62 */     if (player.isFallFlying()) {
/* 63 */       ItemStack itemStack = player.getItemInHand(hand);
/* 64 */       if (level instanceof ServerLevel) {
/* 64 */         ServerLevel serverLevel = (ServerLevel)level;
/* 65 */         if (player.dropAllLeashConnections(null))
/* 66 */           level.playSound(null, (Entity)player, SoundEvents.LEAD_BREAK, SoundSource.NEUTRAL, 1.0F, 1.0F); 
/* 68 */         Projectile.spawnProjectile((Projectile)new FireworkRocketEntity(level, itemStack, (LivingEntity)player), serverLevel, itemStack);
/* 69 */         itemStack.consume(1, (LivingEntity)player);
/* 70 */         player.awardStat(Stats.ITEM_USED.get(this));
/*    */       } 
/* 73 */       return (InteractionResult)InteractionResult.SUCCESS;
/*    */     } 
/* 75 */     return (InteractionResult)InteractionResult.PASS;
/*    */   }
/*    */   
/*    */   public Projectile asProjectile(Level level, Position position, ItemStack itemStack, Direction direction) {
/* 81 */     return (Projectile)new FireworkRocketEntity(level, itemStack.copyWithCount(1), position.x(), position.y(), position.z(), true);
/*    */   }
/*    */   
/*    */   public ProjectileItem.DispenseConfig createDispenseConfig() {
/* 86 */     return ProjectileItem.DispenseConfig.builder()
/* 87 */       .positionFunction(FireworkRocketItem::getEntityJustOutsideOfBlockPos)
/* 88 */       .uncertainty(1.0F)
/* 89 */       .power(0.5F)
/* 90 */       .overrideDispenseEvent(1004)
/* 91 */       .build();
/*    */   }
/*    */   
/*    */   private static Vec3 getEntityJustOutsideOfBlockPos(BlockSource source, Direction direction) {
/* 95 */     return source.center().add(direction
/* 96 */         .getStepX() * 0.5000099999997474D, direction
/* 97 */         .getStepY() * 0.5000099999997474D, direction
/* 98 */         .getStepZ() * 0.5000099999997474D);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\FireworkRocketItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */