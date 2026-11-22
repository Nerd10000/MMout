/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.MenuProvider;
/*    */ import net.minecraft.world.SimpleMenuProvider;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.inventory.CartographyTableMenu;
/*    */ import net.minecraft.world.inventory.ContainerLevelAccess;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class CartographyTableBlock extends Block {
/* 19 */   public static final MapCodec<CartographyTableBlock> CODEC = simpleCodec(CartographyTableBlock::new);
/*    */   
/*    */   public MapCodec<CartographyTableBlock> codec() {
/* 23 */     return CODEC;
/*    */   }
/*    */   
/* 26 */   private static final Component CONTAINER_TITLE = (Component)Component.translatable("container.cartography_table");
/*    */   
/*    */   protected CartographyTableBlock(BlockBehaviour.Properties properties) {
/* 29 */     super(properties);
/*    */   }
/*    */   
/*    */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/* 34 */     if (!level.isClientSide()) {
/* 35 */       player.openMenu(state.getMenuProvider(level, pos));
/* 36 */       player.awardStat(Stats.INTERACT_WITH_CARTOGRAPHY_TABLE);
/*    */     } 
/* 38 */     return (InteractionResult)InteractionResult.SUCCESS;
/*    */   }
/*    */   
/*    */   protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
/* 43 */     return (MenuProvider)new SimpleMenuProvider((containerId, inventory, player) -> new CartographyTableMenu(containerId, inventory, ContainerLevelAccess.create(level, pos)), CONTAINER_TITLE);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\CartographyTableBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */