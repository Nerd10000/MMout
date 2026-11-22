/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.EntityTypes;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.decoration.GlowItemFrame;
/*    */ import net.minecraft.world.entity.decoration.HangingEntity;
/*    */ import net.minecraft.world.entity.decoration.ItemFrame;
/*    */ import net.minecraft.world.entity.decoration.painting.Painting;
/*    */ import net.minecraft.world.entity.decoration.painting.PaintingVariant;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.component.TooltipDisplay;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ 
/*    */ public class HangingEntityItem extends Item {
/* 27 */   private static final Component TOOLTIP_RANDOM_VARIANT = (Component)Component.translatable("painting.random").withStyle(ChatFormatting.GRAY);
/*    */   
/*    */   private final EntityType<? extends HangingEntity> type;
/*    */   
/*    */   public HangingEntityItem(EntityType<? extends HangingEntity> type, Item.Properties properties) {
/* 32 */     super(properties);
/* 33 */     this.type = type;
/*    */   }
/*    */   
/*    */   public InteractionResult useOn(UseOnContext context) {
/*    */     GlowItemFrame glowItemFrame;
/* 38 */     BlockPos pos = context.getClickedPos();
/* 39 */     Direction clickedFace = context.getClickedFace();
/* 41 */     BlockPos blockPos = pos.relative(clickedFace);
/* 42 */     Player player = context.getPlayer();
/* 43 */     ItemStack itemInHand = context.getItemInHand();
/* 45 */     if (player != null && !mayPlace(player, clickedFace, itemInHand, blockPos))
/* 46 */       return (InteractionResult)InteractionResult.FAIL; 
/* 49 */     Level level = context.getLevel();
/* 51 */     if (this.type == EntityTypes.PAINTING) {
/* 52 */       Optional<Painting> painting = Painting.create(level, blockPos, clickedFace);
/* 53 */       if (painting.isEmpty())
/* 54 */         return (InteractionResult)InteractionResult.CONSUME; 
/* 56 */       HangingEntity entity = (HangingEntity)painting.get();
/* 57 */     } else if (this.type == EntityTypes.ITEM_FRAME) {
/* 58 */       ItemFrame itemFrame = new ItemFrame(level, blockPos, clickedFace);
/* 59 */     } else if (this.type == EntityTypes.GLOW_ITEM_FRAME) {
/* 60 */       glowItemFrame = new GlowItemFrame(level, blockPos, clickedFace);
/*    */     } else {
/* 62 */       return (InteractionResult)InteractionResult.SUCCESS;
/*    */     } 
/* 65 */     EntityType.createDefaultStackConfig(level, itemInHand, (LivingEntity)player).apply((Entity)glowItemFrame);
/* 67 */     if (glowItemFrame.survives()) {
/* 68 */       if (!level.isClientSide()) {
/* 69 */         glowItemFrame.playPlacementSound();
/* 70 */         level.gameEvent((Entity)player, (Holder)GameEvent.ENTITY_PLACE, glowItemFrame.position());
/* 71 */         level.addFreshEntity((Entity)glowItemFrame);
/*    */       } 
/* 73 */       itemInHand.shrink(1);
/* 74 */       return (InteractionResult)InteractionResult.SUCCESS;
/*    */     } 
/* 77 */     return (InteractionResult)InteractionResult.CONSUME;
/*    */   }
/*    */   
/*    */   protected boolean mayPlace(Player player, Direction direction, ItemStack itemStack, BlockPos blockPos) {
/* 81 */     return (!direction.getAxis().isVertical() && player.mayUseItemAt(blockPos, direction, itemStack));
/*    */   }
/*    */   
/*    */   public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
/* 86 */     if (this.type == EntityTypes.PAINTING && display.shows(DataComponents.PAINTING_VARIANT)) {
/* 87 */       Holder<PaintingVariant> variant = (Holder<PaintingVariant>)itemStack.get(DataComponents.PAINTING_VARIANT);
/* 88 */       if (variant != null) {
/* 89 */         ((PaintingVariant)variant.value()).title().ifPresent(builder);
/* 90 */         ((PaintingVariant)variant.value()).author().ifPresent(builder);
/* 91 */         builder.accept(Component.translatable("painting.dimensions", new Object[] { Integer.valueOf(((PaintingVariant)variant.value()).width()), Integer.valueOf(((PaintingVariant)variant.value()).height()) }));
/* 92 */       } else if (tooltipFlag.isCreative()) {
/* 93 */         builder.accept(TOOLTIP_RANDOM_VARIANT);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\HangingEntityItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */