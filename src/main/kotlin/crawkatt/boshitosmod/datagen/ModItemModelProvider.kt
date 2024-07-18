package crawkatt.boshitosmod.datagen

import crawkatt.boshitosmod.BoshitosMod
import crawkatt.boshitosmod.item.ModItems
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraftforge.client.model.generators.ItemModelBuilder
import net.minecraftforge.client.model.generators.ItemModelProvider
import net.minecraftforge.common.data.ExistingFileHelper
import net.minecraftforge.registries.RegistryObject

class ModItemModelProvider(output: PackOutput, existingFileHelper: ExistingFileHelper) :
    ItemModelProvider(output, BoshitosMod.MOD_ID, existingFileHelper) {
    override fun registerModels() {
        simpleItem(ModItems.RAW_BROTENITA)
    }

    // TODO: Cambiar el Item a futuro
    private fun simpleItem(item: RegistryObject<Item>): ItemModelBuilder {
        return withExistingParent(item.id.path, ResourceLocation("item/generated")).texture("layer0",
            ResourceLocation(BoshitosMod.MOD_ID, "item/${item.id.path}")
        )
    }
}