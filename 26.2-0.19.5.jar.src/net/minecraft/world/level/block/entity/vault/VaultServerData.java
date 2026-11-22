/*     */ package net.minecraft.world.level.block.entity.vault;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public class VaultServerData {
/*     */   public static final String TAG_NAME = "server_data";
/*     */   
/*     */   public static final Codec<VaultServerData> CODEC;
/*     */   
/*     */   private static final int MAX_REWARD_PLAYERS = 128;
/*     */   
/*     */   static {
/*  23 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)UUIDUtil.CODEC_LINKED_SET.lenientOptionalFieldOf("rewarded_players", Set.of()).forGetter(()), (App)Codec.LONG.lenientOptionalFieldOf("state_updating_resumes_at", Long.valueOf(0L)).forGetter(()), (App)ItemStack.CODEC.listOf().lenientOptionalFieldOf("items_to_eject", List.of()).forGetter(()), (App)Codec.INT.lenientOptionalFieldOf("total_ejections_needed", Integer.valueOf(0)).forGetter(())).apply((Applicative)i, VaultServerData::new));
/*     */   }
/*     */   
/*  32 */   private final Set<UUID> rewardedPlayers = (Set<UUID>)new ObjectLinkedOpenHashSet();
/*     */   
/*     */   private long stateUpdatingResumesAt;
/*     */   
/*  34 */   private final List<ItemStack> itemsToEject = (List<ItemStack>)new ObjectArrayList();
/*     */   
/*     */   private long lastInsertFailTimestamp;
/*     */   
/*     */   private int totalEjectionsNeeded;
/*     */   
/*     */   boolean isDirty;
/*     */   
/*     */   public VaultServerData(Set<UUID> rewardedPlayers, long stateUpdatingResumesAt, List<ItemStack> itemsToEject, int totalEjectionsNeeded) {
/*  42 */     this.rewardedPlayers.addAll(rewardedPlayers);
/*  43 */     this.stateUpdatingResumesAt = stateUpdatingResumesAt;
/*  44 */     this.itemsToEject.addAll(itemsToEject);
/*  45 */     this.totalEjectionsNeeded = totalEjectionsNeeded;
/*     */   }
/*     */   
/*     */   void setLastInsertFailTimestamp(long lastInsertFailTimestamp) {
/*  53 */     this.lastInsertFailTimestamp = lastInsertFailTimestamp;
/*     */   }
/*     */   
/*     */   long getLastInsertFailTimestamp() {
/*  58 */     return this.lastInsertFailTimestamp;
/*     */   }
/*     */   
/*     */   Set<UUID> getRewardedPlayers() {
/*  63 */     return this.rewardedPlayers;
/*     */   }
/*     */   
/*     */   boolean hasRewardedPlayer(Player player) {
/*  68 */     return this.rewardedPlayers.contains(player.getUUID());
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public void addToRewardedPlayers(Player player) {
/*  73 */     this.rewardedPlayers.add(player.getUUID());
/*  75 */     if (this.rewardedPlayers.size() > 128) {
/*  76 */       Iterator<UUID> iterator = this.rewardedPlayers.iterator();
/*  77 */       if (iterator.hasNext()) {
/*  78 */         iterator.next();
/*  79 */         iterator.remove();
/*     */       } 
/*     */     } 
/*  83 */     markChanged();
/*     */   }
/*     */   
/*     */   long stateUpdatingResumesAt() {
/*  88 */     return this.stateUpdatingResumesAt;
/*     */   }
/*     */   
/*     */   void pauseStateUpdatingUntil(long stateUpdatingResumesAt) {
/*  93 */     this.stateUpdatingResumesAt = stateUpdatingResumesAt;
/*  94 */     markChanged();
/*     */   }
/*     */   
/*     */   List<ItemStack> getItemsToEject() {
/*  99 */     return this.itemsToEject;
/*     */   }
/*     */   
/*     */   void markEjectionFinished() {
/* 104 */     this.totalEjectionsNeeded = 0;
/* 105 */     markChanged();
/*     */   }
/*     */   
/*     */   void setItemsToEject(List<ItemStack> newItemsToEject) {
/* 110 */     this.itemsToEject.clear();
/* 111 */     this.itemsToEject.addAll(newItemsToEject);
/* 112 */     this.totalEjectionsNeeded = this.itemsToEject.size();
/* 113 */     markChanged();
/*     */   }
/*     */   
/*     */   ItemStack getNextItemToEject() {
/* 118 */     if (this.itemsToEject.isEmpty())
/* 119 */       return ItemStack.EMPTY; 
/* 122 */     return Objects.<ItemStack>requireNonNullElse(this.itemsToEject.get(this.itemsToEject.size() - 1), ItemStack.EMPTY);
/*     */   }
/*     */   
/*     */   ItemStack popNextItemToEject() {
/* 127 */     if (this.itemsToEject.isEmpty())
/* 128 */       return ItemStack.EMPTY; 
/* 130 */     markChanged();
/* 132 */     return Objects.<ItemStack>requireNonNullElse(this.itemsToEject.remove(this.itemsToEject.size() - 1), ItemStack.EMPTY);
/*     */   }
/*     */   
/*     */   void set(VaultServerData from) {
/* 137 */     this.stateUpdatingResumesAt = from.stateUpdatingResumesAt();
/* 138 */     this.itemsToEject.clear();
/* 139 */     this.itemsToEject.addAll(from.itemsToEject);
/* 140 */     this.rewardedPlayers.clear();
/* 141 */     this.rewardedPlayers.addAll(from.rewardedPlayers);
/*     */   }
/*     */   
/*     */   private void markChanged() {
/* 145 */     this.isDirty = true;
/*     */   }
/*     */   
/*     */   public float ejectionProgress() {
/* 149 */     if (this.totalEjectionsNeeded == 1)
/* 150 */       return 1.0F; 
/* 153 */     return 1.0F - Mth.inverseLerp(getItemsToEject().size(), 1.0F, this.totalEjectionsNeeded);
/*     */   }
/*     */   
/*     */   public VaultServerData() {}
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\vault\VaultServerData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */