/*      */ package net.minecraft.world.entity.animal.fox;
/*      */ 
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EquipmentSlot;
/*      */ import net.minecraft.world.entity.PathfinderMob;
/*      */ import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.level.ItemLike;
/*      */ import net.minecraft.world.level.LevelReader;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.CaveVines;
/*      */ import net.minecraft.world.level.block.SweetBerryBushBlock;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.gameevent.GameEvent;
/*      */ import net.minecraft.world.level.gamerules.GameRules;
/*      */ 
/*      */ public class FoxEatBerriesGoal extends MoveToBlockGoal {
/*      */   private static final int WAIT_TICKS = 40;
/*      */   
/*      */   protected int ticksWaited;
/*      */   
/*      */   public FoxEatBerriesGoal(double speedModifier, int searchRange, int verticalSearchRange) {
/* 1220 */     super((PathfinderMob)Fox.this, speedModifier, searchRange, verticalSearchRange);
/*      */   }
/*      */   
/*      */   public double acceptedDistance() {
/* 1225 */     return 2.0D;
/*      */   }
/*      */   
/*      */   public boolean shouldRecalculatePath() {
/* 1230 */     return (this.tryTicks % 100 == 0);
/*      */   }
/*      */   
/*      */   protected boolean isValidTarget(LevelReader level, BlockPos pos) {
/* 1235 */     BlockState blockState = level.getBlockState(pos);
/* 1236 */     return ((blockState.is(Blocks.SWEET_BERRY_BUSH) && ((Integer)blockState.getValue((Property)SweetBerryBushBlock.AGE)).intValue() >= 2) || 
/* 1237 */       CaveVines.hasGlowBerries(blockState));
/*      */   }
/*      */   
/*      */   public void tick() {
/* 1242 */     if (isReachedTarget()) {
/* 1243 */       if (this.ticksWaited >= 40) {
/* 1244 */         onReachedTarget();
/*      */       } else {
/* 1246 */         this.ticksWaited++;
/*      */       } 
/* 1248 */     } else if (!isReachedTarget() && Fox.access$300(Fox.this).nextFloat() < 0.05F) {
/* 1249 */       Fox.this.playSound(SoundEvents.FOX_SNIFF, 1.0F, 1.0F);
/*      */     } 
/* 1252 */     super.tick();
/*      */   }
/*      */   
/*      */   protected void onReachedTarget() {
/* 1256 */     if (!((Boolean)getServerLevel(Fox.this.level()).getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue())
/*      */       return; 
/* 1260 */     BlockState state = Fox.this.level().getBlockState(this.blockPos);
/* 1263 */     if (state.is(Blocks.SWEET_BERRY_BUSH)) {
/* 1264 */       pickSweetBerries(state);
/* 1265 */     } else if (CaveVines.hasGlowBerries(state)) {
/* 1266 */       pickGlowBerry(state);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void pickGlowBerry(BlockState state) {
/* 1271 */     CaveVines.use((Entity)Fox.this, state, Fox.this.level(), this.blockPos);
/*      */   }
/*      */   
/*      */   private void pickSweetBerries(BlockState state) {
/* 1275 */     int age = ((Integer)state.getValue((Property)SweetBerryBushBlock.AGE)).intValue();
/* 1276 */     state.setValue((Property)SweetBerryBushBlock.AGE, Integer.valueOf(1));
/* 1277 */     int count = 1 + Fox.this.level().getRandom().nextInt(2) + ((age == 3) ? 1 : 0);
/* 1278 */     ItemStack heldItem = Fox.this.getItemBySlot(EquipmentSlot.MAINHAND);
/* 1279 */     if (heldItem.isEmpty()) {
/* 1280 */       Fox.this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.SWEET_BERRIES));
/* 1281 */       count--;
/*      */     } 
/* 1283 */     if (count > 0)
/* 1284 */       Block.popResource(Fox.this.level(), this.blockPos, new ItemStack((ItemLike)Items.SWEET_BERRIES, count)); 
/* 1286 */     Fox.this.playSound(SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, 1.0F, 1.0F);
/* 1287 */     Fox.this.level().setBlock(this.blockPos, (BlockState)state.setValue((Property)SweetBerryBushBlock.AGE, Integer.valueOf(1)), 2);
/* 1288 */     Fox.this.level().gameEvent((Holder)GameEvent.BLOCK_CHANGE, this.blockPos, GameEvent.Context.of((Entity)Fox.this));
/*      */   }
/*      */   
/*      */   public boolean canUse() {
/* 1293 */     return (!Fox.this.isSleeping() && super.canUse());
/*      */   }
/*      */   
/*      */   public void start() {
/* 1298 */     this.ticksWaited = 0;
/* 1299 */     Fox.this.setSitting(false);
/* 1300 */     super.start();
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\fox\Fox$FoxEatBerriesGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */