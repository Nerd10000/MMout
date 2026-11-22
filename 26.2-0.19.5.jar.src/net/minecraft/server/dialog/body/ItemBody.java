/*    */ package net.minecraft.server.dialog.body;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function6;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.item.ItemStackTemplate;
/*    */ 
/*    */ public final class ItemBody extends Record implements DialogBody {
/*    */   private final ItemStackTemplate item;
/*    */   
/*    */   private final Optional<PlainMessage> description;
/*    */   
/*    */   private final boolean showDecorations;
/*    */   
/*    */   private final boolean showTooltip;
/*    */   
/*    */   private final int width;
/*    */   
/*    */   private final int height;
/*    */   
/*    */   public static final MapCodec<ItemBody> MAP_CODEC;
/*    */   
/*    */   public ItemBody(ItemStackTemplate item, Optional<PlainMessage> description, boolean showDecorations, boolean showTooltip, int width, int height) {
/* 11 */     this.item = item;
/* 11 */     this.description = description;
/* 11 */     this.showDecorations = showDecorations;
/* 11 */     this.showTooltip = showTooltip;
/* 11 */     this.width = width;
/* 11 */     this.height = height;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/body/ItemBody;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/body/ItemBody;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/body/ItemBody;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/body/ItemBody;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/body/ItemBody;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/body/ItemBody;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ItemStackTemplate item() {
/* 11 */     return this.item;
/*    */   }
/*    */   
/*    */   public Optional<PlainMessage> description() {
/* 11 */     return this.description;
/*    */   }
/*    */   
/*    */   public boolean showDecorations() {
/* 11 */     return this.showDecorations;
/*    */   }
/*    */   
/*    */   public boolean showTooltip() {
/* 11 */     return this.showTooltip;
/*    */   }
/*    */   
/*    */   public int width() {
/* 11 */     return this.width;
/*    */   }
/*    */   
/*    */   public int height() {
/* 11 */     return this.height;
/*    */   }
/*    */   
/*    */   static {
/* 19 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ItemStackTemplate.CODEC.fieldOf("item").forGetter(ItemBody::item), (App)PlainMessage.CODEC.optionalFieldOf("description").forGetter(ItemBody::description), (App)Codec.BOOL.optionalFieldOf("show_decorations", Boolean.valueOf(true)).forGetter(ItemBody::showDecorations), (App)Codec.BOOL.optionalFieldOf("show_tooltip", Boolean.valueOf(true)).forGetter(ItemBody::showTooltip), (App)ExtraCodecs.intRange(1, 256).optionalFieldOf("width", Integer.valueOf(16)).forGetter(ItemBody::width), (App)ExtraCodecs.intRange(1, 256).optionalFieldOf("height", Integer.valueOf(16)).forGetter(ItemBody::height)).apply((Applicative)i, ItemBody::new));
/*    */   }
/*    */   
/*    */   public MapCodec<ItemBody> mapCodec() {
/* 30 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\dialog\body\ItemBody.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */