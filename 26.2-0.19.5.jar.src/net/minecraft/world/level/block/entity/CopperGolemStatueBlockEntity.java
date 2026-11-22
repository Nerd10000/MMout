/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.core.component.DataComponentMap;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityTypes;
/*    */ import net.minecraft.world.entity.animal.golem.CopperGolem;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.BlockItemStateProperties;
/*    */ import net.minecraft.world.level.block.CopperGolemStatueBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class CopperGolemStatueBlockEntity extends BlockEntity {
/*    */   public CopperGolemStatueBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 22 */     super(BlockEntityTypes.COPPER_GOLEM_STATUE, worldPosition, blockState);
/*    */   }
/*    */   
/*    */   public void createStatue(CopperGolem copperGolem) {
/* 26 */     setComponents(DataComponentMap.builder()
/* 27 */         .addAll(components())
/* 28 */         .set(DataComponents.CUSTOM_NAME, copperGolem.getCustomName())
/* 29 */         .build());
/* 30 */     setChanged();
/*    */   }
/*    */   
/*    */   public CopperGolem removeStatue(BlockState state) {
/* 34 */     CopperGolem copperGolem = (CopperGolem)EntityTypes.COPPER_GOLEM.create(this.level, EntitySpawnReason.TRIGGERED);
/* 35 */     if (copperGolem != null) {
/* 36 */       copperGolem.setCustomName((Component)components().get(DataComponents.CUSTOM_NAME));
/* 37 */       return initCopperGolem(state, copperGolem);
/*    */     } 
/* 39 */     return null;
/*    */   }
/*    */   
/*    */   private CopperGolem initCopperGolem(BlockState state, CopperGolem copperGolem) {
/* 43 */     BlockPos blockPos = getBlockPos();
/* 44 */     copperGolem.snapTo((Vec3.atCenterOf((Vec3i)blockPos)).x, blockPos.getY(), (Vec3.atCenterOf((Vec3i)blockPos)).z, ((Direction)state.getValue((Property)CopperGolemStatueBlock.FACING)).toYRot(), 0.0F);
/* 45 */     copperGolem.yHeadRot = copperGolem.getYRot();
/* 46 */     copperGolem.yBodyRot = copperGolem.getYRot();
/* 47 */     copperGolem.playSpawnSound();
/* 48 */     return copperGolem;
/*    */   }
/*    */   
/*    */   public ClientboundBlockEntityDataPacket getUpdatePacket() {
/* 53 */     return ClientboundBlockEntityDataPacket.create(this);
/*    */   }
/*    */   
/*    */   public ItemStack getItem(ItemStack itemStack, CopperGolemStatueBlock.Pose pose) {
/* 57 */     itemStack.applyComponents(collectComponents());
/* 58 */     itemStack.set(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with((Property)CopperGolemStatueBlock.POSE, (Comparable)pose));
/* 59 */     return itemStack;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\CopperGolemStatueBlockEntity.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */