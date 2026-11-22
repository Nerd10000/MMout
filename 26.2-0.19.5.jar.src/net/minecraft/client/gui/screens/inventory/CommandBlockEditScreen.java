/*    */ package net.minecraft.client.gui.screens.inventory;
/*    */ 
/*    */ import net.minecraft.client.gui.components.CycleButton;
/*    */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.game.ServerboundSetCommandBlockPacket;
/*    */ import net.minecraft.world.level.BaseCommandBlock;
/*    */ import net.minecraft.world.level.block.entity.CommandBlockEntity;
/*    */ 
/*    */ public class CommandBlockEditScreen extends AbstractCommandBlockEditScreen {
/*    */   private final CommandBlockEntity autoCommandBlock;
/*    */   
/*    */   private CycleButton<CommandBlockEntity.Mode> modeButton;
/*    */   
/*    */   private CycleButton<Boolean> conditionalButton;
/*    */   
/*    */   private CycleButton<Boolean> autoexecButton;
/*    */   
/* 14 */   private CommandBlockEntity.Mode mode = CommandBlockEntity.Mode.REDSTONE;
/*    */   
/*    */   private boolean conditional;
/*    */   
/*    */   private boolean autoexec;
/*    */   
/*    */   public CommandBlockEditScreen(CommandBlockEntity commandBlock) {
/* 19 */     this.autoCommandBlock = commandBlock;
/*    */   }
/*    */   
/*    */   protected BaseCommandBlock getCommandBlock() {
/* 24 */     return this.autoCommandBlock.getCommandBlock();
/*    */   }
/*    */   
/*    */   protected int getPreviousY() {
/* 29 */     return 135;
/*    */   }
/*    */   
/*    */   protected void init() {
/* 34 */     super.init();
/* 35 */     enableControls(false);
/*    */   }
/*    */   
/*    */   protected void addExtraControls() {
/* 40 */     this.modeButton = (CycleButton<CommandBlockEntity.Mode>)addRenderableWidget((GuiEventListener)CycleButton.builder(mode -> {
/*    */             switch (mode) {
/*    */               default:
/*    */                 throw new MatchException(null, null);
/*    */               case SEQUENCE:
/*    */               
/*    */               case AUTO:
/*    */               
/*    */               case REDSTONE:
/*    */                 break;
/*    */             } 
/*    */             return (Component)Component.translatable("advMode.mode.redstone");
/* 40 */           }this.mode)
/*    */         
/* 48 */         .withValues((Object[])CommandBlockEntity.Mode.values())
/* 49 */         .displayOnlyValue()
/* 50 */         .create(this.width / 2 - 50 - 100 - 4, 165, 100, 20, (Component)Component.translatable("advMode.mode"), (button, value) -> this.mode = value));
/* 53 */     this.conditionalButton = (CycleButton<Boolean>)addRenderableWidget(
/* 54 */         (GuiEventListener)CycleButton.booleanBuilder((Component)Component.translatable("advMode.mode.conditional"), (Component)Component.translatable("advMode.mode.unconditional"), this.conditional)
/* 55 */         .displayOnlyValue()
/* 56 */         .create(this.width / 2 - 50, 165, 100, 20, (Component)Component.translatable("advMode.type"), (button, value) -> this.conditional = value.booleanValue()));
/* 59 */     this.autoexecButton = (CycleButton<Boolean>)addRenderableWidget(
/* 60 */         (GuiEventListener)CycleButton.booleanBuilder((Component)Component.translatable("advMode.mode.autoexec.bat"), (Component)Component.translatable("advMode.mode.redstoneTriggered"), this.autoexec)
/* 61 */         .displayOnlyValue()
/* 62 */         .create(this.width / 2 + 50 + 4, 165, 100, 20, (Component)Component.translatable("advMode.triggering"), (button, value) -> this.autoexec = value.booleanValue()));
/*    */   }
/*    */   
/*    */   private void enableControls(boolean state) {
/* 67 */     this.doneButton.active = state;
/* 68 */     this.outputButton.active = state;
/* 69 */     this.modeButton.active = state;
/* 70 */     this.conditionalButton.active = state;
/* 71 */     this.autoexecButton.active = state;
/*    */   }
/*    */   
/*    */   public void updateGui() {
/* 75 */     BaseCommandBlock commandBlock = this.autoCommandBlock.getCommandBlock();
/* 76 */     this.commandEdit.setValue(commandBlock.getCommand());
/* 77 */     boolean trackOutput = commandBlock.isTrackOutput();
/* 78 */     this.mode = this.autoCommandBlock.getMode();
/* 79 */     this.conditional = this.autoCommandBlock.isConditional();
/* 80 */     this.autoexec = this.autoCommandBlock.isAutomatic();
/* 82 */     this.outputButton.setValue(Boolean.valueOf(trackOutput));
/* 83 */     this.modeButton.setValue(this.mode);
/* 84 */     this.conditionalButton.setValue(Boolean.valueOf(this.conditional));
/* 85 */     this.autoexecButton.setValue(Boolean.valueOf(this.autoexec));
/* 87 */     updatePreviousOutput(trackOutput);
/* 88 */     enableControls(true);
/*    */   }
/*    */   
/*    */   public void resize(int width, int height) {
/* 93 */     super.resize(width, height);
/* 94 */     enableControls(true);
/*    */   }
/*    */   
/*    */   protected void populateAndSendPacket() {
/* 99 */     this.minecraft.getConnection().send((Packet)new ServerboundSetCommandBlockPacket(this.autoCommandBlock.getBlockPos(), this.commandEdit.getValue(), this.mode, this.autoCommandBlock.getCommandBlock().isTrackOutput(), this.conditional, this.autoexec));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\CommandBlockEditScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */