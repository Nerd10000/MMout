/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.network.chat.ClickEvent;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ 
/*     */ public class SignText {
/*     */   private static final Codec<Component[]> LINES_CODEC;
/*     */   
/*     */   public static final Codec<SignText> DIRECT_CODEC;
/*     */   
/*     */   public static final int LINES = 4;
/*     */   
/*     */   private final Component[] messages;
/*     */   
/*     */   private final Component[] filteredMessages;
/*     */   
/*     */   private final DyeColor color;
/*     */   
/*     */   private final boolean hasGlowingText;
/*     */   
/*     */   private FormattedCharSequence[] renderMessages;
/*     */   
/*     */   private boolean renderMessagedFiltered;
/*     */   
/*     */   static {
/*  23 */     LINES_CODEC = ComponentSerialization.CODEC.listOf().comapFlatMap(input -> Util.fixedSize(input, 4).map(()), components -> List.of(components[0], components[1], components[2], components[3]));
/*  28 */     DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)LINES_CODEC.fieldOf("messages").forGetter(()), (App)LINES_CODEC.lenientOptionalFieldOf("filtered_messages").forGetter(SignText::filteredMessages), (App)ExtraCodecs.optionalAlwaysPresentFieldOf((Codec)DyeColor.CODEC, "color", DyeColor.BLACK).forGetter(()), (App)ExtraCodecs.optionalAlwaysPresentFieldOf((Codec)Codec.BOOL, "has_glowing_text", Boolean.valueOf(false)).forGetter(())).apply((Applicative)i, SignText::load));
/*     */   }
/*     */   
/*     */   public SignText() {
/*  44 */     this(emptyMessages(), emptyMessages(), DyeColor.BLACK, false);
/*     */   }
/*     */   
/*     */   public SignText(Component[] messages, Component[] filteredMessages, DyeColor color, boolean hasGlowingText) {
/*  48 */     this.messages = messages;
/*  49 */     this.filteredMessages = filteredMessages;
/*  50 */     this.color = color;
/*  51 */     this.hasGlowingText = hasGlowingText;
/*     */   }
/*     */   
/*     */   private static Component[] emptyMessages() {
/*  55 */     return new Component[] { CommonComponents.EMPTY, CommonComponents.EMPTY, CommonComponents.EMPTY, CommonComponents.EMPTY };
/*     */   }
/*     */   
/*     */   private static SignText load(Component[] messages, Optional<Component[]> filteredMessages, DyeColor color, boolean hasGlowingText) {
/*  59 */     return new SignText(messages, filteredMessages.orElse(Arrays.<Component>copyOf(messages, messages.length)), color, hasGlowingText);
/*     */   }
/*     */   
/*     */   public boolean hasGlowingText() {
/*  63 */     return this.hasGlowingText;
/*     */   }
/*     */   
/*     */   public SignText setHasGlowingText(boolean hasGlowingText) {
/*  67 */     if (hasGlowingText == this.hasGlowingText)
/*  68 */       return this; 
/*  70 */     return new SignText(this.messages, this.filteredMessages, this.color, hasGlowingText);
/*     */   }
/*     */   
/*     */   public DyeColor getColor() {
/*  74 */     return this.color;
/*     */   }
/*     */   
/*     */   public SignText setColor(DyeColor color) {
/*  78 */     if (color == getColor())
/*  79 */       return this; 
/*  81 */     return new SignText(this.messages, this.filteredMessages, color, this.hasGlowingText);
/*     */   }
/*     */   
/*     */   public Component getMessage(int index, boolean shouldFilter) {
/*  85 */     return getMessages(shouldFilter)[index];
/*     */   }
/*     */   
/*     */   public SignText setMessage(int index, Component message) {
/*  89 */     return setMessage(index, message, message);
/*     */   }
/*     */   
/*     */   public SignText setMessage(int index, Component rawMessage, Component filteredMessage) {
/*  93 */     Component[] messages = Arrays.<Component>copyOf(this.messages, this.messages.length);
/*  94 */     Component[] filteredMessages = Arrays.<Component>copyOf(this.filteredMessages, this.filteredMessages.length);
/*  95 */     messages[index] = rawMessage;
/*  96 */     filteredMessages[index] = filteredMessage;
/*  97 */     return new SignText(messages, filteredMessages, this.color, this.hasGlowingText);
/*     */   }
/*     */   
/*     */   public boolean hasMessage(Player player) {
/* 101 */     return Arrays.<Component>stream(getMessages(player.isTextFilteringEnabled())).anyMatch(component -> !component.getString().isEmpty());
/*     */   }
/*     */   
/*     */   public Component[] getMessages(boolean shouldFilter) {
/* 105 */     return shouldFilter ? this.filteredMessages : this.messages;
/*     */   }
/*     */   
/*     */   public FormattedCharSequence[] getRenderMessages(boolean shouldFilter, Function<Component, FormattedCharSequence> prepare) {
/* 109 */     if (this.renderMessages == null || this.renderMessagedFiltered != shouldFilter) {
/* 110 */       this.renderMessagedFiltered = shouldFilter;
/* 111 */       this.renderMessages = new FormattedCharSequence[4];
/* 112 */       for (int i = 0; i < 4; i++)
/* 113 */         this.renderMessages[i] = prepare.apply(getMessage(i, shouldFilter)); 
/*     */     } 
/* 116 */     return this.renderMessages;
/*     */   }
/*     */   
/*     */   private Optional<Component[]> filteredMessages() {
/* 120 */     for (int i = 0; i < 4; i++) {
/* 121 */       if (!this.filteredMessages[i].equals(this.messages[i]))
/* 122 */         return (Optional)Optional.of(this.filteredMessages); 
/*     */     } 
/* 125 */     return (Optional)Optional.empty();
/*     */   }
/*     */   
/*     */   public boolean hasAnyClickCommands(Player player) {
/* 129 */     for (Component message : getMessages(player.isTextFilteringEnabled())) {
/* 130 */       Style style = message.getStyle();
/* 131 */       ClickEvent event = style.getClickEvent();
/* 132 */       if (event != null && event.action() == ClickEvent.Action.RUN_COMMAND)
/* 133 */         return true; 
/*     */     } 
/* 136 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\SignText.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */