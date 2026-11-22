/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import net.minecraft.commands.CommandSource;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.server.permissions.PermissionSet;
/*     */ import net.minecraft.world.level.BaseCommandBlock;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.CommandBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class CommandBlockEntity extends BlockEntity {
/*     */   private static final boolean DEFAULT_POWERED = false;
/*     */   
/*     */   private static final boolean DEFAULT_CONDITION_MET = false;
/*     */   
/*     */   private static final boolean DEFAULT_AUTOMATIC = false;
/*     */   
/*     */   private boolean powered = false;
/*     */   
/*     */   private boolean auto = false;
/*     */   
/*     */   private boolean conditionMet = false;
/*     */   
/*     */   private final BaseCommandBlock commandBlock;
/*     */   
/*     */   public CommandBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  32 */     super(BlockEntityTypes.COMMAND_BLOCK, worldPosition, blockState);
/* 158 */     this.commandBlock = new BaseCommandBlock() {
/*     */         public void setCommand(String command) {
/* 161 */           super.setCommand(command);
/* 162 */           CommandBlockEntity.this.setChanged();
/*     */         }
/*     */         
/*     */         public void onUpdated(ServerLevel level) {
/* 167 */           BlockState state = level.getBlockState(CommandBlockEntity.this.worldPosition);
/* 168 */           level.sendBlockUpdated(CommandBlockEntity.this.worldPosition, state, state, 3);
/*     */         }
/*     */         
/*     */         public CommandSourceStack createCommandSourceStack(ServerLevel level, CommandSource source) {
/* 173 */           Direction facing = (Direction)CommandBlockEntity.this.getBlockState().getValue((Property)CommandBlock.FACING);
/* 174 */           return new CommandSourceStack(source, Vec3.atCenterOf((Vec3i)CommandBlockEntity.this.worldPosition), new Vec2(0.0F, facing.toYRot()), level, (PermissionSet)LevelBasedPermissionSet.GAMEMASTER, getName().getString(), getName(), level.getServer(), null);
/*     */         }
/*     */         
/*     */         public boolean isValid() {
/* 179 */           return !CommandBlockEntity.this.isRemoved();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/*     */     super.saveAdditional(output);
/*     */     this.commandBlock.save(output);
/*     */     output.putBoolean("powered", isPowered());
/*     */     output.putBoolean("conditionMet", wasConditionMet());
/*     */     output.putBoolean("auto", isAutomatic());
/*     */   }
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/*     */     super.loadAdditional(input);
/*     */     this.commandBlock.load(input);
/*     */     this.powered = input.getBooleanOr("powered", false);
/*     */     this.conditionMet = input.getBooleanOr("conditionMet", false);
/*     */     setAutomatic(input.getBooleanOr("auto", false));
/*     */   }
/*     */   
/*     */   public BaseCommandBlock getCommandBlock() {
/*     */     return this.commandBlock;
/*     */   }
/*     */   
/*     */   public void setPowered(boolean powered) {
/*     */     this.powered = powered;
/*     */   }
/*     */   
/*     */   public boolean isPowered() {
/*     */     return this.powered;
/*     */   }
/*     */   
/*     */   public boolean isAutomatic() {
/*     */     return this.auto;
/*     */   }
/*     */   
/*     */   public void setAutomatic(boolean auto) {
/*     */     boolean previousAuto = this.auto;
/*     */     this.auto = auto;
/*     */     if (!previousAuto && auto && !this.powered && this.level != null && getMode() != Mode.SEQUENCE)
/*     */       scheduleTick(); 
/*     */   }
/*     */   
/*     */   public void onModeSwitch() {
/*     */     Mode newMode = getMode();
/*     */     if (newMode == Mode.AUTO && (this.powered || this.auto) && this.level != null)
/*     */       scheduleTick(); 
/*     */   }
/*     */   
/*     */   private void scheduleTick() {
/*     */     Block commandBlock = getBlockState().getBlock();
/*     */     if (commandBlock instanceof CommandBlock) {
/*     */       markConditionMet();
/*     */       this.level.scheduleTick(this.worldPosition, commandBlock, 1);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean wasConditionMet() {
/*     */     return this.conditionMet;
/*     */   }
/*     */   
/*     */   public boolean markConditionMet() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: iconst_1
/*     */     //   2: putfield conditionMet : Z
/*     */     //   5: aload_0
/*     */     //   6: invokevirtual isConditional : ()Z
/*     */     //   9: ifeq -> 108
/*     */     //   12: aload_0
/*     */     //   13: getfield worldPosition : Lnet/minecraft/core/BlockPos;
/*     */     //   16: aload_0
/*     */     //   17: getfield level : Lnet/minecraft/world/level/Level;
/*     */     //   20: aload_0
/*     */     //   21: getfield worldPosition : Lnet/minecraft/core/BlockPos;
/*     */     //   24: invokevirtual getBlockState : (Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;
/*     */     //   27: getstatic net/minecraft/world/level/block/CommandBlock.FACING : Lnet/minecraft/world/level/block/state/properties/EnumProperty;
/*     */     //   30: invokevirtual getValue : (Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;
/*     */     //   33: checkcast net/minecraft/core/Direction
/*     */     //   36: invokevirtual getOpposite : ()Lnet/minecraft/core/Direction;
/*     */     //   39: invokevirtual relative : (Lnet/minecraft/core/Direction;)Lnet/minecraft/core/BlockPos;
/*     */     //   42: astore_1
/*     */     //   43: aload_0
/*     */     //   44: getfield level : Lnet/minecraft/world/level/Level;
/*     */     //   47: aload_1
/*     */     //   48: invokevirtual getBlockState : (Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;
/*     */     //   51: invokevirtual getBlock : ()Lnet/minecraft/world/level/block/Block;
/*     */     //   54: instanceof net/minecraft/world/level/block/CommandBlock
/*     */     //   57: ifeq -> 103
/*     */     //   60: aload_0
/*     */     //   61: getfield level : Lnet/minecraft/world/level/Level;
/*     */     //   64: aload_1
/*     */     //   65: invokevirtual getBlockEntity : (Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/entity/BlockEntity;
/*     */     //   68: astore_2
/*     */     //   69: aload_0
/*     */     //   70: aload_2
/*     */     //   71: instanceof net/minecraft/world/level/block/entity/CommandBlockEntity
/*     */     //   74: ifeq -> 96
/*     */     //   77: aload_2
/*     */     //   78: checkcast net/minecraft/world/level/block/entity/CommandBlockEntity
/*     */     //   81: astore_3
/*     */     //   82: aload_3
/*     */     //   83: invokevirtual getCommandBlock : ()Lnet/minecraft/world/level/BaseCommandBlock;
/*     */     //   86: invokevirtual getSuccessCount : ()I
/*     */     //   89: ifle -> 96
/*     */     //   92: iconst_1
/*     */     //   93: goto -> 97
/*     */     //   96: iconst_0
/*     */     //   97: putfield conditionMet : Z
/*     */     //   100: goto -> 108
/*     */     //   103: aload_0
/*     */     //   104: iconst_0
/*     */     //   105: putfield conditionMet : Z
/*     */     //   108: aload_0
/*     */     //   109: getfield conditionMet : Z
/*     */     //   112: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #98	-> 0
/*     */     //   #99	-> 5
/*     */     //   #100	-> 12
/*     */     //   #101	-> 43
/*     */     //   #102	-> 60
/*     */     //   #103	-> 69
/*     */     //   #104	-> 100
/*     */     //   #105	-> 103
/*     */     //   #108	-> 108
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   82	14	3	commandBlockEntity	Lnet/minecraft/world/level/block/entity/CommandBlockEntity;
/*     */     //   69	31	2	backsideCommandBlock	Lnet/minecraft/world/level/block/entity/BlockEntity;
/*     */     //   43	65	1	relative	Lnet/minecraft/core/BlockPos;
/*     */     //   0	113	0	this	Lnet/minecraft/world/level/block/entity/CommandBlockEntity;
/*     */   }
/*     */   
/*     */   public Mode getMode() {
/*     */     BlockState state = getBlockState();
/*     */     if (state.is(Blocks.COMMAND_BLOCK))
/*     */       return Mode.REDSTONE; 
/*     */     if (state.is(Blocks.REPEATING_COMMAND_BLOCK))
/*     */       return Mode.AUTO; 
/*     */     if (state.is(Blocks.CHAIN_COMMAND_BLOCK))
/*     */       return Mode.SEQUENCE; 
/*     */     return Mode.REDSTONE;
/*     */   }
/*     */   
/*     */   public boolean isConditional() {
/*     */     BlockState blockState = this.level.getBlockState(getBlockPos());
/*     */     if (blockState.getBlock() instanceof CommandBlock)
/*     */       return ((Boolean)blockState.getValue((Property)CommandBlock.CONDITIONAL)).booleanValue(); 
/*     */     return false;
/*     */   }
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/*     */     super.applyImplicitComponents(components);
/*     */     this.commandBlock.setCustomName((Component)components.get(DataComponents.CUSTOM_NAME));
/*     */   }
/*     */   
/*     */   protected void collectImplicitComponents(DataComponentMap.Builder components) {
/*     */     super.collectImplicitComponents(components);
/*     */     components.set(DataComponents.CUSTOM_NAME, this.commandBlock.getCustomName());
/*     */   }
/*     */   
/*     */   public void removeComponentsFromTag(ValueOutput output) {
/*     */     super.removeComponentsFromTag(output);
/*     */     output.discard("CustomName");
/*     */     output.discard("conditionMet");
/*     */     output.discard("powered");
/*     */   }
/*     */   
/*     */   public enum Mode {
/*     */     SEQUENCE, AUTO, REDSTONE;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\CommandBlockEntity.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */