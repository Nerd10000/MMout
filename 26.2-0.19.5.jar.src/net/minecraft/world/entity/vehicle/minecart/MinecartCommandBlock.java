/*     */ package net.minecraft.world.entity.vehicle.minecart;
/*     */ 
/*     */ import net.minecraft.commands.CommandSource;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.server.permissions.PermissionSet;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BaseCommandBlock;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class MinecartCommandBlock extends AbstractMinecart {
/*  29 */   private static final EntityDataAccessor<String> DATA_ID_COMMAND_NAME = SynchedEntityData.defineId(MinecartCommandBlock.class, EntityDataSerializers.STRING);
/*     */   
/*  30 */   private static final EntityDataAccessor<Component> DATA_ID_LAST_OUTPUT = SynchedEntityData.defineId(MinecartCommandBlock.class, EntityDataSerializers.COMPONENT);
/*     */   
/*  32 */   private final BaseCommandBlock commandBlock = new MinecartCommandBase();
/*     */   
/*     */   private static final int ACTIVATION_DELAY = 4;
/*     */   
/*     */   private int lastActivated;
/*     */   
/*     */   public MinecartCommandBlock(EntityType<? extends MinecartCommandBlock> type, Level level) {
/*  38 */     super(type, level);
/*     */   }
/*     */   
/*     */   protected Item getDropItem() {
/*  43 */     return Items.MINECART;
/*     */   }
/*     */   
/*     */   public ItemStack getPickResult() {
/*  48 */     return new ItemStack((ItemLike)Items.COMMAND_BLOCK_MINECART);
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  53 */     super.defineSynchedData(entityData);
/*  54 */     entityData.define(DATA_ID_COMMAND_NAME, "");
/*  55 */     entityData.define(DATA_ID_LAST_OUTPUT, CommonComponents.EMPTY);
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/*  60 */     super.readAdditionalSaveData(input);
/*  61 */     this.commandBlock.load(input);
/*  62 */     getEntityData().set(DATA_ID_COMMAND_NAME, getCommandBlock().getCommand());
/*  63 */     getEntityData().set(DATA_ID_LAST_OUTPUT, getCommandBlock().getLastOutput());
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  68 */     super.addAdditionalSaveData(output);
/*  69 */     this.commandBlock.save(output);
/*     */   }
/*     */   
/*     */   public BlockState getDefaultDisplayBlockState() {
/*  74 */     return Blocks.COMMAND_BLOCK.defaultBlockState();
/*     */   }
/*     */   
/*     */   public BaseCommandBlock getCommandBlock() {
/*  78 */     return this.commandBlock;
/*     */   }
/*     */   
/*     */   public void activateMinecart(ServerLevel level, int xt, int yt, int zt, boolean state) {
/*  83 */     if (state && 
/*  84 */       this.tickCount - this.lastActivated >= 4) {
/*  85 */       getCommandBlock().performCommand(level);
/*  86 */       this.lastActivated = this.tickCount;
/*     */     } 
/*     */   }
/*     */   
/*     */   public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
/*  93 */     if (!player.canUseGameMasterBlocks())
/*  94 */       return (InteractionResult)InteractionResult.PASS; 
/*  96 */     if (player.level().isClientSide())
/*  97 */       player.openMinecartCommandBlock(this); 
/*  99 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 104 */     super.onSyncedDataUpdated(accessor);
/* 106 */     if (DATA_ID_LAST_OUTPUT.equals(accessor)) {
/*     */       try {
/* 108 */         this.commandBlock.setLastOutput((Component)getEntityData().get(DATA_ID_LAST_OUTPUT));
/* 109 */       } catch (Throwable throwable) {}
/* 111 */     } else if (DATA_ID_COMMAND_NAME.equals(accessor)) {
/* 112 */       this.commandBlock.setCommand((String)getEntityData().get(DATA_ID_COMMAND_NAME));
/*     */     } 
/*     */   }
/*     */   
/*     */   private class MinecartCommandBase extends BaseCommandBlock {
/*     */     public void onUpdated(ServerLevel level) {
/* 119 */       MinecartCommandBlock.this.getEntityData().set(MinecartCommandBlock.DATA_ID_COMMAND_NAME, getCommand());
/* 120 */       MinecartCommandBlock.this.getEntityData().set(MinecartCommandBlock.DATA_ID_LAST_OUTPUT, getLastOutput());
/*     */     }
/*     */     
/*     */     public CommandSourceStack createCommandSourceStack(ServerLevel level, CommandSource source) {
/* 125 */       return new CommandSourceStack(source, MinecartCommandBlock.this.position(), MinecartCommandBlock.this.getRotationVector(), level, (PermissionSet)LevelBasedPermissionSet.GAMEMASTER, getName().getString(), MinecartCommandBlock.this.getDisplayName(), level.getServer(), (Entity)MinecartCommandBlock.this);
/*     */     }
/*     */     
/*     */     public boolean isValid() {
/* 130 */       return !MinecartCommandBlock.this.isRemoved();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\vehicle\minecart\MinecartCommandBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */