/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.component.Tool;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.GrowingPlantHeadBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ 
/*    */ public class ShearsItem extends Item {
/*    */   public ShearsItem(Item.Properties properties) {
/* 30 */     super(properties);
/*    */   }
/*    */   
/*    */   public static Tool createToolProperties() {
/* 35 */     HolderGetter<Block> registrationLookup = BuiltInRegistries.acquireBootstrapRegistrationLookup((Registry)BuiltInRegistries.BLOCK);
/* 37 */     return new Tool(
/* 38 */         List.of(
/* 39 */           Tool.Rule.minesAndDrops((HolderSet)HolderSet.direct(new Holder[] { (Holder)Blocks.COBWEB.builtInRegistryHolder() }, ), 15.0F), 
/* 40 */           Tool.Rule.overrideSpeed((HolderSet)registrationLookup.getOrThrow(BlockTags.SHEARS_EXTREME_BREAKING_SPEED), 15.0F), 
/* 41 */           Tool.Rule.overrideSpeed((HolderSet)registrationLookup.getOrThrow(BlockTags.SHEARS_MAJOR_BREAKING_SPEED), 5.0F), 
/* 42 */           Tool.Rule.overrideSpeed((HolderSet)registrationLookup.getOrThrow(BlockTags.SHEARS_MINOR_BREAKING_SPEED), 2.0F)), 1.0F, 1, true);
/*    */   }
/*    */   
/*    */   public boolean mineBlock(ItemStack itemStack, Level level, BlockState state, BlockPos pos, LivingEntity miner) {
/* 52 */     Tool tool = (Tool)itemStack.get(DataComponents.TOOL);
/* 53 */     if (tool == null)
/* 54 */       return false; 
/* 57 */     if (!level.isClientSide() && !state.is(BlockTags.FIRE) && tool.damagePerBlock() > 0)
/* 58 */       itemStack.hurtAndBreak(tool.damagePerBlock(), miner, EquipmentSlot.MAINHAND); 
/* 60 */     return true;
/*    */   }
/*    */   
/*    */   public InteractionResult useOn(UseOnContext context) {
/* 65 */     Level level = context.getLevel();
/* 66 */     BlockPos pos = context.getClickedPos();
/* 67 */     BlockState state = level.getBlockState(pos);
/* 68 */     Block block = state.getBlock();
/* 69 */     if (block instanceof GrowingPlantHeadBlock) {
/* 69 */       GrowingPlantHeadBlock plantBlock = (GrowingPlantHeadBlock)block;
/* 70 */       if (!plantBlock.isMaxAge(state)) {
/* 71 */         Player player = context.getPlayer();
/* 72 */         ItemStack itemInHand = context.getItemInHand();
/* 73 */         if (player instanceof ServerPlayer) {
/* 73 */           ServerPlayer serverPlayer = (ServerPlayer)player;
/* 74 */           CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, itemInHand);
/*    */         } 
/* 76 */         level.playSound((Entity)player, pos, SoundEvents.GROWING_PLANT_CROP, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 77 */         BlockState newState = plantBlock.getMaxAgeState(state);
/* 78 */         level.setBlockAndUpdate(pos, newState);
/* 79 */         level.gameEvent((Holder)GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of((Entity)context.getPlayer(), newState));
/* 80 */         if (player != null)
/* 81 */           itemInHand.hurtAndBreak(1, (LivingEntity)player, context.getHand().asEquipmentSlot()); 
/* 84 */         return (InteractionResult)InteractionResult.SUCCESS;
/*    */       } 
/*    */     } 
/* 87 */     return super.useOn(context);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\ShearsItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */