/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.item.ItemStackTemplate;
/*     */ 
/*     */ public interface HoverEvent {
/*     */   public static final Codec<HoverEvent> CODEC;
/*     */   
/*     */   Action action();
/*     */   
/*     */   static {
/*  21 */     CODEC = Action.CODEC.dispatch("action", HoverEvent::action, action -> action.codec);
/*     */   }
/*     */   
/*     */   public static final class ShowText extends Record implements HoverEvent {
/*     */     private final Component value;
/*     */     
/*     */     public static final MapCodec<ShowText> CODEC;
/*     */     
/*     */     public ShowText(Component value) {
/*  25 */       this.value = value;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/HoverEvent$ShowText;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #25	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/HoverEvent$ShowText;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/HoverEvent$ShowText;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #25	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/HoverEvent$ShowText;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/HoverEvent$ShowText;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #25	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/network/chat/HoverEvent$ShowText;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Component value() {
/*  25 */       return this.value;
/*     */     }
/*     */     
/*     */     static {
/*  26 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ComponentSerialization.CODEC.fieldOf("value").forGetter(ShowText::value)).apply((Applicative)i, ShowText::new));
/*     */     }
/*     */     
/*     */     public HoverEvent.Action action() {
/*  32 */       return HoverEvent.Action.SHOW_TEXT;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class ShowItem extends Record implements HoverEvent {
/*     */     private final ItemStackTemplate item;
/*     */     
/*     */     public ShowItem(ItemStackTemplate item) {
/*  36 */       this.item = item;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/HoverEvent$ShowItem;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #36	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/HoverEvent$ShowItem;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/HoverEvent$ShowItem;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #36	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/HoverEvent$ShowItem;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/HoverEvent$ShowItem;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #36	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/network/chat/HoverEvent$ShowItem;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public ItemStackTemplate item() {
/*  36 */       return this.item;
/*     */     }
/*     */     
/*  37 */     public static final MapCodec<ShowItem> CODEC = ItemStackTemplate.MAP_CODEC.xmap(ShowItem::new, ShowItem::item);
/*     */     
/*     */     public HoverEvent.Action action() {
/*  41 */       return HoverEvent.Action.SHOW_ITEM;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class ShowEntity extends Record implements HoverEvent {
/*     */     private final HoverEvent.EntityTooltipInfo entity;
/*     */     
/*     */     public static final MapCodec<ShowEntity> CODEC;
/*     */     
/*     */     public ShowEntity(HoverEvent.EntityTooltipInfo entity) {
/*  45 */       this.entity = entity;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/HoverEvent$ShowEntity;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #45	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/HoverEvent$ShowEntity;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/HoverEvent$ShowEntity;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #45	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/HoverEvent$ShowEntity;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/HoverEvent$ShowEntity;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #45	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/network/chat/HoverEvent$ShowEntity;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public HoverEvent.EntityTooltipInfo entity() {
/*  45 */       return this.entity;
/*     */     }
/*     */     
/*     */     static {
/*  46 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)HoverEvent.EntityTooltipInfo.CODEC.forGetter(ShowEntity::entity)).apply((Applicative)i, ShowEntity::new));
/*     */     }
/*     */     
/*     */     public HoverEvent.Action action() {
/*  52 */       return HoverEvent.Action.SHOW_ENTITY;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class EntityTooltipInfo {
/*     */     public static final MapCodec<EntityTooltipInfo> CODEC;
/*     */     
/*     */     public final EntityType<?> type;
/*     */     
/*     */     public final UUID uuid;
/*     */     
/*     */     public final Optional<Component> name;
/*     */     
/*     */     private List<Component> linesCache;
/*     */     
/*     */     static {
/*  57 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("id").forGetter(()), (App)UUIDUtil.LENIENT_CODEC.fieldOf("uuid").forGetter(()), (App)ComponentSerialization.CODEC.optionalFieldOf("name").forGetter(())).apply((Applicative)i, EntityTooltipInfo::new));
/*     */     }
/*     */     
/*     */     public EntityTooltipInfo(EntityType<?> type, UUID uuid, Component name) {
/*  70 */       this(type, uuid, Optional.ofNullable(name));
/*     */     }
/*     */     
/*     */     public EntityTooltipInfo(EntityType<?> type, UUID uuid, Optional<Component> name) {
/*  74 */       this.type = type;
/*  75 */       this.uuid = uuid;
/*  76 */       this.name = name;
/*     */     }
/*     */     
/*     */     public List<Component> getTooltipLines() {
/*  80 */       if (this.linesCache == null) {
/*  81 */         this.linesCache = new ArrayList<>();
/*  82 */         Objects.requireNonNull(this.linesCache);
/*  82 */         this.name.ifPresent(this.linesCache::add);
/*  83 */         this.linesCache.add(Component.translatable("gui.entity_tooltip.type", new Object[] { this.type.getDescription() }));
/*  84 */         this.linesCache.add(Component.literal(this.uuid.toString()));
/*     */       } 
/*  86 */       return this.linesCache;
/*     */     }
/*     */     
/*     */     public boolean equals(Object o) {
/*  91 */       if (this == o)
/*  92 */         return true; 
/*  94 */       if (o == null || getClass() != o.getClass())
/*  95 */         return false; 
/*  98 */       EntityTooltipInfo that = (EntityTooltipInfo)o;
/*  99 */       return (this.type.equals(that.type) && this.uuid.equals(that.uuid) && this.name.equals(that.name));
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 104 */       int result = this.type.hashCode();
/* 105 */       result = 31 * result + this.uuid.hashCode();
/* 106 */       result = 31 * result + this.name.hashCode();
/* 107 */       return result;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum Action implements StringRepresentable {
/* 113 */     SHOW_TEXT("show_text", true, (String)HoverEvent.ShowText.CODEC),
/* 114 */     SHOW_ITEM("show_item", true, (String)HoverEvent.ShowItem.CODEC),
/* 115 */     SHOW_ENTITY("show_entity", true, (String)HoverEvent.ShowEntity.CODEC);
/*     */     
/* 118 */     public static final Codec<Action> UNSAFE_CODEC = StringRepresentable.fromValues(Action::values);
/*     */     
/* 119 */     public static final Codec<Action> CODEC = UNSAFE_CODEC.validate(Action::filterForSerialization);
/*     */     
/*     */     private final String name;
/*     */     
/*     */     private final boolean allowFromServer;
/*     */     
/*     */     private final MapCodec<? extends HoverEvent> codec;
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     Action(String name, boolean allowFromServer, MapCodec<? extends HoverEvent> codec) {
/* 126 */       this.name = name;
/* 127 */       this.allowFromServer = allowFromServer;
/* 128 */       this.codec = codec;
/*     */     }
/*     */     
/*     */     public boolean isAllowedFromServer() {
/* 132 */       return this.allowFromServer;
/*     */     }
/*     */     
/*     */     public String getSerializedName() {
/* 137 */       return this.name;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 142 */       return "<action " + this.name + ">";
/*     */     }
/*     */     
/*     */     private static DataResult<Action> filterForSerialization(Action action) {
/* 146 */       if (!action.isAllowedFromServer())
/* 147 */         return DataResult.error(() -> "Action not allowed: " + String.valueOf(action)); 
/* 149 */       return DataResult.success(action, Lifecycle.stable());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\HoverEvent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */