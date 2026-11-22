/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.Leashable;
/*    */ import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class LeadItem extends Item {
/*    */   public LeadItem(Item.Properties properties) {
/* 20 */     super(properties);
/*    */   }
/*    */   
/*    */   public InteractionResult useOn(UseOnContext context) {
/* 25 */     Level level = context.getLevel();
/* 26 */     BlockPos pos = context.getClickedPos();
/* 28 */     BlockState state = level.getBlockState(pos);
/* 29 */     if (state.is(BlockTags.FENCES)) {
/* 30 */       Player player = context.getPlayer();
/* 31 */       if (!level.isClientSide() && player != null)
/* 32 */         return bindPlayerMobs(player, level, pos); 
/*    */     } 
/* 36 */     return (InteractionResult)InteractionResult.PASS;
/*    */   }
/*    */   
/*    */   public static InteractionResult bindPlayerMobs(Player player, Level level, BlockPos pos) {
/* 40 */     List<Leashable> entitiesToLeash = Leashable.leashableInArea(level, Vec3.atCenterOf((Vec3i)pos), l -> (l.getLeashHolder() == player));
/* 42 */     if (entitiesToLeash.isEmpty())
/* 43 */       return (InteractionResult)InteractionResult.PASS; 
/* 46 */     Optional<LeashFenceKnotEntity> existingKnot = LeashFenceKnotEntity.getKnot(level, pos);
/* 48 */     LeashFenceKnotEntity activeKnot = existingKnot.orElseGet(() -> LeashFenceKnotEntity.createKnot(level, pos));
/* 50 */     boolean anyLeashed = false;
/* 51 */     for (Leashable leashable : entitiesToLeash) {
/* 52 */       if (leashable.canHaveALeashAttachedTo((Entity)activeKnot)) {
/* 53 */         leashable.setLeashedTo((Entity)activeKnot, true);
/* 54 */         anyLeashed = true;
/*    */       } 
/*    */     } 
/* 58 */     if (anyLeashed) {
/* 59 */       activeKnot.playPlacementSound();
/* 60 */       level.gameEvent((Holder)GameEvent.BLOCK_ATTACH, pos, GameEvent.Context.of((Entity)player));
/* 61 */       return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/*    */     } 
/* 64 */     if (existingKnot.isEmpty())
/* 65 */       activeKnot.discard(); 
/* 68 */     return (InteractionResult)InteractionResult.PASS;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\LeadItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */