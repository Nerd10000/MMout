/*     */ package net.minecraft.world.entity.vehicle.minecart;
/*     */ 
/*     */ import net.minecraft.commands.CommandSource;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.server.permissions.PermissionSet;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.BaseCommandBlock;
/*     */ 
/*     */ class MinecartCommandBase extends BaseCommandBlock {
/*     */   public void onUpdated(ServerLevel level) {
/* 119 */     MinecartCommandBlock.this.getEntityData().set(MinecartCommandBlock.DATA_ID_COMMAND_NAME, getCommand());
/* 120 */     MinecartCommandBlock.this.getEntityData().set(MinecartCommandBlock.DATA_ID_LAST_OUTPUT, getLastOutput());
/*     */   }
/*     */   
/*     */   public CommandSourceStack createCommandSourceStack(ServerLevel level, CommandSource source) {
/* 125 */     return new CommandSourceStack(source, MinecartCommandBlock.this.position(), MinecartCommandBlock.this.getRotationVector(), level, (PermissionSet)LevelBasedPermissionSet.GAMEMASTER, getName().getString(), MinecartCommandBlock.this.getDisplayName(), level.getServer(), (Entity)MinecartCommandBlock.this);
/*     */   }
/*     */   
/*     */   public boolean isValid() {
/* 130 */     return !MinecartCommandBlock.this.isRemoved();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\vehicle\minecart\MinecartCommandBlock$MinecartCommandBase.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */