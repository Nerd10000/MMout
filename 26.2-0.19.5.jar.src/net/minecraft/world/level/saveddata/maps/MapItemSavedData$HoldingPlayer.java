/*     */ package net.minecraft.world.level.saveddata.maps;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ 
/*     */ public class HoldingPlayer {
/*     */   public final Player player;
/*     */   
/*     */   private boolean dirtyData = true;
/*     */   
/*     */   private int minDirtyX;
/*     */   
/*     */   private int minDirtyY;
/*     */   
/* 108 */   private int maxDirtyX = 127;
/*     */   
/* 109 */   private int maxDirtyY = 127;
/*     */   
/*     */   private boolean dirtyDecorations = true;
/*     */   
/*     */   private int tick;
/*     */   
/*     */   public int step;
/*     */   
/*     */   private HoldingPlayer(Player player) {
/* 115 */     this.player = player;
/*     */   }
/*     */   
/*     */   private MapItemSavedData.MapPatch createPatch() {
/* 119 */     int startX = this.minDirtyX;
/* 120 */     int startY = this.minDirtyY;
/* 121 */     int width = this.maxDirtyX + 1 - this.minDirtyX;
/* 122 */     int height = this.maxDirtyY + 1 - this.minDirtyY;
/* 124 */     byte[] patch = new byte[width * height];
/* 125 */     for (int x = 0; x < width; x++) {
/* 126 */       for (int y = 0; y < height; y++)
/* 127 */         patch[x + y * width] = MapItemSavedData.this.colors[startX + x + (startY + y) * 128]; 
/*     */     } 
/* 130 */     return new MapItemSavedData.MapPatch(startX, startY, width, height, patch);
/*     */   }
/*     */   
/*     */   private Packet<?> nextUpdatePacket(MapId id) {
/*     */     MapItemSavedData.MapPatch patch;
/*     */     Collection<MapDecoration> decorations;
/* 135 */     if (this.dirtyData) {
/* 136 */       this.dirtyData = false;
/* 137 */       patch = createPatch();
/*     */     } else {
/* 139 */       patch = null;
/*     */     } 
/* 143 */     if (this.dirtyDecorations && this.tick++ % 5 == 0) {
/* 144 */       this.dirtyDecorations = false;
/* 145 */       decorations = MapItemSavedData.this.decorations.values();
/*     */     } else {
/* 147 */       decorations = null;
/*     */     } 
/* 150 */     if (decorations != null || patch != null)
/* 151 */       return (Packet<?>)new ClientboundMapItemDataPacket(id, MapItemSavedData.this.scale, MapItemSavedData.this.locked, decorations, patch); 
/* 154 */     return null;
/*     */   }
/*     */   
/*     */   private void markColorsDirty(int x, int y) {
/* 158 */     if (this.dirtyData) {
/* 159 */       this.minDirtyX = Math.min(this.minDirtyX, x);
/* 160 */       this.minDirtyY = Math.min(this.minDirtyY, y);
/* 161 */       this.maxDirtyX = Math.max(this.maxDirtyX, x);
/* 162 */       this.maxDirtyY = Math.max(this.maxDirtyY, y);
/*     */     } else {
/* 164 */       this.dirtyData = true;
/* 165 */       this.minDirtyX = x;
/* 166 */       this.minDirtyY = y;
/* 167 */       this.maxDirtyX = x;
/* 168 */       this.maxDirtyY = y;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void markDecorationsDirty() {
/* 173 */     this.dirtyDecorations = true;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\saveddata\maps\MapItemSavedData$HoldingPlayer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */