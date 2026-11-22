/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.component.DataComponentGetter;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentUtils;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.component.TooltipProvider;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.JukeboxBlock;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ 
/*    */ public final class JukeboxPlayable extends Record implements TooltipProvider {
/*    */   private final Holder<JukeboxSong> song;
/*    */   
/*    */   public JukeboxPlayable(Holder<JukeboxSong> song) {
/* 27 */     this.song = song;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/JukeboxPlayable;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/JukeboxPlayable;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/JukeboxPlayable;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/JukeboxPlayable;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/JukeboxPlayable;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/JukeboxPlayable;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Holder<JukeboxSong> song() {
/* 27 */     return this.song;
/*    */   }
/*    */   
/* 28 */   public static final Codec<JukeboxPlayable> CODEC = JukeboxSong.CODEC.xmap(JukeboxPlayable::new, JukeboxPlayable::song);
/*    */   
/* 29 */   public static final StreamCodec<RegistryFriendlyByteBuf, JukeboxPlayable> STREAM_CODEC = StreamCodec.composite(JukeboxSong.STREAM_CODEC, JukeboxPlayable::song, JukeboxPlayable::new);
/*    */   
/*    */   public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
/* 36 */     consumer.accept(ComponentUtils.mergeStyles(((JukeboxSong)this.song.value()).description(), Style.EMPTY.withColor(ChatFormatting.GRAY)));
/*    */   }
/*    */   
/*    */   public static InteractionResult tryInsertIntoJukebox(Level level, BlockPos pos, ItemStack toInsert, Player player) {
/* 40 */     JukeboxPlayable jukeboxPlayable = (JukeboxPlayable)toInsert.get(DataComponents.JUKEBOX_PLAYABLE);
/* 41 */     if (jukeboxPlayable == null)
/* 42 */       return (InteractionResult)InteractionResult.TRY_WITH_EMPTY_HAND; 
/* 45 */     BlockState state = level.getBlockState(pos);
/* 46 */     if (!state.is(Blocks.JUKEBOX) || ((Boolean)state.getValue((Property)JukeboxBlock.HAS_RECORD)).booleanValue())
/* 47 */       return (InteractionResult)InteractionResult.TRY_WITH_EMPTY_HAND; 
/* 50 */     if (!level.isClientSide()) {
/* 51 */       ItemStack inserted = toInsert.consumeAndReturn(1, (LivingEntity)player);
/* 52 */       BlockEntity blockEntity = level.getBlockEntity(pos);
/* 52 */       if (blockEntity instanceof JukeboxBlockEntity) {
/* 52 */         JukeboxBlockEntity jukebox = (JukeboxBlockEntity)blockEntity;
/* 53 */         jukebox.setTheItem(inserted);
/* 54 */         level.gameEvent((Holder)GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of((Entity)player, state));
/*    */       } 
/* 57 */       player.awardStat(Stats.PLAY_RECORD);
/*    */     } 
/* 60 */     return (InteractionResult)InteractionResult.SUCCESS;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\JukeboxPlayable.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */