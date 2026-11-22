/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.EntityTypes;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.PostSpawnProcessor;
/*    */ import net.minecraft.world.entity.decoration.ArmorStand;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class ArmorStandItem extends Item {
/*    */   public ArmorStandItem(Item.Properties properties) {
/* 24 */     super(properties);
/*    */   }
/*    */   
/*    */   public InteractionResult useOn(UseOnContext context) {
/* 29 */     Direction clickedFace = context.getClickedFace();
/* 30 */     if (clickedFace == Direction.DOWN)
/* 31 */       return (InteractionResult)InteractionResult.FAIL; 
/* 34 */     Level level = context.getLevel();
/* 35 */     BlockPlaceContext placeContext = new BlockPlaceContext(context);
/* 36 */     BlockPos blockPos = placeContext.getClickedPos();
/* 38 */     ItemStack itemStack = context.getItemInHand();
/* 39 */     Vec3 pos = Vec3.atBottomCenterOf((Vec3i)blockPos);
/* 40 */     AABB box = EntityTypes.ARMOR_STAND.getDimensions().makeBoundingBox(pos.x(), pos.y(), pos.z());
/* 42 */     if (!level.noCollision(null, box) || !level.getEntities(null, box).isEmpty())
/* 43 */       return (InteractionResult)InteractionResult.FAIL; 
/* 46 */     if (level instanceof ServerLevel) {
/* 46 */       ServerLevel serverLevel = (ServerLevel)level;
/* 47 */       PostSpawnProcessor<ArmorStand> entityConfig = EntityType.createDefaultStackConfig((Level)serverLevel, itemStack, (LivingEntity)context.getPlayer());
/* 48 */       ArmorStand entity = (ArmorStand)EntityTypes.ARMOR_STAND.create(serverLevel, entityConfig, blockPos, EntitySpawnReason.SPAWN_ITEM_USE, true, true);
/* 50 */       if (entity == null)
/* 51 */         return (InteractionResult)InteractionResult.FAIL; 
/* 54 */       float yRot = Mth.floor((Mth.wrapDegrees(context.getRotation() - 180.0F) + 22.5F) / 45.0F) * 45.0F;
/* 55 */       entity.snapTo(entity.getX(), entity.getY(), entity.getZ(), yRot, 0.0F);
/* 57 */       serverLevel.addFreshEntityWithPassengers((Entity)entity);
/* 59 */       level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ARMOR_STAND_PLACE, SoundSource.BLOCKS, 0.75F, 0.8F);
/* 60 */       entity.gameEvent((Holder)GameEvent.ENTITY_PLACE, (Entity)context.getPlayer());
/*    */     } 
/* 63 */     itemStack.shrink(1);
/* 64 */     return (InteractionResult)InteractionResult.SUCCESS;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\ArmorStandItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */