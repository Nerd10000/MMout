/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public enum Action implements StringRepresentable {
/* 129 */   OPEN_URL("open_url", true, (MapCodec)ClickEvent.OpenUrl.CODEC),
/* 130 */   OPEN_FILE("open_file", false, (MapCodec)ClickEvent.OpenFile.CODEC),
/* 131 */   RUN_COMMAND("run_command", true, (MapCodec)ClickEvent.RunCommand.CODEC),
/* 132 */   SUGGEST_COMMAND("suggest_command", true, (MapCodec)ClickEvent.SuggestCommand.CODEC),
/* 133 */   SHOW_DIALOG("show_dialog", true, (MapCodec)ClickEvent.ShowDialog.CODEC),
/* 134 */   CHANGE_PAGE("change_page", true, (MapCodec)ClickEvent.ChangePage.CODEC),
/* 135 */   COPY_TO_CLIPBOARD("copy_to_clipboard", true, (MapCodec)ClickEvent.CopyToClipboard.CODEC),
/* 136 */   CUSTOM("custom", true, (MapCodec)ClickEvent.Custom.CODEC);
/*     */   
/*     */   public static final Codec<Action> UNSAFE_CODEC;
/*     */   
/*     */   public static final Codec<Action> CODEC;
/*     */   
/*     */   private final boolean allowFromServer;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final MapCodec<? extends ClickEvent> codec;
/*     */   
/*     */   static {
/* 139 */     UNSAFE_CODEC = (Codec<Action>)StringRepresentable.fromEnum(Action::values);
/* 140 */     CODEC = UNSAFE_CODEC.validate(Action::filterForSerialization);
/*     */   }
/*     */   
/*     */   Action(String name, boolean allowFromServer, MapCodec<? extends ClickEvent> codec) {
/* 147 */     this.name = name;
/* 148 */     this.allowFromServer = allowFromServer;
/* 149 */     this.codec = codec;
/*     */   }
/*     */   
/*     */   public boolean isAllowedFromServer() {
/* 153 */     return this.allowFromServer;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 158 */     return this.name;
/*     */   }
/*     */   
/*     */   public MapCodec<? extends ClickEvent> valueCodec() {
/* 162 */     return this.codec;
/*     */   }
/*     */   
/*     */   public static DataResult<Action> filterForSerialization(Action action) {
/* 166 */     if (!action.isAllowedFromServer())
/* 167 */       return DataResult.error(() -> "Click event type not allowed: " + String.valueOf(action)); 
/* 169 */     return DataResult.success(action, Lifecycle.stable());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\ClickEvent$Action.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */