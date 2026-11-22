/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.component.InstrumentComponent;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ 
/*    */ public class InstrumentItem extends Item {
/*    */   public InstrumentItem(Item.Properties properties) {
/* 22 */     super(properties);
/*    */   }
/*    */   
/*    */   public static ItemStack create(Item item, Holder<Instrument> instrument) {
/* 26 */     ItemStack itemStack = new ItemStack(item);
/* 27 */     itemStack.set(DataComponents.INSTRUMENT, new InstrumentComponent(instrument));
/* 28 */     return itemStack;
/*    */   }
/*    */   
/*    */   public InteractionResult use(Level level, Player player, InteractionHand hand) {
/* 33 */     ItemStack itemStack = player.getItemInHand(hand);
/* 34 */     Optional<? extends Holder<Instrument>> instrumentHolder = getInstrument(itemStack);
/* 35 */     if (instrumentHolder.isPresent()) {
/* 36 */       Instrument instrument = (Instrument)((Holder)instrumentHolder.get()).value();
/* 37 */       player.startUsingItem(hand);
/* 38 */       play(level, player, instrument);
/* 39 */       player.getCooldowns().addCooldown(itemStack, Mth.floor(instrument.useDuration() * 20.0F));
/* 40 */       player.awardStat(Stats.ITEM_USED.get(this));
/* 41 */       return (InteractionResult)InteractionResult.CONSUME;
/*    */     } 
/* 43 */     return (InteractionResult)InteractionResult.FAIL;
/*    */   }
/*    */   
/*    */   public int getUseDuration(ItemStack itemStack, LivingEntity user) {
/* 48 */     Optional<Holder<Instrument>> instrument = getInstrument(itemStack);
/* 49 */     return ((Integer)instrument.<Integer>map(instrumentHolder -> Integer.valueOf(Mth.floor(((Instrument)instrumentHolder.value()).useDuration() * 20.0F))).orElse(Integer.valueOf(0))).intValue();
/*    */   }
/*    */   
/*    */   private static Optional<Holder<Instrument>> getInstrument(ItemStack itemStack) {
/* 53 */     InstrumentComponent instrument = (InstrumentComponent)itemStack.get(DataComponents.INSTRUMENT);
/* 54 */     return (instrument != null) ? Optional.<Holder<Instrument>>of(instrument.instrument()) : Optional.<Holder<Instrument>>empty();
/*    */   }
/*    */   
/*    */   public ItemUseAnimation getUseAnimation(ItemStack itemStack) {
/* 59 */     return ItemUseAnimation.TOOT_HORN;
/*    */   }
/*    */   
/*    */   private static void play(Level level, Player player, Instrument instrument) {
/* 63 */     SoundEvent soundEvent = (SoundEvent)instrument.soundEvent().value();
/* 64 */     float volume = instrument.range() / 16.0F;
/* 65 */     level.playSound((Entity)player, (Entity)player, soundEvent, SoundSource.RECORDS, volume, 1.0F);
/* 66 */     level.gameEvent((Holder)GameEvent.INSTRUMENT_PLAY, player.position(), GameEvent.Context.of((Entity)player));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\InstrumentItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */