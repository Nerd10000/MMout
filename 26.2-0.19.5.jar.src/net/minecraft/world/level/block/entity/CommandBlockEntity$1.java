/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import net.minecraft.commands.CommandSource;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.server.permissions.PermissionSet;
/*     */ import net.minecraft.world.level.BaseCommandBlock;
/*     */ import net.minecraft.world.level.block.CommandBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ class null extends BaseCommandBlock {
/*     */   public void setCommand(String command) {
/* 161 */     super.setCommand(command);
/* 162 */     CommandBlockEntity.this.setChanged();
/*     */   }
/*     */   
/*     */   public void onUpdated(ServerLevel level) {
/* 167 */     BlockState state = level.getBlockState(CommandBlockEntity.this.worldPosition);
/* 168 */     level.sendBlockUpdated(CommandBlockEntity.this.worldPosition, state, state, 3);
/*     */   }
/*     */   
/*     */   public CommandSourceStack createCommandSourceStack(ServerLevel level, CommandSource source) {
/* 173 */     Direction facing = (Direction)CommandBlockEntity.this.getBlockState().getValue((Property)CommandBlock.FACING);
/* 174 */     return new CommandSourceStack(source, Vec3.atCenterOf((Vec3i)CommandBlockEntity.this.worldPosition), new Vec2(0.0F, facing.toYRot()), level, (PermissionSet)LevelBasedPermissionSet.GAMEMASTER, getName().getString(), getName(), level.getServer(), null);
/*     */   }
/*     */   
/*     */   public boolean isValid() {
/* 179 */     return !CommandBlockEntity.this.isRemoved();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\CommandBlockEntity$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */