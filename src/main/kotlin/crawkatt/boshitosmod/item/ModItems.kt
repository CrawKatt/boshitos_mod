package crawkatt.boshitosmod.item

import crawkatt.boshitosmod.BoshitosMod
import net.minecraft.world.item.Item
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

// Con `object` se define una "clase" de Java con los parámetros públicos. NO USAR CLASS
object ModItems {
    val ITEMS: DeferredRegister<Item> = DeferredRegister.create(ForgeRegistries.ITEMS, BoshitosMod.MOD_ID)

    // las llaves seguido del String, son el equivalente a `() -> new Item(new Item.Properties())`, no es necesario el `new`
    val RAW_BROTENITA: RegistryObject<Item> = ITEMS.register("raw_brotenita") {
        Item(Item.Properties())
    }

    /*
    fun register(eventBus: IEventBus) {
        ITEMS.register(eventBus)
    }
    */
}