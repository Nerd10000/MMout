/*     */ package net.minecraft.commands.arguments.blocks;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ class null implements Consumer<Property.Value<?>> {
/*     */   private boolean separate = false;
/*     */   
/*     */   public void accept(Property.Value<?> value) {
/* 483 */     if (this.separate)
/* 484 */       result.append(','); 
/* 487 */     result.append(value.property().getName());
/* 488 */     result.append('=');
/* 489 */     result.append(value.valueName());
/* 490 */     this.separate = true;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\blocks\BlockStateParser$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */