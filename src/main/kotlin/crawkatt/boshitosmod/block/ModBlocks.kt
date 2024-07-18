package crawkatt.boshitosmod.block

import crawkatt.boshitosmod.BoshitosMod
import crawkatt.boshitosmod.item.ModItems
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object ModBlocks {
    val BLOCKS: DeferredRegister<Block> = DeferredRegister.create(ForgeRegistries.BLOCKS, BoshitosMod.MOD_ID)

    val BROTENITA_BLOCK: RegistryObject<Block> = registerBlock("brotenita_block") {
        Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK))
    }

    // the returned ObjectHolderDelegate can be used as a property delegate
    // this is automatically registered by the deferred registry at the correct times
    private fun <T : Block> registerBlock(name: String, block: () -> T): RegistryObject<T> {
        val toReturn = BLOCKS.register(name, block)
        registerBlockItem(name, toReturn)
        return toReturn
    }

    private fun <T: Block> registerBlockItem(name: String, block: RegistryObject<T>) {
        ModItems.ITEMS.register(name) { BlockItem(block.get(), Item.Properties()) }
    }

    fun register(eventBus: IEventBus) {
        BLOCKS.register(eventBus)
    }
}