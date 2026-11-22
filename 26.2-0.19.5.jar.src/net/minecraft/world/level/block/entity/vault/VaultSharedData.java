/*    */ package net.minecraft.world.level.block.entity.vault;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
/*    */ import java.util.Set;
/*    */ import java.util.UUID;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.UUIDUtil;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class VaultSharedData {
/*    */   public static final String TAG_NAME = "shared_data";
/*    */   
/*    */   public static final Codec<VaultSharedData> CODEC;
/*    */   
/*    */   static {
/* 19 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)ItemStack.lenientOptionalFieldOf("display_item").forGetter(()), (App)UUIDUtil.CODEC_LINKED_SET.lenientOptionalFieldOf("connected_players", Set.of()).forGetter(()), (App)Codec.DOUBLE.lenientOptionalFieldOf("connected_particles_range", Double.valueOf(VaultConfig.DEFAULT.deactivationRange())).forGetter(())).apply((Applicative)i, VaultSharedData::new));
/*    */   }
/*    */   
/* 25 */   private ItemStack displayItem = ItemStack.EMPTY;
/*    */   
/* 26 */   private Set<UUID> connectedPlayers = (Set<UUID>)new ObjectLinkedOpenHashSet();
/*    */   
/* 27 */   private double connectedParticlesRange = VaultConfig.DEFAULT.deactivationRange();
/*    */   
/*    */   boolean isDirty;
/*    */   
/*    */   public VaultSharedData(ItemStack displayItem, Set<UUID> connectedPlayers, double connectedParticlesRange) {
/* 33 */     this.displayItem = displayItem;
/* 34 */     this.connectedPlayers.addAll(connectedPlayers);
/* 35 */     this.connectedParticlesRange = connectedParticlesRange;
/*    */   }
/*    */   
/*    */   public ItemStack getDisplayItem() {
/* 42 */     return this.displayItem;
/*    */   }
/*    */   
/*    */   public boolean hasDisplayItem() {
/* 46 */     return !this.displayItem.isEmpty();
/*    */   }
/*    */   
/*    */   public void setDisplayItem(ItemStack stack) {
/* 50 */     if (ItemStack.matches(this.displayItem, stack))
/*    */       return; 
/* 54 */     this.displayItem = stack.copy();
/* 55 */     markDirty();
/*    */   }
/*    */   
/*    */   boolean hasConnectedPlayers() {
/* 60 */     return !this.connectedPlayers.isEmpty();
/*    */   }
/*    */   
/*    */   Set<UUID> getConnectedPlayers() {
/* 65 */     return this.connectedPlayers;
/*    */   }
/*    */   
/*    */   double connectedParticlesRange() {
/* 70 */     return this.connectedParticlesRange;
/*    */   }
/*    */   
/*    */   void updateConnectedPlayersWithinRange(ServerLevel serverLevel, BlockPos pos, VaultServerData serverData, VaultConfig config, double limit) {
/* 78 */     Set<UUID> currentConnectedPlayers = (Set<UUID>)config.playerDetector().detect(serverLevel, config.entitySelector(), pos, limit, false).stream().filter(uuid -> !serverData.getRewardedPlayers().contains(uuid)).collect(Collectors.toSet());
/* 80 */     if (!this.connectedPlayers.equals(currentConnectedPlayers)) {
/* 81 */       this.connectedPlayers = currentConnectedPlayers;
/* 82 */       markDirty();
/*    */     } 
/*    */   }
/*    */   
/*    */   private void markDirty() {
/* 87 */     this.isDirty = true;
/*    */   }
/*    */   
/*    */   void set(VaultSharedData from) {
/* 92 */     this.displayItem = from.displayItem;
/* 93 */     this.connectedPlayers = from.connectedPlayers;
/* 94 */     this.connectedParticlesRange = from.connectedParticlesRange;
/*    */   }
/*    */   
/*    */   public VaultSharedData() {}
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\vault\VaultSharedData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */