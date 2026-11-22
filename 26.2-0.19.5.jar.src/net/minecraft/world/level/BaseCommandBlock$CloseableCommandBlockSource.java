/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import java.time.ZonedDateTime;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.util.Locale;
/*     */ import net.minecraft.commands.CommandSource;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ 
/*     */ public class CloseableCommandBlockSource implements CommandSource, AutoCloseable {
/*     */   private final ServerLevel level;
/*     */   
/* 174 */   private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.ROOT);
/*     */   
/*     */   private boolean closed;
/*     */   
/*     */   protected CloseableCommandBlockSource(ServerLevel level) {
/* 179 */     this.level = level;
/*     */   }
/*     */   
/*     */   public boolean acceptsSuccess() {
/* 184 */     return (!this.closed && ((Boolean)this.level.getGameRules().get(GameRules.SEND_COMMAND_FEEDBACK)).booleanValue());
/*     */   }
/*     */   
/*     */   public boolean acceptsFailure() {
/* 189 */     return !this.closed;
/*     */   }
/*     */   
/*     */   public boolean shouldInformAdmins() {
/* 194 */     return (!this.closed && ((Boolean)this.level.getGameRules().get(GameRules.COMMAND_BLOCK_OUTPUT)).booleanValue());
/*     */   }
/*     */   
/*     */   public void sendSystemMessage(Component message) {
/* 199 */     if (!this.closed) {
/* 200 */       BaseCommandBlock.this.lastOutput = (Component)Component.literal("[" + TIME_FORMAT.format(ZonedDateTime.now()) + "] ").append(message);
/* 201 */       BaseCommandBlock.this.onUpdated(this.level);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void close() throws Exception {
/* 207 */     this.closed = true;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\BaseCommandBlock$CloseableCommandBlockSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */