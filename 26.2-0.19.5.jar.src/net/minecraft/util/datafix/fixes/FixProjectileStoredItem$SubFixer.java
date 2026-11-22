package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.Typed;
import com.mojang.datafixers.types.Type;

interface SubFixer<F> {
  Typed<F> fix(Typed<?> paramTyped, Type<F> paramType);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\FixProjectileStoredItem$SubFixer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */