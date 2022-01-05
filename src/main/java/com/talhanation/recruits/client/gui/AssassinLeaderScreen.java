package com.talhanation.recruits.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.talhanation.recruits.Main;
import com.talhanation.recruits.entities.AbstractRecruitEntity;
import com.talhanation.recruits.entities.AssassinLeaderEntity;
import com.talhanation.recruits.inventory.AssassinLeaderContainer;
import com.talhanation.recruits.inventory.RecruitInventoryContainer;
import com.talhanation.recruits.network.*;
import de.maxhenkel.corelib.inventory.ScreenBase;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

import java.util.UUID;


@OnlyIn(Dist.CLIENT)
public class AssassinLeaderScreen extends ScreenBase<AssassinLeaderContainer> {
    private static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(Main.MOD_ID,"textures/gui/assassin_gui.png");

    private static final ITextComponent TEXT_HEALTH = new TranslationTextComponent("gui.recruits.health");
    private static final ITextComponent TEXT_LEVEL = new TranslationTextComponent("gui.recruits.level");
    private static final ITextComponent TEXT_GROUP = new TranslationTextComponent("gui.recruits.group");
    private static final ITextComponent TEXT_KILLS = new TranslationTextComponent("gui.recruits.kills");

    private static final int fontColor = 4210752;

    private final PlayerInventory playerInventory;
    private final AssassinLeaderEntity assassinLeaderEntity;
    private TextFieldWidget textField;

    private UUID targetUUID;
    private int count = 1;

    public AssassinLeaderScreen(AssassinLeaderContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(RESOURCE_LOCATION, container, playerInventory, title);
        this.playerInventory = playerInventory;
        this.assassinLeaderEntity = container.getEntity();
        imageWidth = 176;
        imageHeight = 218;
    }


    @Override
    protected void init() {
        super.init();
        minecraft.keyboardHandler.setSendRepeatsToGui(true);


        //GROUP
        addButton(new Button(leftPos + 77, topPos + 100, 8, 12, new StringTextComponent("<"), button -> {
            if (this.count != 0) {
                this.count--;
            }
        }));

        addButton(new Button(leftPos + 77 + 85, topPos + 100, 8, 12, new StringTextComponent(">"), button -> {
            if (this.count != 16) {
                this.count++;
            }
        }));


        //HUNT
        addButton(new Button(leftPos + 77 + 65, topPos + 4, 30, 12, new StringTextComponent("Hunt"), button -> {
            playerInventory.player.sendMessage(new StringTextComponent("TEXT: " + textField.getValue()), playerInventory.player.getUUID());
            Main.SIMPLE_CHANNEL.sendToServer(new MessageAssassinate(textField.getValue(), this.count));
        onClose();

        }));

        textField = new TextFieldWidget(font, leftPos + 30, topPos + 30, 116, 16, new StringTextComponent(""));
        textField.setTextColor(-1);
        textField.setTextColorUneditable(-1);
        textField.setBordered(true);
        textField.setMaxLength(24);

        addButton(textField);
        setInitialFocus(textField);
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
        super.renderLabels(matrixStack, mouseX, mouseY);

        int k = 79;//rechst links
        int l = 19;//höhe
        //Titles
        //font.draw(matrixStack, recruit.getDisplayName().getVisualOrderText(), 8, 5, fontColor);
        font.draw(matrixStack, playerInventory.getDisplayName().getVisualOrderText(), 8, this.imageHeight - 96 + 2, fontColor);

        //Info
        //font.draw(matrixStack, "Lvl:" + assassin.getXpLevel(), k + 25, l + 10, fontColor);

    }

    protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        super.renderBg(matrixStack, partialTicks, mouseX, mouseY);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;

        //InventoryScreen.renderEntityInInventory(i + 50, j + 82, 30, (float)(i + 50) - mouseX, (float)(j + 75 - 50) - mouseY, this.recruit);
    }


    @Override
    public boolean keyPressed(int key, int a, int b) {
        if (key == GLFW.GLFW_KEY_ESCAPE) {
            minecraft.player.closeContainer();
            return true;
        }

        return textField.keyPressed(key, a, b) || textField.canConsumeInput() || super.keyPressed(key, a, b);
    }

    @Override
    public void onClose() {
        super.onClose();
        minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
