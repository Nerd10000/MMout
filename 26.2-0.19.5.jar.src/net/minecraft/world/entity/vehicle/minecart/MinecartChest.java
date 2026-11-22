/*    */ package net.minecraft.world.entity.vehicle.minecart;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.ContainerUser;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.monster.piglin.PiglinAi;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.inventory.ChestMenu;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.ChestBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class MinecartChest extends AbstractMinecartContainer {
/*    */   public MinecartChest(EntityType<? extends MinecartChest> type, Level level) {
/* 26 */     super(type, level);
/*    */   }
/*    */   
/*    */   protected Item getDropItem() {
/* 31 */     return Items.CHEST_MINECART;
/*    */   }
/*    */   
/*    */   public ItemStack getPickResult() {
/* 36 */     return new ItemStack((ItemLike)Items.CHEST_MINECART);
/*    */   }
/*    */   
/*    */   public int getContainerSize() {
/* 41 */     return 27;
/*    */   }
/*    */   
/*    */   public BlockState getDefaultDisplayBlockState() {
/* 46 */     return (BlockState)Blocks.CHEST.defaultBlockState().setValue((Property)ChestBlock.FACING, (Comparable)Direction.NORTH);
/*    */   }
/*    */   
/*    */   public int getDefaultDisplayOffset() {
/* 51 */     return 8;
/*    */   }
/*    */   
/*    */   public AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
/* 56 */     return (AbstractContainerMenu)ChestMenu.threeRows(containerId, inventory, (Container)this);
/*    */   }
/*    */   
/*    */   public void stopOpen(ContainerUser containerUser) {
/* 61 */     level().gameEvent((Holder)GameEvent.CONTAINER_CLOSE, position(), GameEvent.Context.of((Entity)containerUser.getLivingEntity()));
/*    */   }
/*    */   
/*    */   public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
/* 66 */     InteractionResult result = interactWithContainerVehicle(player);
/* 67 */     if (result.consumesAction()) {
/* 67 */       Level level = player.level();
/* 67 */       if (level instanceof ServerLevel) {
/* 67 */         ServerLevel serverLevel = (ServerLevel)level;
/* 68 */         gameEvent((Holder)GameEvent.CONTAINER_OPEN, (Entity)player);
/* 69 */         PiglinAi.angerNearbyPiglins(serverLevel, player, true);
/*    */       } 
/*    */     } 
/* 71 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\vehicle\minecart\MinecartChest.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */